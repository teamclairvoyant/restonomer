package com.clairvoyant.restonomer.common

import org.apache.commons.io.FileUtils
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.{BeforeAndAfterEach, Suite}

import java.io.File

trait MockFileSystemPersistence extends BeforeAndAfterEach {
  this: Suite =>

  val mappingsDirectory: String

  lazy val outputPath = s"/tmp/$mappingsDirectory"

  def outputDF(implicit sparkSession: SparkSession): DataFrame = sparkSession.read.json(outputPath)

  override def afterEach: Unit = FileUtils.deleteDirectory(new File(outputPath))

}
