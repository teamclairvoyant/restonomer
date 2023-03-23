package com.clairvoyant.restonomer.core.common

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import io.findify.s3mock.S3Mock
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.client3._
import sttp.model.Method

import scala.concurrent.Future

trait CoreSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll with DataFrameMatchers {

  implicit val sttpBackend: SttpBackend[Future, Any] = HttpClientFutureBackend()

  val mockAWSAccessKey = "test_access_key"
  val mockAWSSecretKey = "test_secret_key"

  val s3MockPort: Int = 8081
  val s3MockEndpoint: String = s"http://localhost:$s3MockPort"

  val sparkConf: SparkConf = new SparkConf()
    .set("spark.hadoop.fs.s3a.endpoint", s3MockEndpoint)
    .set("spark.hadoop.fs.s3a.path.style.access", "true")
    .set("spark.hadoop.fs.s3a.access.key", mockAWSAccessKey)
    .set("spark.hadoop.fs.s3a.secret.key", mockAWSSecretKey)
    .set("spark.hadoop.fs.s3a.change.detection.version.required", "false")

  implicit lazy val sparkSession: SparkSession = SparkSession
    .builder()
    .master("local[*]")
    .config(sparkConf)
    .getOrCreate()

  val resourcesPath = "restonomer-core/src/test/resources"
  val url = "/test_url"
  val uri = s"http://localhost:8080$url"

  val basicHttpRequest: Request[Either[String, String], Any] = basicRequest.method(
    method = Method.GET,
    uri = uri"$uri"
  )

  lazy val mockedHttpServer: WireMockServer =
    new WireMockServer(
      wireMockConfig()
        .port(8080)
    )

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
