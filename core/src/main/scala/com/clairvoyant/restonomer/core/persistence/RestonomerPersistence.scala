package com.clairvoyant.restonomer.core.persistence

import org.apache.spark.sql.DataFrame

sealed trait RestonomerPersistence {

  val format: String
  def persist(restonomerResponseDataFrame: DataFrame): Unit

}

case class LocalFileSystem(override val format: String, path: String) extends RestonomerPersistence {

  override def persist(restonomerResponseDataFrame: DataFrame): Unit = {
    restonomerResponseDataFrame.write.format(format).save(path)
  }

}
