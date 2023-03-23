package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import com.clairvoyant.restonomer.spark.utils.writer.DataFrameToS3BucketWriter
import org.apache.spark.sql.DataFrame

class S3BucketPersistenceSpec extends CoreSpec {

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(text =
      Seq(
        """
          |{
          |  "col_A": "val_A",
          |  "col_B": "val_B",
          |  "col_C": "val_C"
          |}
          |""".stripMargin
      )
    )

  "persist()" should "save the dataframe to the files in the s3 bucket" in {
    val bucketName = "test-bucket"
    val filePath = "test-output-dir"

    s3Client.createBucket(bucketName)

    val s3BucketPersistence = S3Bucket(
      bucketName = "test-bucket",
      fileFormat = "JSON",
      filePath = filePath
    )

    s3BucketPersistence.persist(
      restonomerResponseDF,
      new DataFrameToS3BucketWriter(
        bucketName = s3BucketPersistence.bucketName,
        fileFormat = s3BucketPersistence.fileFormat,
        filePath = s3BucketPersistence.filePath,
        saveMode = s3BucketPersistence.saveMode
      )
    )
  }

  override def beforeAll(): Unit = s3Mock.start
  override def afterAll(): Unit = s3Mock.stop

}
