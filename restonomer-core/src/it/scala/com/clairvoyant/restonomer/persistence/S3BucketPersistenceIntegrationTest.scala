package com.clairvoyant.restonomer.persistence

import com.clairvoyant.data.scalaxy.test.util.mock.S3BucketMock
import com.clairvoyant.restonomer.common.IntegrationTestDependencies

class S3BucketPersistenceIntegrationTest extends IntegrationTestDependencies with S3BucketMock {

  override val mappingsDirectory: String = "persistence"

  it should "persist the restonomer response dataframe in the s3 bucket in the desired format at the desired path" in {
    val bucketName = "test-bucket"

    s3Client.createBucket(bucketName)

    runCheckpoint(checkpointFileName = "checkpoint_s3_bucket_persistence.conf")
    readParquet(s"s3a://$bucketName/test-output-dir") should matchExpectedDataFrame(
      "expected_s3_bucket_persistence.json"
    )
  }

  override def beforeAll(): Unit = {
    super[IntegrationTestDependencies].beforeAll()
    super[S3BucketMock].beforeAll()
  }

  override def afterAll(): Unit = {
    super[IntegrationTestDependencies].afterAll()
    super[S3BucketMock].afterAll()
  }

}
