package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec

class ChangeColumnCaseTransformationSpec extends DataScalaxyTestUtil {

  val restonomerResponseDF1 = readJSONFromText(
    """
      |{
      |    "col_a": "1",
      |    "COL_B": "2"
      |}""".stripMargin
  )

  val restonomerResponseDF2 = readJSONFromText(
    """
      |{
      |    "colA": "1",
      |    "colB": "2"
      |}""".stripMargin
  )

  val restonomerResponseDF3 = readJSONFromText(
    """
      |{
      |    "ColA": "1",
      |    "ColB": "2"
      |}""".stripMargin
  )

  "transform() - with 'lower' targetCase" should "renames all the columns to lower case" in {
    val restonomerTransformation = ChangeColumnCase(
      targetCaseType = "lower"
    )

    val expectedDF = readJSONFromText(
      """
        |{
        |  "col_a": "1",
        |  "col_b": "2"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF1)
    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedDF)
  }

  "transform() - with 'kebab' targetCase and 'snake' sourceCase" should "renames all the columns to kebab case" in {
    val restonomerTransformation = ChangeColumnCase(
      targetCaseType = "kebab",
      sourceCaseType = "snake"
    )

    val expectedDF = readJSONFromText(
      """
        |{
        |  "col-a": "1",
        |  "col-b": "2"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF1)
    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedDF)
  }

  "transform() - with 'Camel' targetCase and 'snake' sourceCase" should "rename all the columns to camel case" in {
    val restonomerTransformation = ChangeColumnCase(
      targetCaseType = "camel",
      sourceCaseType = "snake"
    )

    val expectedDF = readJSONFromText(
      """
        |{
        |  "colA": "1",
        |  "colB": "2"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF1)
    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedDF)
  }

  "transform() - with 'Pascal' targetCase and 'snake' sourceCase" should "rename all the columns to pascal case" in {
    val restonomerTransformation = ChangeColumnCase(
      targetCaseType = "pascal",
      sourceCaseType = "snake"
    )

    val expectedDF = readJSONFromText(
      """
        |{
        |  "ColA": "1",
        |  "ColB": "2"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF1)
    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedDF)
  }

  "transform() - with 'snake' targetCase and 'camel' sourceCase" should "rename all the columns to snake case" in {
    val restonomerTransformation = ChangeColumnCase(
      targetCaseType = "snake",
      sourceCaseType = "camel"
    )

    val expectedDF = readJSONFromText(
      """
        |{
        |  "col_a": "1",
        |  "col_b": "2"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF2)
    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedDF)
  }

  "transform() - with 'kebab' targetCase and 'camel' sourceCase" should "rename all the columns to kebab case" in {
    val restonomerTransformation = ChangeColumnCase(
      targetCaseType = "kebab",
      sourceCaseType = "camel"
    )

    val expectedDF = readJSONFromText(
      """
        |{
        |  "col-a": "1",
        |  "col-b": "2"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF2)
    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedDF)
  }

  "transform() - with 'pascal' targetCase and 'camel' sourceCase" should "rename all the columns to pascal case" in {
    val restonomerTransformation = ChangeColumnCase(
      targetCaseType = "pascal",
      sourceCaseType = "camel"
    )

    val expectedDF = readJSONFromText(
      """
        |{
        |  "ColA": "1",
        |  "ColB": "2"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF2)
    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedDF)
  }

  "transform() - with 'snake' targetCase and 'pascal' sourceCase" should "rename all the columns to snake case" in {
    val restonomerTransformation = ChangeColumnCase(
      targetCaseType = "snake",
      sourceCaseType = "pascal"
    )

    val expectedDF = readJSONFromText(
      """
        |{
        |  "col_a": "1",
        |  "col_b": "2"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF3)
    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedDF)
  }

  "transform() - with 'kebab' targetCase and 'pascal' sourceCase" should "rename all the columns to kebab case" in {
    val restonomerTransformation = ChangeColumnCase(
      targetCaseType = "kebab",
      sourceCaseType = "pascal"
    )

    val expectedDF = readJSONFromText(
      """
        |{
        |  "col-a": "1",
        |  "col-b": "2"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF3)
    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedDF)
  }

  "transform() - with 'camel' targetCase and 'pascal' sourceCase" should "rename all the columns to camel case" in {
    val restonomerTransformation = ChangeColumnCase(
      targetCaseType = "camel",
      sourceCaseType = "pascal"
    )

    val expectedDF = readJSONFromText(
      """
        |{
        |  "colA": "1",
        |  "colB": "2"
        |}
        |""".stripMargin
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF3)
    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedDF)
  }

  "transform() - with unsupported source case type " should "throw Exception" in {
    val restonomerTransformation = ChangeColumnCase(
      targetCaseType = "camel",
      sourceCaseType = "upper"
    )

    assertThrows[Exception] {
      restonomerTransformation.transform(restonomerResponseDF3)
    }
  }

}
