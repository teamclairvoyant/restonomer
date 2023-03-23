package com.clairvoyant.restonomer.core.common

import com.amazonaws.auth.{AWSStaticCredentialsProvider, AnonymousAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
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

  val sparkConf = new SparkConf()
  sparkConf.set("spark.hadoop.fs.s3a.access.key", "test")
  sparkConf.set("spark.hadoop.fs.s3a.secret.key", "test")

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

  val s3Mock: S3Mock = S3Mock(port = 8081)

  val awsClientEndpoint = new EndpointConfiguration("http://localhost:8081", "us-west-2")

  lazy val s3Client: AmazonS3 =
    AmazonS3ClientBuilder
      .standard()
      .withPathStyleAccessEnabled(true)
      .withEndpointConfiguration(awsClientEndpoint)
      .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
      .build

}
