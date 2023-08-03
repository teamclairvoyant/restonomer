package com.clairvoyant.restonomer.core.pagination

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil

class CursorBasedPaginationSpec extends DataScalaxyTestUtil {

  "getNextPageToken()" should "return the token for next page" in {
    val responseBody =
      """
        |{
        |    "current": 111111,
        |    "next": 222222,
        |    "data": [
        |      {
        |        "id": 3296105,
        |        "date": "2021-05-01T23:34:21+02:00",
        |        "status": "Paid to affiliate",
        |        "orderValue": 623.21
        |      }
        |    ]
        |}
        |""".stripMargin

    val pagination = CursorBasedPagination(nextCursorAttribute = "$.next")

    pagination.getNextPageToken(responseBody) shouldBe Some("cursor" -> "222222")
  }

  "getNextPageToken()" should "not return the token for next page" in {
    val responseBody =
      """
        |{
        |    "current": 111111,
        |    "next": null,
        |    "data": [
        |      {
        |        "id": 3296105,
        |        "date": "2021-05-01T23:34:21+02:00",
        |        "status": "Paid to affiliate",
        |        "orderValue": 623.21
        |      }
        |    ]
        |}
        |""".stripMargin

    val pagination = CursorBasedPagination(nextCursorAttribute = "$.next")

    pagination.getNextPageToken(responseBody) shouldBe None
  }

}
