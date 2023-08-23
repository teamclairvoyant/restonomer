package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class AddColumnWithExpressionTransformationIntegrationTest
    extends IntegrationTestDependencies
    with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "transform and add the column as per the expression to the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_add_column_with_expression_transformation.conf")
    outputDF should matchExpectedDataFrame("expected_add_column_with_expression_transformation.json")
  }

}
