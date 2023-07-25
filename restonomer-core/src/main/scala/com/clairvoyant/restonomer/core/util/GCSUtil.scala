package com.clairvoyant.restonomer.core.util

import com.google.cloud.storage.StorageOptions
import com.google.cloud.storage.Storage
import com.google.cloud.storage.Storage.BlobListOption
import com.google.cloud.storage.Blob
import collection.convert.ImplicitConversions.*

object GCSUtil {

  val GCS_PREFIX = "gs://"

  def getBucketName(fullGCSPath: String): String = fullGCSPath.stripPrefix(GCS_PREFIX).split("/", 2)(0)

  def getBlobName(fullGCSPath: String): String = fullGCSPath.stripPrefix(GCS_PREFIX).split("/", 2)(1)

  def getBlobs(fullGCSPath: String)(using gcsStorageClient: Storage): List[Blob] =
    gcsStorageClient
      .list(getBucketName(fullGCSPath), BlobListOption.prefix(getBlobName(fullGCSPath)))
      .iterateAll()
      .toList

  def getBlobFullPath(blob: Blob): String = s"${GCS_PREFIX}${blob.getBucket}/${blob.getName}"

}
