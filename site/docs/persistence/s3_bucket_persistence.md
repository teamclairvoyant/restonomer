# S3Bucket

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
