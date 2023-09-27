package com.clairvoyant.restonomer.pagination

import com.clairvoyant.restonomer.common.CoreSpec

class NextPageURLBasedPaginationSpec extends CoreSpec {

  "getNextPageToken()" should "return the token for next page" in {
    val responseBody =
      """
        |{
        |    "data": [
        |      {
        |        "id": 3296105,
        |        "date": "2021-05-01T23:34:21+02:00",
        |        "status": "Paid to affiliate",
        |        "orderValue": 623.21
        |      }
        |    ],
        |    "next": "/v1/affiliates/1",
        |}
        |""".stripMargin

    val pagination = NextPageURLBasedPagination(nextURLAttribute = "$.next")

    pagination.getNextPageToken(responseBody) shouldBe Some("nextURL" -> "/v1/affiliates/1")
  }

  "getNextPageToken()" should "not return the token for next page" in {
    val responseBody =
      """
        |{
        |    "data": [
        |      {
        |        "id": 3296105,
        |        "date": "2021-05-01T23:34:21+02:00",
        |        "status": "Paid to affiliate",
        |        "orderValue": 623.21
        |      }
        |    ],
        |    "next": null,
        |}
        |""".stripMargin

    val pagination = NextPageURLBasedPagination(nextURLAttribute = "$.next")

    pagination.getNextPageToken(responseBody) shouldBe None
  }

}
