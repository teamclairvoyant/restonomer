package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}
import org.apache.spark.sql.types._

class CastColumnsTransformationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  override val expectedDFSchema: Option[StructType] = Some(
    StructType(
      List(
        StructField(name = "col_A", dataType = StringType),
        StructField(name = "col_B", dataType = DoubleType),
        StructField(name = "col_C", dataType = new DecimalType(precision = 19, scale = 2)),
        StructField(name = "col_D", dataType = TimestampType),
        StructField(name = "col_E", dataType = DateType)
      )
    )
  )

  it should "cast the columns of restonomer response dataframe as specified in the transformation mapper" in {
    runCheckpoint(checkpointFileName = "checkpoint_cast_columns_transformation.conf")
    outputDF should matchExpectedDataFrame(readMockJSON("expected_cast_columns_transformation.json"))
  }

}
