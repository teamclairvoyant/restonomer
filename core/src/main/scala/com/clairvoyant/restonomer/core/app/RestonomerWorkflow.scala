package com.clairvoyant.restonomer.core.app

import akka.actor.ActorSystem
import com.clairvoyant.restonomer.core.converter.ResponseToDataFrameConverter
import com.clairvoyant.restonomer.core.http.{RestonomerRequest, RestonomerResponse}
import com.clairvoyant.restonomer.core.model.{ApplicationConfig, CheckpointConfig}
import com.clairvoyant.restonomer.core.persistence.{FileSystem, RestonomerPersistence}
import com.clairvoyant.restonomer.spark.utils.writer.DataFrameToFileSystemWriter
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import sttp.client3.SttpBackend
import sttp.client3.akkahttp.AkkaHttpBackend

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class RestonomerWorkflow(implicit sparkSession: SparkSession) {

  def run(checkpointConfig: CheckpointConfig): Unit = {
    implicit val actorSystem: ActorSystem = ActorSystem(checkpointConfig.name)
    implicit val akkaHttpBackend: SttpBackend[Future, Any] = AkkaHttpBackend.usingActorSystem(actorSystem)

    val restonomerRequest = RestonomerRequest.builder(checkpointConfig.request).build
    val restonomerResponse = RestonomerResponse.fetchFromRequest(restonomerRequest)

    val restonomerResponseDF = restonomerResponse.body
      .map(ResponseToDataFrameConverter(checkpointConfig.response.body.format).convertResponseToDataFrame)

    val restonomerResponseTransformedDF = restonomerResponseDF.map { df =>
      checkpointConfig.response.transformations
        .foldLeft(df) { case (df, restonomerTransformation) =>
          restonomerTransformation.transform(df)
        }
    }

    val persistedRestonomerResponseDF = persistRestonomerResponseDataFrame(
      restonomerResponseDF = restonomerResponseTransformedDF,
      restonomerPersistence = checkpointConfig.response.persistence
    )

    Await.result(persistedRestonomerResponseDF, Duration.Inf)
    Await.result(actorSystem.terminate(), Duration.Inf)
  }

  private def persistRestonomerResponseDataFrame(
      restonomerResponseDF: Future[DataFrame],
      restonomerPersistence: RestonomerPersistence
  ): Future[Unit] = {
    val dataFrameWriter =
      restonomerPersistence match {
        case FileSystem(fileFormat, filePath) =>
          new DataFrameToFileSystemWriter(
            sparkSession = sparkSession,
            fileFormat = fileFormat,
            filePath = filePath
          )
      }

    restonomerResponseDF.map(restonomerPersistence.persist(_, dataFrameWriter))
  }

}

private object RestonomerWorkflow {

  def apply(applicationConfig: ApplicationConfig): RestonomerWorkflow = {
    implicit val sparkSession: SparkSession = SparkSession
      .builder()
      .config(
        applicationConfig.sparkConfigs
          .map { sparkConfigs =>
            sparkConfigs.foldLeft(new SparkConf()) { case (sparkConf, sparkConfig) =>
              sparkConf.set(sparkConfig._1, sparkConfig._2)
            }
          }
          .getOrElse(new SparkConf())
      )
      .getOrCreate()

    new RestonomerWorkflow()
  }

}
