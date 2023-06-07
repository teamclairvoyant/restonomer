package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}
import org.apache.spark.sql.types.{StructField, StructType, TimestampType}

class ConvertColumnToTimestampTransformationIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "convert the specified column to Timestamp type" in {
    runCheckpoint(checkpointFileName = "checkpoint_convert_column_to_timestamp_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_convert_column_to_timestamp_transformation.json")
  }

}
