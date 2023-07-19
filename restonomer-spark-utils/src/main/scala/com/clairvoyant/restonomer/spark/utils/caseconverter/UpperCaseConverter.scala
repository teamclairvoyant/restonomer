package com.clairvoyant.restonomer.spark.utils.caseconverter

class UpperCaseConverter extends CaseConverter {

  def convert(inputString: String, sourceCaseType: String = "lower"): String = inputString.toUpperCase()

}
