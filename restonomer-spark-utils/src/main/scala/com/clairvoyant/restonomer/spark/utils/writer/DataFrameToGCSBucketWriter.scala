package com.clairvoyant.restonomer.spark.utils.writer

import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

class DataFrameToGCSBucketWriter(
    serviceAccountCredentialsFile: Option[String],
    bucketName: String,
    fileFormat: String,
    filePath: String,
    saveMode: String
) extends DataFrameWriter {

  override def write(dataFrame: DataFrame)(using sparkSession: SparkSession): Unit = {

    val hadoopConfigurations: Configuration = sparkSession.sparkContext.hadoopConfiguration

    hadoopConfigurations.set("fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem")
    hadoopConfigurations.set("google.cloud.auth.service.account.enable", "true")
    hadoopConfigurations.set(
      "google.cloud.auth.service.account.json.keyfile",
      serviceAccountCredentialsFile.getOrElse("")
    )

    dataFrame.write
      .mode(saveMode)
      .format(fileFormat)
      .save(s"gs://$bucketName/$filePath")
  }

}
