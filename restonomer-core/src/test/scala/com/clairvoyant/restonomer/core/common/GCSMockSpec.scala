package com.clairvoyant.restonomer.core.common

import com.google.cloud.NoCredentials
import com.google.cloud.storage.{BucketInfo, Storage, StorageOptions}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, Suite}
import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import com.dimafeng.testcontainers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait

trait GCSMockSpec extends TestContainerForAll {
  this: Suite =>

  val gcsMockPort: Int = 4443
  val gcsMockEndpoint: String = s"http://0.0.0.0:$gcsMockPort"

  given gcsStorageClient: Storage =
    StorageOptions
      .newBuilder()
      .setHost(gcsMockEndpoint)
      .setProjectId("test-project")
      .setCredentials(NoCredentials.getInstance())
      .build()
      .getService()

  override val containerDef = GenericContainer.Def(
    "fsouza/fake-gcs-server:latest",
    exposedPorts = Seq(gcsMockPort)
  )

}
