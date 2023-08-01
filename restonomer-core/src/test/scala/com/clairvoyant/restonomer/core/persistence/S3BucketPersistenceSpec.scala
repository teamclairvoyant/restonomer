package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.restonomer.core.common.S3MockSpec.*
import com.clairvoyant.restonomer.core.common.{CoreSpec, S3MockSpec}
import com.clairvoyant.restonomer.spark.utils.writer.DataFrameToS3BucketWriter
import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.DataFrame
import com.clairvoyant.data.scalaxy.reader.text.JSONTextToDataFrameReader

class S3BucketPersistenceSpec extends CoreSpec with S3MockSpec {

  val hadoopConfigurations: Configuration = sparkSession.sparkContext.hadoopConfiguration

  hadoopConfigurations.set("fs.s3a.endpoint", s3MockEndpoint)
  hadoopConfigurations.set("fs.s3a.access.key", s3MockAWSAccessKey)
  hadoopConfigurations.set("fs.s3a.secret.key", s3MockAWSSecretKey)
  hadoopConfigurations.set("fs.s3a.path.style.access", "true")
  hadoopConfigurations.set("fs.s3a.change.detection.version.required", "false")

  val restonomerResponseDF: DataFrame = JSONTextToDataFrameReader()
    .read(
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
      fileFormat = "JSON",
      filePath = "test-output-dir"
    )

    s3Client.createBucket(s3BucketPersistence.bucketName)

    s3BucketPersistence.persist(
      restonomerResponseDF,
      new DataFrameToS3BucketWriter(
        bucketName = s3BucketPersistence.bucketName,
        fileFormat = s3BucketPersistence.fileFormat,
        filePath = s3BucketPersistence.filePath,
        saveMode = s3BucketPersistence.saveMode
      )
    )

    sparkSession.read.json(
      s"s3a://${s3BucketPersistence.bucketName}/${s3BucketPersistence.filePath}"
    ) should matchExpectedDataFrame(restonomerResponseDF)
  }

}
