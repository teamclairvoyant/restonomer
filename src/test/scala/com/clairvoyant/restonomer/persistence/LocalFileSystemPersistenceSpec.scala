package com.clairvoyant.restonomer.persistence

import com.clairvoyant.data.scalaxy.test.util.matchers.DataFrameMatcher
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader
import com.clairvoyant.data.scalaxy.writer.local.file.formats.*
import org.apache.commons.io.FileUtils
import org.scalatest.BeforeAndAfterEach

import java.io.File

class LocalFileSystemPersistenceSpec extends DataFrameReader with DataFrameMatcher with BeforeAndAfterEach {

  val restonomerResponseDF = readJSONFromText(
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
