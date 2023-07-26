package com.clairvoyant.restonomer.core.util

import com.clairvoyant.restonomer.core.common.{CoreSpec, GCSMockSpec}
import com.clairvoyant.restonomer.core.util.GCSUtil.*
import com.dimafeng.testcontainers.GenericContainer.Def
import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import com.google.cloud.storage.{BlobId, BucketInfo, Storage, StorageOptions}
import org.scalatest.flatspec.AnyFlatSpec
import org.testcontainers.containers.wait.strategy.Wait

import java.net.URL
import scala.io.Source

class GCSUtilSpec extends CoreSpec with GCSMockSpec {

  "getBucketName() - with fullGCSPath" should "return correct bucket name" in {
    getBucketName(mockFullGCSPath) shouldBe mockGCSBucketName
  }

  "getBlobName() - with fullGCSPath" should "return correct blob name" in {
    getBlobName(mockFullGCSPath) shouldBe mockBlobName
  }

  "getBlobs() - with fullGCSPath" should "return list of blobs" in {
    getBlobs(s"$mockFullGCSPath/authentication/api_key_authentication") should have size 3
  }

  "getBlobFullPath()" should "return full path of blob" in {
    getBlobFullPath(
      blob = gcsStorageClient.get(
        BlobId.of(
          gcsBucket.getName(),
          s"$mockBlobName/authentication/api_key_authentication/checkpoint_api_key_authentication_cookie.conf"
        )
      )
    ) shouldBe s"$mockFullGCSPath/authentication/api_key_authentication/checkpoint_api_key_authentication_cookie.conf"
  }

}
