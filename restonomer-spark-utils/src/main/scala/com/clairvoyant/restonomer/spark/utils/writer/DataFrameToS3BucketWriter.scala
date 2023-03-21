package com.clairvoyant.restonomer.spark.utils.writer

import org.apache.spark.sql.{DataFrame, SaveMode}

class DataFrameToS3BucketWriter(
    bucketName: String,
    path: String,
    fileFormat: String,
    saveMode: SaveMode = SaveMode.ErrorIfExists,
    writeOptions: Map[String, String],
    numberOfPartitions: Option[Int] = None,
    partitionColumns: Seq[String]
) extends DataFrameWriter {

  override def write(dataFrame: DataFrame): Unit =
    writeOptions
      .foldLeft(
        numberOfPartitions
          .map(dataFrame.repartition)
          .getOrElse(dataFrame)
          .write
          .mode(saveMode)
      ) { case (writer, key -> value) =>
        writer.option(key, value)
      }
      .partitionBy(partitionColumns: _*)
      .format(fileFormat)
      .save(s"s3a://$bucketName/$path")

}
