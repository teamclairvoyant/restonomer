package com.clairvoyant.restonomer.pagination

import com.clairvoyant.restonomer.common.CoreSpec

class PageNumberWithTotalPagesBasedPaginationSpec extends CoreSpec {

  "getNextPageToken()" should "return the token for next page" in {
    val responseBody =
      """
        |{
        |  "data": {
        |    "total": {
        |      "numberPages": 3
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

    val pagination = PageNumberWithTotalPagesBasedPagination(
      totalNumberOfPagesAttribute = "$.data.total.numberPages",
      currentPageNumberAttribute = "$.data.page"
    )

    pagination.getNextPageToken(responseBody) shouldBe Some("page" -> "2")
  }

  "getNextPageToken()" should "not return the token for next page" in {
    val responseBody =
      """
        |{
        |  "data": {
        |    "total": {
        |      "numberPages": 3
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

    val pagination = PageNumberWithTotalPagesBasedPagination(
      totalNumberOfPagesAttribute = "$.data.total.numberPages",
      currentPageNumberAttribute = "$.data.page"
    )

    pagination.getNextPageToken(responseBody) shouldBe None
  }

}
