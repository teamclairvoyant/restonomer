package com.clairvoyant.restonomer.core.common

import com.dimafeng.testcontainers.GenericContainer
import com.dimafeng.testcontainers.scalatest.TestContainerForEach
import com.google.cloud.NoCredentials
import com.google.cloud.storage.StorageOptions.Builder
import com.google.cloud.storage.{BucketInfo, Storage, StorageOptions}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, Suite}
import org.testcontainers.containers.wait.strategy.Wait

trait GCSMockSpec extends TestContainerForEach {
  this: Suite =>

  val gcsMockPort: Int = 4443

  override val containerDef = GenericContainer.Def(
    dockerImage = "fsouza/fake-gcs-server:latest",
    exposedPorts = Seq(gcsMockPort)
  )

  val gcsStorageClientBuilder: Builder = StorageOptions
    .newBuilder()
    .setProjectId("test-project")
    .setCredentials(NoCredentials.getInstance())

}
