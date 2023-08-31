package com.clairvoyant.restonomer.body

import zio.config.derivation.nameWithLabel

@nameWithLabel
sealed trait RestonomerRequestBody

case class TextDataBody(data: String) extends RestonomerRequestBody

case class FormDataBody(data: Map[String, String]) extends RestonomerRequestBody

case class JSONDataBody(data: String) extends RestonomerRequestBody
