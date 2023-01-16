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
    val currentPageNumber = JsonPath.read[Long](responseBody, currentPageNumberAttribute)

    if (JsonPath.read[Long](responseBody, totalNumberOfRecordsAttribute) > currentPageNumber * maxRecordsPerPage)
      Some(pageTokenName -> (currentPageNumber + 1).toString)
    else
      None
  }

}
