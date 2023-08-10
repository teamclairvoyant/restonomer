# FileSystem

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
