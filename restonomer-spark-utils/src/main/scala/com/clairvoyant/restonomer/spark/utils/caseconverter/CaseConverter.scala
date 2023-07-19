package com.clairvoyant.restonomer.spark.utils.caseconverter

import org.apache.spark.sql.DataFrame

trait CaseConverter {
  def convert(inputString: String, sourceCaseType: String): String
}
