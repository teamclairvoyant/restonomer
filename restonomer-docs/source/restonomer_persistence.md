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

| Input Arguments | Mandatory | Default Value | Description                                                        |
|:----------------|:---------:|:-------------:|:-------------------------------------------------------------------|
| file-format     |    Yes    |    Parquet    | The format of the files to be persisted for the response dataframe |
| file-path       |    Yes    |       -       | The path of the directory where output files will be persisted     |

User can configure the FileSystem persistence in the below manner:

```hocon
persistence = {
  type = "FileSystem"
  file-format = "JSON"
  file-path = "./rest-output/"
}
```
