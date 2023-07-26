package com.clairvoyant.restonomer.core.common

import com.dimafeng.testcontainers.GenericContainer
import com.dimafeng.testcontainers.GenericContainer.Def
import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import com.google.cloud.NoCredentials
import com.google.cloud.storage.StorageOptions.Builder
import com.google.cloud.storage.{BucketInfo, Storage, StorageOptions}
import org.scalatest.Suite
import org.scalatest.flatspec.AnyFlatSpec
import org.testcontainers.containers.wait.strategy.Wait

import java.net.URL
import scala.io.Source

trait GCSMockSpec extends TestContainerForAll {
  this: Suite =>

  val gcsPrefix = "gs://"
  val mockGCSBucketName = "test-bucket"
  val mockBlobName = "dirA/dirB/dirC"
  val mockFullGCSPath = s"$gcsPrefix$mockGCSBucketName/$mockBlobName"
  val mockGCSPort = 4443

  override val containerDef: Def[GenericContainer] = GenericContainer.Def(
    dockerImage = "fsouza/fake-gcs-server:latest",
    exposedPorts = Seq(mockGCSPort),
    command = Seq("-scheme=http")
  )

  implicit lazy val gcsStorageClient: Storage = withContainers { container =>
    StorageOptions
      .newBuilder()
      .setProjectId("test-project")
      .setCredentials(NoCredentials.getInstance())
      .setHost(s"http://${container.containerIpAddress}:${container.mappedPort(mockGCSPort)}")
      .build()
      .getService
  }

  lazy val gcsBucket = gcsStorageClient.create(BucketInfo.of(mockGCSBucketName))

}
