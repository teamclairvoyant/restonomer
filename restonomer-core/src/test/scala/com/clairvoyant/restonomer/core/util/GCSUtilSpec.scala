package com.clairvoyant.restonomer.core.util

import com.clairvoyant.restonomer.core.common.{CoreSpec, GCSMockSpec}
import com.clairvoyant.restonomer.core.util.GCSUtil.*
import com.google.cloud.storage.{BucketInfo, StorageOptions}

class GCSUtilSpec extends CoreSpec with GCSMockSpec {

  
      .build()
      .getService()

  "getBucketName() - with fullGCSPath" should "return correct bucket name" in {
    getBucketName(fullGCSPath = "gs://test-bucket/test-blob") shouldBe "test-bucket"
  }

  "getBlobName() - with fullGCSPath" should "return correct blob name" in {
    getBlobName(fullGCSPath = "gs://test-bucket/test-blob/abc/def") shouldBe "test-blob/abc/def"
  }

  "getBlobs() - with fullGCSPath" should "return list of blobs" in {
    withContainers { container =>
      val gcsBucket = gcsStorageClient
            .setHost(s"${container.}")
            .create(BucketInfo.of("test-bucket-1"))

      gcsBucket.create("test-blob/file-1.txt", "file-1 content".getBytes())
      gcsBucket.create("test-blob/file-2.txt", "file-2 content".getBytes())
      gcsBucket.create("test-blob/file-3.txt", "file-3 content".getBytes())

      getBlobs(fullGCSPath = "gs://test-bucket-1/test-blob") should have size 3
    }

  }

  "getBlobFullPath()" should "return full path of blob" in {
    val gcsBucket = gcsStorageClient.create(BucketInfo.of("test-bucket-2"))
    val blob = gcsBucket.create("test-blob/file-1.txt", "file-1 content".getBytes())

    getBlobFullPath(blob) shouldBe "gs://test-bucket-2/test-blob/file-1.txt"
  }

}
