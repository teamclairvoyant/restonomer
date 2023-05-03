package com.clairvoyant.restonomer.core.pagination

import com.jayway.jsonpath.JsonPath
import zio.config.derivation.nameWithLabel

@nameWithLabel
sealed trait RestonomerPagination {
  def getNextPageToken(responseBody: String): Option[(String, String)]
}

case class PageNumberWithTotalRecordsBasedPagination(
    totalNumberOfRecordsAttribute: String,
    currentPageNumberAttribute: String,
    maxRecordsPerPage: Int,
    pageTokenName: String
) extends RestonomerPagination {

  override def getNextPageToken(responseBody: String): Option[(String, String)] = {
    val totalNumberOfRecords = JsonPath.read[Int](responseBody, totalNumberOfRecordsAttribute)
    val currentPageNumber = JsonPath.read[Int](responseBody, currentPageNumberAttribute)

    if (totalNumberOfRecords > currentPageNumber * maxRecordsPerPage)
      Some(pageTokenName -> (currentPageNumber + 1).toString)
    else
      None
  }

}
