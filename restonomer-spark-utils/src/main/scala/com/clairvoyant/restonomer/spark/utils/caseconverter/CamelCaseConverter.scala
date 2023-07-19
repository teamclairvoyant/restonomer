package com.clairvoyant.restonomer.spark.utils.caseconverter

class CamelCaseConverter extends CaseConverter {

  def convert(inputString: String, sourceCaseType: String): String = {

    sourceCaseType.toLowerCase() match {
      case "snake" =>
        snakeToCamel(inputString)
      case "pascal" =>
        pascalToCamel(inputString)
      case "kebab" =>
        kebabToCamel(inputString)
      case _ =>
        throw new Exception(s"Camel-case conversion only supported for source case types : Snake/Pascal/Kebab..")
    }
  }

  private def snakeToCamel(snakeCase: String): String = {
    val words = snakeCase.split("_")
    words.headOption.getOrElse("").toLowerCase + words.tail.map(_.capitalize).mkString
  }

  private def pascalToCamel(pascalCase: String): String = {
    pascalCase.head.toLower + pascalCase.tail
  }

  private def kebabToCamel(kebabCase: String): String = {
    val words = kebabCase.split("-")
    words.headOption.getOrElse("").toLowerCase + words.tail.map(_.capitalize).mkString
  }

}
