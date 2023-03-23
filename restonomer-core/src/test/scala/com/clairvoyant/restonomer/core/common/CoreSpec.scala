package com.clairvoyant.restonomer.core.common

import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.client3._
import sttp.model.Method

import scala.concurrent.Future

trait CoreSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll with DataFrameMatchers with S3MockSpec {

  implicit val sttpBackend: SttpBackend[Future, Any] = HttpClientFutureBackend()

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

}
