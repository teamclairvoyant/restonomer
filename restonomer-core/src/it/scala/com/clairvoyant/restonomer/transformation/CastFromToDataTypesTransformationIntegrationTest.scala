package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}
import org.apache.spark.sql.types._

class CastFromToDataTypesTransformationIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  override val expectedDFSchema: Option[StructType] = Some(
    StructType(
      List(
        StructField(name = "col_A", dataType = IntegerType),
        StructField(name = "col_B", dataType = IntegerType),
        StructField(name = "col_C", dataType = new DecimalType(5, 2)),
        StructField(name = "col_D", dataType = StructType(List(StructField(name = "col_E", dataType = IntegerType)))),
        StructField(
          name = "col_F",
          dataType = ArrayType(StructType(List(StructField(name = "col_G", dataType = IntegerType))))
        )
      )
    )
  )

  it should "cast the columns of restonomer response dataframe as as per from and to data types provided" in {
    runCheckpoint(checkpointFileName = "checkpoint_cast_from_to_data_types_transformation.conf")
    outputDF should matchExpectedDataFrame(readMockJSON("expected_cast_from_to_data_types_transformation.json"))
  }

}
