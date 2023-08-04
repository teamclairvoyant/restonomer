package com.clairvoyant.restonomer.core.pagination

import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil

class PageNumberWithTotalRecordsBasedPaginationSpec extends DataScalaxyTestUtil {

  "getNextPageToken()" should "return the token for next page" in {
    val responseBody =
      """
        |{
        |  "data": {
        |    "total": {
        |      "numberItems": 3
        |    },
        |    "page": 1,
        |    "items": [
        |      {
        |        "col_A": "val_1",
        |        "col_B": "val_2",
        |        "col_C": "val_3"
        |      }
        |    ]
        |  }
        |}
        |
        |""".stripMargin

    val pagination = PageNumberWithTotalRecordsBasedPagination(
      totalNumberOfRecordsAttribute = "$.data.total.numberItems",
      currentPageNumberAttribute = "$.data.page",
      maxRecordsPerPage = 1
    )

    pagination.getNextPageToken(responseBody) shouldBe Some("page" -> "2")
  }

  "getNextPageToken()" should "not return the token for next page" in {
    val responseBody =
      """
        |{
        |  "data": {
        |    "total": {
        |      "numberItems": 3
        |    },
        |    "page": 3,
        |    "items": [
        |      {
        |        "col_A": "val_1",
        |        "col_B": "val_2",
        |        "col_C": "val_3"
        |      }
        |    ]
        |  }
        |}
        |
        |""".stripMargin

    val pagination = PageNumberWithTotalRecordsBasedPagination(
      totalNumberOfRecordsAttribute = "$.data.total.numberItems",
      currentPageNumberAttribute = "$.data.page",
      maxRecordsPerPage = 1
    )

    pagination.getNextPageToken(responseBody) shouldBe None
  }

}
