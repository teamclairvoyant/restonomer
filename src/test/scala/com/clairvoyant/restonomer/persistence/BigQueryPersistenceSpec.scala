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

  "persist() - with indirect write" should "save the dataframe to the big query table" in {
    val indirectBigQueryWriterType = IndirectBigQueryWriterType(
      temporaryGcsBucket = "gs://innersource-restonomer/task-big-query"
    )

    val bigQueryPersistence = BigQuery(
      serviceAccountCredentialsFile = Some("/Users/mandar179178/Documents/playground-375318-f7d2dda86716.json"),
      table = "restonomer.dummy_table_001",
      writerType = indirectBigQueryWriterType
    )

    bigQueryPersistence.persist(restonomerResponseDF)
  }

  "persist() - with direct write" should "save the dataframe to the big query table" in {
    val bigQueryPersistence = BigQuery(
      serviceAccountCredentialsFile = Option("/Users/mandar179178/Documents/playground-375318-f7d2dda86716.json"),
      table = "restonomer.dummy_table_001",
      writerType = DirectBigQueryWriterType()
    )

    bigQueryPersistence.persist(restonomerResponseDF)
  }

}
