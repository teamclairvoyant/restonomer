package com.clairvoyant.restonomer.spark.utils.caseconverter

class LowerCaseConverter extends CaseConverter {

  def convert(inputString: String, sourceCaseType: String = "upper"): String = inputString.toLowerCase()

}
