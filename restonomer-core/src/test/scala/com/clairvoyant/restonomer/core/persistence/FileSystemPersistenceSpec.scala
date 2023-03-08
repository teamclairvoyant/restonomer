package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.writer.DataFrameToFileSystemWriter
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.DataFrame

import java.io.File

class FileSystemPersistenceSpec extends CoreSpec {

  import sparkSession.implicits._

  val restonomerResponseDF: DataFrame = Seq(("val_A", "val_B", "val_C", "val_D"))
    .toDF("col_A", "col_B", "col_C", "col_D")

  lazy val dataFrameToFileSystemWriterOutputDirPath = s"out_${System.currentTimeMillis()}"

  "persist() - with proper format and path" should
    "save the dataframe to the file in the desired format at the desired path" in {
      val fileSystemPersistence = FileSystem(
        fileFormat = "JSON",
        filePath = dataFrameToFileSystemWriterOutputDirPath
      )

      fileSystemPersistence.persist(
        restonomerResponseDF,
        new DataFrameToFileSystemWriter(
          sparkSession = sparkSession,
          fileFormat = fileSystemPersistence.fileFormat,
          filePath = fileSystemPersistence.filePath
        )
      )

      sparkSession.read.json(dataFrameToFileSystemWriterOutputDirPath) should matchExpectedDataFrame(
        restonomerResponseDF
      )
    }

  override def afterAll(): Unit = FileUtils.deleteDirectory(new File(dataFrameToFileSystemWriterOutputDirPath))

}
