package com.clairvoyant.restonomer.core.common

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import io.findify.s3mock.S3Mock

trait S3MockSpec {

  val mockAWSAccessKey = "test_access_key"
  val mockAWSSecretKey = "test_secret_key"

  val s3MockPort: Int = 8081
  val s3MockEndpoint: String = s"http://localhost:$s3MockPort"

  val mockS3BucketName = "test-bucket"

  val s3Mock: S3Mock = S3Mock(port = s3MockPort)

  lazy val s3Client: AmazonS3 =
    AmazonS3ClientBuilder
      .standard()
      .disableChunkedEncoding
      .withPathStyleAccessEnabled(true)
      .withEndpointConfiguration(new EndpointConfiguration(s3MockEndpoint, Regions.US_EAST_1.getName))
      .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(mockAWSAccessKey, mockAWSSecretKey)))
      .build

}
