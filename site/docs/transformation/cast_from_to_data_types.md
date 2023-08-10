# CastFromToDataTypes

This transformation lets users cast all columns having `X` data type to a different `Y` data type.

Users can provide a list of mapping for source data type and target data type.

Users can provide a flag to define whether the casting needs to be performed at nested level or only at the root level.

This transformation expects user to provide below inputs:

| Input Arguments  | Mandatory | Default Value | Description                                                                                                                                                                    |
|:-----------------|:---------:|:-------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| data-type-mapper |    Yes    |       -       | It defines the list of mapping of source data type and target data type                                                                                                        |
| cast-recursively |    No     |     false     | It defines a boolean flag that tells whether casting needs to be performed <br/>at nested level (cast-recursively = true) or only at the root level (cast-recursively = false) |


For example, consider we have below restonomer response in json:

```json
{
  "col_A": 5,
  "col_B": 4,
  "col_C": 3.4678,
  "col_D": {
    "col_E": 6
  },
  "col_F": [
    {
      "col_G": 7
    }
  ]
}
```

Now, suppose the requirement is to cast all columns having `long` data type to `string` data type and `double` data type to `decimal(5,2)` data type.

Then, user can configure the `CastFromToDataTypes` transformation in the below manner:

```hocon
{
  type = "CastFromToDataTypes"
  data-type-mapper = {
    "long" = "string"
    "double" = "decimal(5,2)"
  }
  cast-recursively = true
}
```

The transformed response will now have the columns with the desired data types:

```json
{
  "col_A": "5",
  "col_B": "4",
  "col_C": 3.47,
  "col_D": {
    "col_E": "6"
  },
  "col_F": [
    {
      "col_G": "7"
    }
  ]
}
```
