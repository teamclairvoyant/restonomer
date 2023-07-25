package com.clairvoyant.restonomer.core.common

import com.google.cloud.NoCredentials
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Suite
import org.scalatest.BeforeAndAfter
import com.google.cloud.storage.BucketInfo

trait GCSMockSpec {
  val gcsMockPort: Int = 4443
  val gcsMockEndpoint: String = s"http://localhost:$gcsMockPort"

  given gcsStorageClient: Storage =
    StorageOptions
      .newBuilder()
      .setHost(gcsMockEndpoint)
      .setProjectId("test-project")
      .setCredentials(NoCredentials.getInstance())
      .build()
      .getService()

}
