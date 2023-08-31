# CastNestedColumn

It lets the user cast the data type of any nested or struct type column from one type to another.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                                           |
|:----------------|:---------:|:-------------:|:------------------------------------------------------|
| column-name     |    Yes    |       -       | The name of the nested or struct column               |
| ddl             |    Yes    |       -       | The new Data Definition Language (DDL) for the column |

For example, consider we have below restonomer response in json:

```json
{
  "col_A": "val_A",
  "col_B": {
     "col_C": "val_C",
     "col_D": 5
  }
}
```

Now, there is no direct way to cast the data type of `col_D` from `Long` to `String`. But it can be easily done using the
`CastNestedColumn` transformation.

User can configure the `CastNestedColumn` transformation in the below manner:

```hocon
{
  type = "CastNestedColumn"
  column-name = "col_B"
  ddl = "col_C STRING, col_D STRING"
}
```

The transformed response will now have the data type of `col_D` as `String`:

```json
{
  "col_A": "val_A",
  "col_B": {
     "col_C": "val_C",
     "col_D": "5"
  }
}
```
