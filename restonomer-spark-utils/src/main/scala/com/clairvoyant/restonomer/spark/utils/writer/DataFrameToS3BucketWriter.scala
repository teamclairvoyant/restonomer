package com.clairvoyant.restonomer.spark.utils.writer

import org.apache.spark.sql.{DataFrame, SaveMode}

class DataFrameToS3BucketWriter(
    bucketName: String,
    fileFormat: String,
    filePath: String,
    saveMode: SaveMode
) extends DataFrameWriter {

  override def write(dataFrame: DataFrame): Unit = {
    dataFrame.write
      .mode(saveMode)
      .format(fileFormat)
      .save(s"s3a://$bucketName/$filePath")
  }

}
