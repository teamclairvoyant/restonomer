package com.clairvoyant.restonomer.core.pagination

import com.jayway.jsonpath.JsonPath

sealed trait RestonomerPagination {

  def getNextPageToken(responseBody: String): Option[(String, String)]

}

case class PaginationMechanismA(
    totalNumberOfRecordsAttribute: String,
    maxRecordsPerPage: Long,
    currentPageNumberAttribute: String,
    pageTokenName: String
) extends RestonomerPagination {

  override def getNextPageToken(responseBody: String): Option[(String, String)] = {
    val totalNumberOfRecords = JsonPath.read[Long](responseBody, totalNumberOfRecordsAttribute)
    val currentPageNumber = JsonPath.read[Long](responseBody, currentPageNumberAttribute)

    if (totalNumberOfRecords > currentPageNumber * maxRecordsPerPage) {
      val nextPageNumber = currentPageNumber + 1
      Some(pageTokenName -> nextPageNumber.toString)
    } else
      None
  }

}
