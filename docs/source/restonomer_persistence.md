# What is restonomer persistence ?

Once the restonomer response is generated and the transformations(if any) have been applied to the response, then 
restonomer framework gives you the provision to persist the response to any target data source.

The restonomer persistence is described by a sealed trait `RestonomerPersistence` :

```scala
sealed trait RestonomerPersistence {

  def persist(restonomerResponseDF: DataFrame, dataFrameWriter: DataFrameWriter): Unit =
    dataFrameWriter.write(restonomerResponseDF)

}
```

User can configure the persistence attribute under the `response` attribute in checkpoint in the below manner. 
For example, consider the below FileSystem persistence:

```hocon
  persistence = {
    type = "file-system"
    file-format = "json"
    file-path = "./rest-output/"
  }
```

The complete checkpoint configuration after adding persistence config looks like:

```hocon
name = "checkpoint_no_authentication"

request = {
  url = "http://ip.jsontest.com"
}

response = {
  body = {
    format = "JSON"
  }

  transformations = [
    {
      type = "add-column"
      column-name = "test_column_1"
      column-value = "test_value_1"
      column-data-type = "string"
    }
  ]

  persistence = {
    type = "file-system"
    file-format = "json"
    file-path = "./rest-output/"
  }
}
```

# Types of restonomer persistence

## FileSystem

The FileSystem persistence allows user to persist the response dataframe to a local file system in the desired format at 
the desired path.

The FileSystem persistence is denoted by a case class `FileSystem` :

```scala
case class FileSystem(
    fileFormat: String,
    filePath: String
) extends RestonomerPersistence
```

The FileSystem persistence needs below arguments from the user:

| Input Arguments | Mandatory | Default Value | Description                                                        |
|:----------------|:---------:|:-------------:|:-------------------------------------------------------------------|
| file-format     |    Yes    |    Parquet    | The format of the files to be persisted for the response dataframe |
| file-path       |    Yes    |       -       | The path of the directory where output files will be persisted     |

User can configure the FileSystem persistence in the below manner:

```hocon
persistence = {
  type = "file-system"
  file-format = "json"
  file-path = "./rest-output/"
}
```
