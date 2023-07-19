package com.clairvoyant.restonomer.spark.utils.caseconverter

class SnakeCaseConverter extends CaseConverter {

  def convert(inputString: String, sourceCaseType: String): String = {
    sourceCaseType.toLowerCase() match {
      case "camel" =>
        camelToSnake(inputString)
      case "pascal" =>
        pascalToSnake(inputString)
      case "kebab" =>
        kebabToSnake(inputString)
      case _ =>
        throw new Exception(s"Snake-case conversion only supported for source case types : Camel/Pascal/Kebab..")
    }
  }

  private def camelToSnake(camelCase: String): String = {
    val regex = "([a-z])([A-Z]+)".r
    regex.replaceAllIn(camelCase, "$1_$2").toLowerCase()
  }

  private def pascalToSnake(pascalCase: String): String = {
    val snakeCase = "[A-Z\\d]".r.replaceAllIn(pascalCase, { m => "_" + m.group(0).toLowerCase() })
    snakeCase.stripPrefix("_")
  }

  private def kebabToSnake(kebabCase: String): String = {
    kebabCase.replaceAll("-", "_").toLowerCase()
  }

}
