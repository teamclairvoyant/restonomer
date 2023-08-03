package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil
import com.clairvoyant.restonomer.spark.utils.writer.DataFrameToFileSystemWriter
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.DataFrame
import org.scalatest.BeforeAndAfterAll

import java.io.File

class FileSystemPersistenceSpec extends DataScalaxyTestUtil with BeforeAndAfterAll {

  val restonomerResponseDF = readJSON(
    """
      |{
      |  "col_A": "val_A",
      |  "col_B": "val_B",
      |  "col_C": "val_C",
      |  "col_D": "val_D"
      |}
      |""".stripMargin
  )

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
          fileFormat = fileSystemPersistence.fileFormat,
          filePath = fileSystemPersistence.filePath,
          saveMode = fileSystemPersistence.saveMode
        )
      )

      sparkSession.read.json(dataFrameToFileSystemWriterOutputDirPath) should matchExpectedDataFrame(
        restonomerResponseDF
      )
    }

  override def afterAll(): Unit = FileUtils.deleteDirectory(new File(dataFrameToFileSystemWriterOutputDirPath))

}
