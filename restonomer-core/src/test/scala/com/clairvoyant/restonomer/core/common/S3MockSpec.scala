package com.clairvoyant.restonomer.core.common

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.clairvoyant.restonomer.core.common.S3MockSpec._
import io.findify.s3mock.S3Mock
import org.scalatest.{BeforeAndAfterAll, Suite}

object S3MockSpec {
  val s3MockAWSAccessKey = "test_access_key"
  val s3MockAWSSecretKey = "test_secret_key"
  val s3MockBucketName = "test-bucket"
  val s3MockPort: Int = 8081
  val s3MockEndpoint: String = s"http://localhost:$s3MockPort"
}

trait S3MockSpec extends BeforeAndAfterAll {
  this: Suite =>

  val s3Mock: S3Mock = S3Mock(port = s3MockPort)

  lazy val s3Client: AmazonS3 =
    AmazonS3ClientBuilder
      .standard()
      .disableChunkedEncoding
      .withPathStyleAccessEnabled(true)
      .withEndpointConfiguration(new EndpointConfiguration(s3MockEndpoint, Regions.US_EAST_1.getName))
      .withCredentials(
        new AWSStaticCredentialsProvider(new BasicAWSCredentials(s3MockAWSAccessKey, s3MockAWSSecretKey))
      )
      .build

  override def beforeAll(): Unit = {
    s3Mock.start
    s3Client.createBucket(s3MockBucketName)
  }

  override def afterAll(): Unit = s3Mock.shutdown
}
