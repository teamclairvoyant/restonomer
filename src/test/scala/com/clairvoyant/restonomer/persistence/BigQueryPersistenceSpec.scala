package com.clairvoyant.restonomer.persistence

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader
import com.clairvoyant.data.scalaxy.writer.gcp.bigquery.types.{DirectBigQueryWriterType, IndirectBigQueryWriterType}
import org.scalatest.BeforeAndAfterEach

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

  "persist() - with direct write" should "save the dataframe to the big query table" in {
    val bigQueryPersistence = BigQuery(
      serviceAccountCredentialsFile = Some("/Users/rahulbhatia/Downloads/playground-375318-f7d2dda86716.json"),
      table = "playground-375318:restonomer.dummy_table_001",
      writerType = DirectBigQueryWriterType()
    )

    bigQueryPersistence.persist(restonomerResponseDF)
  }

  "persist() - with indirect write" should "save the dataframe to the big query table" in {
    val bigQueryPersistence = BigQuery(
      serviceAccountCredentialsFile = Some("/Users/rahulbhatia/Downloads/playground-375318-f7d2dda86716.json"),
      table = "playground-375318:restonomer.dummy_table_002",
      writerType = IndirectBigQueryWriterType(
        temporaryGcsBucket = "innersource-restonomer"
      )
    )

    bigQueryPersistence.persist(restonomerResponseDF)
  }

}
