package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.mock.S3BucketMock
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader
import com.clairvoyant.data.scalaxy.writer.aws.s3.formats.JSONFileFormat

class S3BucketPersistenceSpec extends DataFrameReader with DataFrameMatcher with S3BucketMock {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |  "col_A": "val_A",
      |  "col_B": "val_B",
      |  "col_C": "val_C"
      |}
      |""".stripMargin
  )

  "persist()" should "save the dataframe to the files in the s3 bucket" in {
    val s3BucketPersistence = S3Bucket(
      bucketName = "test-bucket",
      fileFormat = JSONFileFormat(),
      filePath = "test-output-dir"
    )

    s3Client.createBucket(s3BucketPersistence.bucketName)

    s3BucketPersistence.persist(restonomerResponseDF)

    readJSONFromFile(
      s"s3a://${s3BucketPersistence.bucketName}/${s3BucketPersistence.filePath}"
    ) should matchExpectedDataFrame(restonomerResponseDF)
  }

}
