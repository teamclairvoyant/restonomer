package com.clairvoyant.restonomer.persistence

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader
import com.clairvoyant.data.scalaxy.writer.gcp.bigquery.types.{DirectBigQueryWriterType, IndirectBigQueryWriterType}
import com.clairvoyant.data.scalaxy.writer.local.file.formats.*
import org.apache.commons.io.FileUtils
import org.scalatest.BeforeAndAfterEach

import java.io.File

class BigQueryPersistenceSpec extends DataFrameReader with DataFrameMatcher with BeforeAndAfterEach {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |  "col_A": "val_A",
      |  "col_B": "val_B",
      |  "col_C": "val_C",
      |  "col_D": "val_D"
      |}
      |""".stripMargin
  )

  lazy val dataFrameToFileSystemWriterOutputDirPath = s"out_${System.currentTimeMillis()}"

  "persist() - with Indirect format" should "save the df to the bq" in {

      val bqPersistence = BigQuery(
        serviceAccountCredentialsFile = Option("/Users/mandar179178/Documents/playground-375318-f7d2dda86716.json"),
        table = "restonomer.dummy_table_001",
        // project = Option("playground-375318"),
        writerType = IndirectBigQueryWriterType(temporaryGcsBucket = "gs://innersource-restonomer/task-big-query")
      )
      bqPersistence.persist(restonomerResponseDF)
    }

  "persist() - with direct format" should "save the df to the bq" in {

    val bqPersistence = BigQuery(
      serviceAccountCredentialsFile = Option("/Users/mandar179178/Documents/playground-375318-f7d2dda86716.json"),
      table = "restonomer.dummy_table_001",
      // project = Option("playground-375318"),
      writerType = DirectBigQueryWriterType()
    )
    bqPersistence.persist(restonomerResponseDF)
  }
}
