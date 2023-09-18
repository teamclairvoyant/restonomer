package com.clairvoyant.restonomer.persistence

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.mock.S3BucketMock
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader
import com.clairvoyant.data.scalaxy.writer.aws.s3.formats.JSONFileFormat
import org.apache.spark.sql.SaveMode

class BigQueryPersistenceSpec extends DataFrameReader with DataFrameMatcher {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |  "col_A": "val_A",
      |  "col_B": "val_B",
      |  "col_C": "val_C"
      |}
      |""".stripMargin
  )

  "persist()" should "save the dataframe to BigQuery Table named RestonomerTable in restonomer dataset" in {
    val bigQueryPersistence = BigQuery(
      serviceAccountCredentialsFile = Option("/Users/mandar179178/Downloads/restonomer-bq-service-account.json"),
      table = "rn_table",
      dataset = Option("restonomer"),
      temporaryGcsBucket = Option("innersource-restonomer"),
      createDisposition = None,
      writeDisposition = None,
      partitionField = None,
      clusteredFields = None,
      saveMode = SaveMode.ErrorIfExists
    )
    bigQueryPersistence.persist(restonomerResponseDF)
  }

}
