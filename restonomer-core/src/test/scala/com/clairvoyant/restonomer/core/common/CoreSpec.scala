package com.clairvoyant.restonomer.core.common

import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import org.apache.spark.sql.SparkSession
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import sttp.client3.*
import sttp.model.Method

import scala.concurrent.Future

trait CoreSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll with DataFrameMatchers {

  given sparkSession: SparkSession =
    SparkSession
      .builder()
      .master("local[*]")
      .getOrCreate()

  val resourcesPath = "restonomer-core/src/test/resources"
  val url = "/test_url"
  val uri = s"http://localhost:8080$url"

  val basicHttpRequest: Request[Either[String, String], Any] = basicRequest.method(
    method = Method.GET,
    uri = uri"$uri"
  )

}
