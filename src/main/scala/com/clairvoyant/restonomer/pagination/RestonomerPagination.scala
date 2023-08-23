package com.clairvoyant.restonomer.pagination

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
    pageTokenName: String = "page"
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

case class PageNumberWithTotalPagesBasedPagination(
    totalNumberOfPagesAttribute: String,
    currentPageNumberAttribute: String,
    pageTokenName: String = "page"
) extends RestonomerPagination {

  override def getNextPageToken(responseBody: String): Option[(String, String)] = {
    val totalNumberOfPages = JsonPath.read[Int](responseBody, totalNumberOfPagesAttribute)
    val currentPageNumber = JsonPath.read[Int](responseBody, currentPageNumberAttribute)

    if (currentPageNumber != totalNumberOfPages)
      Some(pageTokenName -> (currentPageNumber + 1).toString)
    else
      None
  }

}

case class CursorBasedPagination(
    nextCursorAttribute: String,
    cursorTokenName: String = "cursor"
) extends RestonomerPagination {

  override def getNextPageToken(responseBody: String): Option[(String, String)] =
    Option(JsonPath.read[Any](responseBody, nextCursorAttribute)) match
      case Some(value) => Some(cursorTokenName -> value.toString())
      case None        => None

}

case class OffsetBasedPagination(
    offsetAttribute: String,
    limitAttribute: String,
    totalNumberOfRecordsAttribute: String,
    offsetTokenName: String = "offset"
) extends RestonomerPagination {

  override def getNextPageToken(responseBody: String): Option[(String, String)] = {
    val offset = JsonPath.read[Int](responseBody, offsetAttribute)
    val limit = JsonPath.read[Int](responseBody, limitAttribute)
    val totalNumberOfRecords = JsonPath.read[Int](responseBody, totalNumberOfRecordsAttribute)

    val nextOffset = offset + limit

    if (nextOffset < totalNumberOfRecords)
      Some(offsetTokenName -> nextOffset.toString)
    else
      None
  }

}
