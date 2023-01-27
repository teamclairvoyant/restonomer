package com.clairvoyant.restonomer.core.pagination

import com.clairvoyant.restonomer.core.common.CoreSpec

class PageNumberBasedPaginationSpec extends CoreSpec {

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

    val pagination = PageNumberBasedPagination(
      totalNumberOfRecordsAttribute = "$.data.total.numberItems",
      currentPageNumberAttribute = "$.data.page",
      maxRecordsPerPage = 1,
      pageTokenName = "page"
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

    val pagination = PageNumberBasedPagination(
      totalNumberOfRecordsAttribute = "$.data.total.numberItems",
      currentPageNumberAttribute = "$.data.page",
      maxRecordsPerPage = 1,
      pageTokenName = "page"
    )

    pagination.getNextPageToken(responseBody) shouldBe None
  }

}
