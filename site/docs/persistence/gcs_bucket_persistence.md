# GCSBucket

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
