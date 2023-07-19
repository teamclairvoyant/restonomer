package com.clairvoyant.restonomer.spark.utils.caseconverter

class KebabCaseConverter extends CaseConverter {

  def convert(inputString: String, sourceCaseType: String): String = {

    sourceCaseType.toLowerCase() match {
      case "snake" =>
        snakeToKebab(inputString)
      case "camel" =>
        camelToKebab(inputString)
      case "pascal" =>
        pascalToKebab(inputString)
      case _ =>
        throw new Exception(s"Kebab-case conversion only supported for source case types : Snake/Camel/Pascal..")
    }
  }

  private def snakeToKebab(snakeCase: String): String = {
    snakeCase.replaceAll("_", "-").toLowerCase()
  }

  private def camelToKebab(camelCase: String): String = {
    val regex = "([a-z])([A-Z]+)".r
    regex.replaceAllIn(camelCase, "$1-$2").toLowerCase()
  }

  private def pascalToKebab(pascalCase: String): String = {
    val snakeCase = "[A-Z\\d]".r.replaceAllIn(pascalCase, { m => "-" + m.group(0).toLowerCase() })
    snakeCase.stripPrefix("-")
  }

}
