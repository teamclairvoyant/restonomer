package com.clairvoyant.restonomer.config

import com.clairvoyant.restonomer.app.RestonomerContext
import com.clairvoyant.restonomer.util.GCSUtil.*
import com.google.cloud.storage.Storage.BlobListOption
import com.google.cloud.storage.{Blob, BlobId, Storage, StorageOptions}
import zio.Config

import java.io.{ByteArrayInputStream, File}
import scala.annotation.tailrec
import scala.io.Source

class GCSRestonomerContextLoader(using gcsStorageClient: Storage) extends RestonomerContextLoader {

  override def fileExists(filePath: String): Boolean =
    gcsStorageClient.get(BlobId.of(getBucketName(filePath), getBlobName(filePath))) != null

  override def readConfigFile(configFilePath: String): Source =
    Source.fromInputStream(
      new ByteArrayInputStream(
        gcsStorageClient
          .get(BlobId.of(getBucketName(configFilePath), getBlobName(configFilePath)))
          .getContent()
      )
    )

  override def loadConfigsFromDirectory[C](configDirectoryPath: String, config: Config[C])(
      using configVariablesSubstitutor: Option[ConfigVariablesSubstitutor]
  ): List[C] = {
    @tailrec
    def loadConfigsFromDirectoryHelper(remainingConfigFiles: List[Blob], configs: List[C]): List[C] =
      if (remainingConfigFiles.isEmpty)
        configs
      else {
        val configFile = remainingConfigFiles.head

        if (configFile.isDirectory)
          loadConfigsFromDirectoryHelper(getBlobs(getBlobFullPath(configFile)) ++ remainingConfigFiles.tail, configs)
        else
          loadConfigsFromDirectoryHelper(
            remainingConfigFiles.tail,
            loadConfigFromFile(getBlobFullPath(configFile), config) :: configs
          )
      }

    loadConfigsFromDirectoryHelper(getBlobs(configDirectoryPath), List())
  }

}
