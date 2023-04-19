package com.clairvoyant.restonomer.common

import com.clairvoyant.restonomer.core.app.RestonomerContext
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

trait IntegrationTestDependencies
    extends AnyFlatSpec
    with Matchers
    with MockedHttpServer
    with DataFrameMatchers
    with BeforeAndAfterEach {

  given sparkSession: SparkSession =
    SparkSession
      .builder()
      .master("local[*]")
      .getOrCreate()

  def runCheckpoint(checkpointFileName: String): Unit =
    RestonomerContext(s"$resourcesDirectoryPath/restonomer_context")
      .runCheckpoint(checkpointFilePath = s"$mappingsDirectory/$checkpointFileName")

  given fileNameToDataFrameConversion: Conversion[String, DataFrame] with

    override def apply(fileName: String): DataFrame =
      sparkSession.read
        .option("multiline", value = true)
        .json(s"$mockDataRootDirectoryPath/$mappingsDirectory/$fileName")

}
