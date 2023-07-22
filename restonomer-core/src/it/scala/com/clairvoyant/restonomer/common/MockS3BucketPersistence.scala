package com.clairvoyant.restonomer.common

import org.apache.spark.sql.{DataFrame, SparkSession}

trait MockS3BucketPersistence {

  val s3MockBucketName = "test-bucket"
  val s3MockFilePath = "test-output-dir"

  def outputDF(implicit sparkSession: SparkSession): DataFrame =
    sparkSession.read.parquet(s"s3a://$s3MockBucketName/$s3MockFilePath")

}
