package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.DataFrame
import org.scalatest.BeforeAndAfterEach

import java.io.File
import com.clairvoyant.data.scalaxy.writer.local.file.formats.*
import com.clairvoyant.data.scalaxy.writer.local.file.instances.*

class LocalFileSystemPersistenceSpec extends DataScalaxyTestUtil with BeforeAndAfterEach {

  val restonomerResponseDF = readCSVFromText(
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

  "persist() - with CSV format and path" should
    "save the dataframe to the file in the desired format at the desired path" in {
      val localFileSystemPersistence = LocalFileSystem(
        fileFormat = CSVFileFormat(),
        filePath = dataFrameToFileSystemWriterOutputDirPath
      )

      localFileSystemPersistence.persist(restonomerResponseDF)

      val actualDF = readCSVFromFile(dataFrameToFileSystemWriterOutputDirPath)

      actualDF should matchExpectedDataFrame(restonomerResponseDF)
    }

  "persist() - with JSON format and path" should
    "save the dataframe to the file in the desired format at the desired path" in {
      val localFileSystemPersistence = LocalFileSystem(
        fileFormat = JSONFileFormat(),
        filePath = dataFrameToFileSystemWriterOutputDirPath
      )

      localFileSystemPersistence.persist(restonomerResponseDF)

      val actualDF = readJSONFromFile(dataFrameToFileSystemWriterOutputDirPath)

      actualDF should matchExpectedDataFrame(restonomerResponseDF)
    }

  "persist() - with XML format and path" should
    "save the dataframe to the file in the desired format at the desired path" in {
      val localFileSystemPersistence = LocalFileSystem(
        fileFormat = XMLFileFormat(),
        filePath = dataFrameToFileSystemWriterOutputDirPath
      )

      localFileSystemPersistence.persist(restonomerResponseDF)

      val actualDF = readXMLFromFile(dataFrameToFileSystemWriterOutputDirPath)

      actualDF should matchExpectedDataFrame(restonomerResponseDF)
    }

  "persist() - with Parquet format and path" should
    "save the dataframe to the file in the desired format at the desired path" in {
      val localFileSystemPersistence = LocalFileSystem(
        fileFormat = ParquetFileFormat(),
        filePath = dataFrameToFileSystemWriterOutputDirPath
      )

      localFileSystemPersistence.persist(restonomerResponseDF)

      val actualDF = readParquet(dataFrameToFileSystemWriterOutputDirPath)

      actualDF should matchExpectedDataFrame(restonomerResponseDF)
    }

  override def afterEach(): Unit = FileUtils.deleteDirectory(new File(dataFrameToFileSystemWriterOutputDirPath))

}
