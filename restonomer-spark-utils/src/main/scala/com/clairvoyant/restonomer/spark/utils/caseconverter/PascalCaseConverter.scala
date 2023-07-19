package com.clairvoyant.restonomer.spark.utils.caseconverter

class PascalCaseConverter extends CaseConverter {

  def convert(inputString: String, sourceCaseType: String): String = {

    sourceCaseType.toLowerCase() match {
      case "snake" =>
        snakeToPascal(inputString)
      case "camel" =>
        camelToPascal(inputString)
      case "kebab" =>
        kebabToPascal(inputString)
      case _ =>
        throw new Exception(s"Pascal-case conversion only supported for source case types : Snake/Camel/Kebab..")
    }
  }

  private def snakeToPascal(snakeCase: String): String = {
    snakeCase.toLowerCase.split("_").map(_.capitalize).mkString
  }

  private def camelToPascal(camelCase: String): String = {
    camelCase.head.toUpper + camelCase.tail
  }

  private def kebabToPascal(kebabCase: String): String = {
    kebabCase.toLowerCase.split("-").map(_.capitalize).mkString
  }

}
