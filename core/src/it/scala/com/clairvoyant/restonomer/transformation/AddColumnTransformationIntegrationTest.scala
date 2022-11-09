package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class AddColumnTransformationIntegrationTest extends IntegrationTestDependencies {

  override val mappingsDirectory: String = "transformation/add-column"

  it should "transform and add the column to the restonomer response dataframe" in {
    restonomerContext.runCheckpoint(checkpointFilePath =
      s"$mappingsDirectory/checkpoint_add_column_transformation.conf"
    )
  }

}
