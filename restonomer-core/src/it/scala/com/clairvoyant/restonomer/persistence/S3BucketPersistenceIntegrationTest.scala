package com.clairvoyant.restonomer.persistence

import com.clairvoyant.restonomer.common.MockS3Server._
import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockS3BucketPersistence, MockS3Server}
import org.apache.hadoop.conf.Configuration

class S3BucketPersistenceIntegrationTest
    extends IntegrationTestDependencies
    with MockS3Server
    with MockS3BucketPersistence {

  override val mappingsDirectory: String = "persistence"

  val hadoopConfigurations: Configuration = sparkSession.sparkContext.hadoopConfiguration

  hadoopConfigurations.set("fs.s3a.endpoint", s3MockEndpoint)
  hadoopConfigurations.set("fs.s3a.access.key", s3MockAWSAccessKey)
  hadoopConfigurations.set("fs.s3a.secret.key", s3MockAWSSecretKey)
  hadoopConfigurations.set("fs.s3a.path.style.access", "true")
  hadoopConfigurations.set("fs.s3a.change.detection.version.required", "false")

  it should "persist the restonomer response dataframe in the s3 bucket in the desired format at the desired path" in {
    s3Client.createBucket(s3MockBucketName)

    runCheckpoint(checkpointFileName = "checkpoint_s3_bucket_persistence.conf")
    outputDF should matchExpectedDataFrame(expectedDF("expected_s3_bucket_persistence.json"))
  }

  override def beforeAll(): Unit = {
    super[IntegrationTestDependencies].beforeAll()
    super[MockS3Server].beforeAll()
  }

  override def afterAll(): Unit = {
    super[IntegrationTestDependencies].afterAll()
    super[MockS3Server].afterAll()
  }

}
