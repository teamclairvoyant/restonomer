package com.clairvoyant.restonomer.core

import com.clairvoyant.restonomer.core.app.RestonomerContext
import com.clairvoyant.restonomer.core.model._
import com.clairvoyant.restonomer.core.persistence.FileSystem
import com.clairvoyant.restonomer.core.transformation.AddLiteralColumn

object RestonomerApp extends App {

  val checkpointConfig = CheckpointConfig(
    name = "test_checkpoint",
    data = DataConfig(
      dataRequest = RequestConfig(
        url = "http://ip.jsontest.com"
      ),
      dataResponse = DataResponseConfig(
        bodyFormat = "JSON",
        transformations = List(
          AddLiteralColumn(
            columnName = "test_column_1",
            columnValue = "test_value_1"
          )
        ),
        persistence = FileSystem(
          fileFormat = "JSON",
          filePath = "./rest-output-2/"
        )
      )
    )
  )

  val restonomerContextDirectoryPath = "./restonomer_context"
  val configVariablesFromApplicationArgs = Map("BASIC_AUTH_TOKEN" -> "abcxyz")

  private val restonomerContext = RestonomerContext(
    restonomerContextDirectoryPath = restonomerContextDirectoryPath,
    configVariablesFromApplicationArgs = configVariablesFromApplicationArgs
  )

  restonomerContext.runCheckpoint(checkpointFilePath = "checkpoint_no_authentication.conf")
  restonomerContext.runCheckpoint(checkpointConfig)
  restonomerContext.runCheckpointsUnderDirectory(checkpointsDirectoryPath = "category-1")
  // restonomerContext.runAllCheckpoints()

}
