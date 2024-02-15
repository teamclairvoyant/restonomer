package com.clairvoyant.restonomer.response_body

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}
import org.apache.spark.sql.types.*
class ExcelResponseToDataFrameConverterIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "response_body"

  override val expectedDFSchema: Option[StructType] = Some(
    StructType(
      List(
        StructField(name = "Product line", dataType = StringType),
        StructField(name = "Seller", dataType = StringType),
        StructField(name = "Tracking ID", dataType = StringType),
        StructField(name = "Date shipped", dataType = StringType),
        StructField(name = "Price", dataType = DoubleType),
        StructField(name = "Referral fee rate", dataType = StringType),
        StructField(name = "Quantity", dataType = IntegerType),
        StructField(name = "Revenue", dataType = DoubleType),
        StructField(name = "Earnings", dataType = DoubleType),
        StructField(name = "Sub Tag", dataType = StringType)
      )
    )
  )

  it should "convert the excel file response body into a dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_excel_response_dataframe_converter.conf")
    outputDF should matchExpectedDataFrame("expected_excel_response.json")
  }

}
