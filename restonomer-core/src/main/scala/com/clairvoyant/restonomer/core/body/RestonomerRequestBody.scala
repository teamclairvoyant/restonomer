package com.clairvoyant.restonomer.core.body

sealed trait RestonomerRequestBody

case class TextDataBody(data: String) extends RestonomerRequestBody

case class FormDataBody(data: Map[String, String]) extends RestonomerRequestBody
