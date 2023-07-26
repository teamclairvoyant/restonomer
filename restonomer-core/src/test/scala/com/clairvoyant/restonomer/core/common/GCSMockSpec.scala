package com.clairvoyant.restonomer.core.common

import com.dimafeng.testcontainers.GenericContainer
import com.dimafeng.testcontainers.GenericContainer.Def
import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import com.google.cloud.NoCredentials
import com.google.cloud.storage.StorageOptions
import com.google.cloud.storage.StorageOptions.Builder
import org.scalatest.Suite
import org.scalatest.flatspec.AnyFlatSpec
import org.testcontainers.containers.wait.strategy.Wait

import java.net.URL
import scala.io.Source

trait GCSMockSpec extends TestContainerForAll {
  this: Suite =>

  override val containerDef: Def[GenericContainer] = GenericContainer.Def(
    dockerImage = "fsouza/fake-gcs-server:latest",
    exposedPorts = Seq(4443),
    command = Seq("-scheme=http")
  )

  val gcsStorageClientBuilder: Builder = StorageOptions
    .newBuilder()
    .setProjectId("test-project")
    .setCredentials(NoCredentials.getInstance())

}
