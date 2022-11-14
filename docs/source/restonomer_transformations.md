# What is restonomer transformation ?

Once the api response has been received by the application and transformed into spark dataframe,
one can apply the sequence of pre-defined transformations on the response dataframe.

This gives user the ability to modify the data in the desired format before persisting it in the target system.

The restonomer transformation is represented by the sealed trait `RestonomerTransformation`:

```scala
sealed trait RestonomerTransformation {
  def transform(restonomerResponseDF: DataFrame): DataFrame
}
```

You can add a configuration for the restonomer transformation in the checkpoint conf file in the 
below manner:

```hocon
name = "checkpoint_add_column_transformation"

request = {
  url = "http://localhost:8080/add-column-transformation"
}

response = {
  body = {
    format = "JSON"
  }

  transformations = [
    {
      type = "add-column"
      column-name = "col_D"
      column-value = "val_D"
      column-data-type = "string"
    }
  ]
}

```

# Types of restonomer transformations

## AddColumn Transformation

It lets the user add a new column with a literal value of the desired data type.

This transformation needs below inputs from the user:

| Input Arguments  | Mandatory | Default Value | Description                                                |
|:-----------------|:---------:|:-------------:|:-----------------------------------------------------------|
| column-name      |    Yes    |       -       | Name of the new column to be added                         |
| column-value     |    Yes    |       -       | Literal value of the new column                            |
| column-data-type |    No     |    string     | The spark sql data type that new column needs to be casted |

User can configure the AddColumn transformation in the below manner:

```hocon
{
  type = "add-column"
  column-name = "col_D"
  column-value = "val_D"
  column-data-type = "string"
}
```
