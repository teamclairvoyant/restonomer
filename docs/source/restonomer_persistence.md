# What is restonomer persistence ?

Once the restonomer response is generated and the transformations(if any) have been applied to the response, then 
restonomer framework gives you the provision to persist the response to any target data source.

The restonomer persistence is described by a sealed trait `RestonomerPersistence`.

User can configure the persistence attribute under the `data-response` attribute in checkpoint in the below manner. 
For example, consider the below FileSystem persistence:

```hocon
  persistence = {
    type = "FileSystem"
    file-format = "JSON"
    file-path = "./rest-output/"
  }
```

The complete checkpoint configuration after adding persistence config looks like:

```hocon
name = "checkpoint_no_authentication"

data = {
  data-request = {
    url = "http://ip.jsontest.com"
  }

  data-response = {
    body = {
      type = "JSON"
    }

    transformations = [
      {
        type = "AddColumn"
        column-name = "test_column_1"
        column-value = "test_value_1"
        column-data-type = "string"
      }
    ]

    persistence = {
      type = "FileSystem"
      file-format = "JSON"
      file-path = "./rest-output/"
    }
  }
}
```

# Types of restonomer persistence

## FileSystem

The FileSystem persistence allows user to persist the response dataframe to a local file system in the desired format at 
the desired path.

The FileSystem persistence needs below arguments from the user:

| Input Arguments | Mandatory | Default Value | Description                                                                                                                                               |
|:----------------|:---------:|:-------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------|
| file-format     |    Yes    |       -       | The format (json/csv/parquet) of the files to be persisted for the response dataframe                                                                     |
| file-path       |    Yes    |       -       | The path of the directory where output files will be persisted                                                                                            |
| save-mode       |    No     | ErrorIfExists | This is used to specify the expected behavior of saving a DataFrame to a data source.<br/> Expected values are (append, overwrite, errorifexists, ignore) |


User can configure the FileSystem persistence in the below manner:

```hocon
persistence = {
  type = "FileSystem"
  file-format = "JSON"
  file-path = "./rest-output/"
}
```

## S3Bucket

The S3Bucket persistence allows user to persist the restonomer response dataframe to AWS S3 bucket.

The S3Bucket persistence needs below arguments from the user:

| Input Arguments | Mandatory | Default Value | Description                                                                                                                                               |
|:----------------|:---------:|:-------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------|
| bucket-name     |    Yes    |       -       | The name of the s3 bucket where the dataframe files need to be stored                                                                                     |
| file-format     |    Yes    |       -       | The format (json/csv/parquet) of the files to be persisted for the response dataframe                                                                     |
| file-path       |    Yes    |       -       | The path of the directory where output files will be persisted                                                                                            |
| save-mode       |    No     | ErrorIfExists | This is used to specify the expected behavior of saving a DataFrame to a data source.<br/> Expected values are (append, overwrite, errorifexists, ignore) |

User can configure the S3Bucket persistence in the below manner:

```hocon
persistence = {
  type = "S3Bucket"
  bucket-name = "test-bucket"
  file-format = "JSON"
  file-path = "test-output-dir"
}
```

Now, in order to make this work, user need to first authenticate against AWS account.
This can be done in either of below 2 ways:

*   **Setting up environment variables**

    User need to set below two environment variables in their execution environment:

    *   `AWS_ACCESS_KEY` or `AWS_ACCESS_KEY_ID`
    *   `AWS_SECRET_KEY` or `AWS_SECRET_ACCESS_KEY`

    Users should know beforehand the values of above credentials for their AWS account.

*   **Setting up spark configs**

    User can add below spark configs in the `application.conf` file present under restonomer context directory.

    *   `spark.hadoop.fs.s3a.access.key`
    *   `spark.hadoop.fs.s3a.secret.key`

```hocon
    spark-configs = {
      "spark.hadoop.fs.s3a.access.key" = "<AWS_ACCESS_KEY>"
      "spark.hadoop.fs.s3a.secret.key" = "<AWS_SECRET_KEY>"
    }
```

## GCSBucket

When user wants to use GCSBucket service from GCP for persistence, he can use GCSBucket persistence feature from restonomer.

The GCSBucket persistence needs below arguments from the user:

| Input Arguments            | Mandatory | Default Value | Description                                                                                                                                               |
|:---------------------------|:---------:|:-------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------|
| service-account-cred-file  |    Yes    |       -       | The filepath of the GCP service account credentials                                                                                                       |
| bucket-name                |    Yes    |       -       | The name of the Gcs bucket where the dataframe files need to be stored                                                                                    |
| file-format                |    Yes    |       -       | The format (json/csv/parquet) of the files to be persisted for the response dataframe                                                                     |
| file-path                  |    Yes    |       -       | The path of the directory where output files will be persisted                                                                                            |
| save-mode                  |    No     | ErrorIfExists | This is used to specify the expected behavior of saving a DataFrame to a data source.<br/> Expected values are (append, overwrite, errorifexists, ignore) |

User can configure the S3Bucket persistence in the below manner:

```hocon
persistence = {
  type = "GCSBucket"
  service-account-cred-file = "/user/secret/creds/gcs-cred.json"
  bucket-name = "test-bucket"
  file-format = "JSON"
  file-path = "test-output-dir"
}
```

Now, in order to make this work, user needs to provide a correct service account credentials file.