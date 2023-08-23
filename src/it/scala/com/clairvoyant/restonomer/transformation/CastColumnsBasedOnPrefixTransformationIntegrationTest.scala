package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}
import org.apache.spark.sql.types.*

class CastColumnsBasedOnPrefixTransformationIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  override val expectedDFSchema: Option[StructType] = Some(
    StructType(
      List(
        StructField(name = "name", dataType = StringType),
        StructField(name = "price_in_india", dataType = new DecimalType(precision = 19, scale = 2)),
        StructField(name = "price_in_uk", dataType = new DecimalType(precision = 19, scale = 2)),
        StructField(name = "percentage_difference", dataType = DoubleType)
      )
    )
  )

  it should "cast the columns of restonomer response dataframe as specified in the transformation mapper" in {
    runCheckpoint(checkpointFileName = "checkpoint_cast_columns_based_on_prefix_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_cast_columns_based_on_prefix_transformation.json")
  }

}
