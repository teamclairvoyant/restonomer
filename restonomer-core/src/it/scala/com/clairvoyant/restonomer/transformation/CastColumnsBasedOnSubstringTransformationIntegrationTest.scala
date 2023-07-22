package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}
import org.apache.spark.sql.types.{DecimalType, StringType, StructField, StructType}

class CastColumnsBasedOnSubstringTransformationIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  override val expectedDFSchema: Option[StructType] = Some(
    StructType(
      List(
        StructField(name = "name", dataType = StringType),
        StructField(name = "product_price_in_india", dataType = new DecimalType(precision = 19, scale = 2)),
        StructField(name = "product_price_in_uk", dataType = new DecimalType(precision = 19, scale = 2)),
        StructField(name = "percentage_difference", dataType = new DecimalType(precision = 19, scale = 2))
      )
    )
  )

  it should "cast the columns of restonomer response dataframe as specified in the transformation mapper" in {
    runCheckpoint(checkpointFileName = "checkpoint_cast_columns_based_on_substring_transformation.conf")
    outputDF should matchExpectedDataFrame(readMockJSON("expected_cast_columns_based_on_substring_transformation.json"))
  }

}
