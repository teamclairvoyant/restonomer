package com.clairvoyant.restonomer.pagination

import com.clairvoyant.restonomer.common.CoreSpec

class OffsetBasedPaginationSpec extends CoreSpec {

  "getNextPageToken()" should "return the token for next page when next offset is less than total count" in {
    val responseBody =
      """
        |{
        |    "offset": 6,
        |    "limit": 3,
        |    "count": 11,
        |    "data": [
        |      {
        |        "col_A": "val_A",
        |        "col_B": "val_B",
        |        "col_C": "val_C"
        |      }
        |    ]
        |}
        |""".stripMargin

    val pagination = OffsetBasedPagination(
      offsetAttribute = "$.offset",
      limitAttribute = "$.limit",
      totalNumberOfRecordsAttribute = "$.count"
    )

    pagination.getNextPageToken(responseBody) shouldBe Some("offset" -> "9")
  }

  "getNextPageToken()" should "not return the token for next page when next offset is equal to total count" in {
    val responseBody =
      """
        |{
        |    "offset": 9,
        |    "limit": 3,
        |    "count": 12,
        |    "data": [
        |      {
        |        "col_A": "val_A",
        |        "col_B": "val_B",
        |        "col_C": "val_C"
        |      }
        |    ]
        |}
        |""".stripMargin

    val pagination = OffsetBasedPagination(
      offsetAttribute = "$.offset",
      limitAttribute = "$.limit",
      totalNumberOfRecordsAttribute = "$.count"
    )

    pagination.getNextPageToken(responseBody) shouldBe None
  }

  "getNextPageToken()" should "not return the token for next page when next offset is greater than total count" in {
    val responseBody =
      """
        |{
        |    "offset": 9,
        |    "limit": 3,
        |    "count": 11,
        |    "data": [
        |      {
        |        "col_A": "val_A",
        |        "col_B": "val_B",
        |        "col_C": "val_C"
        |      }
        |    ]
        |}
        |""".stripMargin

    val pagination = OffsetBasedPagination(
      offsetAttribute = "$.offset",
      limitAttribute = "$.limit",
      totalNumberOfRecordsAttribute = "$.count"
    )

    pagination.getNextPageToken(responseBody) shouldBe None
  }

}
