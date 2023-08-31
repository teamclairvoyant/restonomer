# CastColumns

It lets the user cast the data type of multiple columns to the desired different types at once.

This transformation expects user to provide below inputs:

| Input Arguments         | Mandatory | Default Value | Description                                               |
|:------------------------|:---------:|:-------------:|:----------------------------------------------------------|
| column-data-type-mapper |    Yes    |       -       | It defines the mapping of column to its desired data type |

For example, consider we have below restonomer response in json:

```json
{
  "col_A": 5,
  "col_B": 4,
  "col_C": 3.4678,
  "col_D": "1990-07-23 10:20:30",
  "col_E": "23-07-1990 10:20:30",
  "col_F": "1990-07-23",
  "col_G": "23-07-1990"
}
```

Now, suppose the requirement is to cast above columns into below data types:

```text
col_A -> string
col_B -> double
col_C -> decimal type with precision 19 and scale 2
col_D -> TimestampType
col_E -> TimestampType
col_F -> DateType
col_G -> DateType
```

Then, user can configure the `CastColumns` transformation in the below manner:

```hocon
{
  type = "CastColumns"
  column-data-type-mapper = {
    "col_A" = "string"
    "col_B" = "double"
    "col_C" = "decimal(19,2)"
    "col_D" = "timestamp"
    "col_E" = "timestamp(dd-MM-yyyy HH:mm:ss)"
    "col_F" = "date"
    "col_G" = "date(dd-MM-yyyy)"
  }
}
```

The transformed response will now have the columns with the desired data types:

```json
{
  "col_A": "5",
  "col_B": 4.0,
  "col_C": 3.47,
  "col_D": "1990-07-23 10:20:30",
  "col_E": "1990-07-23 10:20:30",
  "col_F": "1990-07-23",
  "col_G": "1990-07-23"
}
```
