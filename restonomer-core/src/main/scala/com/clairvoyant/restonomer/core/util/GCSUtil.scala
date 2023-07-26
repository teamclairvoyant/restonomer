package com.clairvoyant.restonomer.core.util

import com.google.cloud.storage.Storage.BlobListOption
import com.google.cloud.storage.StorageOptions.Builder
import com.google.cloud.storage.{Blob, Storage, StorageOptions}

import scala.jdk.CollectionConverters.*

object GCSUtil {

  val GCS_PREFIX = "gs://"

  def getBucketName(fullGCSPath: String): String = fullGCSPath.stripPrefix(GCS_PREFIX).split("/", 2)(0)

  def getBlobName(fullGCSPath: String): String = fullGCSPath.stripPrefix(GCS_PREFIX).split("/", 2)(1)

  def getBlobs(fullGCSPath: String)(using gcsStorageClient: Storage): List[Blob] =
    gcsStorageClient
      .list(getBucketName(fullGCSPath), BlobListOption.prefix(getBlobName(fullGCSPath)))
      .iterateAll()
      .asScala
      .toList

  def getBlobFullPath(blob: Blob): String = s"${GCS_PREFIX}${blob.getBucket}/${blob.getName}"

}
