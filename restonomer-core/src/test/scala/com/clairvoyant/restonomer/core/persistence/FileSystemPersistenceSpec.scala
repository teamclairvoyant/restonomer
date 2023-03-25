package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.writer.DataFrameToFileSystemWriter
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.{DataFrame, SaveMode}

import java.io.File

class FileSystemPersistenceSpec extends CoreSpec {

  import sparkSession.implicits._

  lazy val dataFrameToFileSystemWriterOutputDirPath = s"out_${System.currentTimeMillis()}"

  val restonomerResponseDF: DataFrame = Seq(("val_A", "val_B", "val_C", "val_D"))
    .toDF("col_A", "col_B", "col_C", "col_D")

  "persist() - with proper format and path" should
    "save the dataframe to the file in the desired format at the desired path" in {
      val fileSystemPersistence = FileSystem(
        fileFormat = "JSON",
        filePath = dataFrameToFileSystemWriterOutputDirPath
      )

      fileSystemPersistence.persist(
        restonomerResponseDF,
        new DataFrameToFileSystemWriter(
          fileFormat = fileSystemPersistence.fileFormat,
          filePath = fileSystemPersistence.filePath,
          saveMode = SaveMode.ErrorIfExists
        )
      )

      sparkSession.read.json(dataFrameToFileSystemWriterOutputDirPath) should matchExpectedDataFrame(
        restonomerResponseDF
      )
    }

  override def afterAll(): Unit = FileUtils.deleteDirectory(new File(dataFrameToFileSystemWriterOutputDirPath))

}
