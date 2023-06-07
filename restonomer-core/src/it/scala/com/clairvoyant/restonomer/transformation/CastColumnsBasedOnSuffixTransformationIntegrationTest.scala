package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}
import org.apache.spark.sql.types.{DecimalType, DoubleType, StringType, StructField, StructType}

class CastColumnsBasedOnSuffixTransformationIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  override val expectedDFSchema: Option[StructType] = Some(
    StructType(
      List(
        StructField(name = "name", dataType = StringType),
        StructField(name = "india_price", dataType = new DecimalType(precision = 19, scale = 2)),
        StructField(name = "US_price", dataType = new DecimalType(precision = 19, scale = 2)),
        StructField(name = "percentage_difference", dataType = DoubleType)
      )
    )
  )

  it should "cast the columns of restonomer response dataframe as specified in the transformation mapper" in {
    runCheckpoint(checkpointFileName = "checkpoint_cast_columns_based_on_suffix_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_cast_columns_based_on_suffix_transformation.json")
  }

}
