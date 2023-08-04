package com.clairvoyant.restonomer.common

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil
import com.clairvoyant.restonomer.core.app.RestonomerContext
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StructType
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec

trait IntegrationTestDependencies extends DataScalaxyTestUtil with MockedHttpServer with BeforeAndAfterEach {

  val expectedDFSchema: Option[StructType] = None

  def runCheckpoint(checkpointFileName: String): Unit =
    RestonomerContext(s"$resourcesDirectoryPath/restonomer_context")
      .runCheckpoint(checkpointFilePath = s"$mappingsDirectory/$checkpointFileName")

  given fileNameToDataFrameConversion: Conversion[String, DataFrame] with

    override def apply(fileName: String): DataFrame = {
      val dataFrameReader = sparkSession.read
        .option("multiline", value = true)
        .option("inferSchema", value = expectedDFSchema.isEmpty)

      expectedDFSchema
        .map { schema => dataFrameReader.schema(schema) }
        .getOrElse(dataFrameReader)
        .json(s"$mockDataRootDirectoryPath/$mappingsDirectory/$fileName")
    }

}
