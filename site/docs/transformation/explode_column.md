# ExplodeColumn

It lets the user explode specific column values into multiple rows with the same column name from the restonomer
response dataframe.

This transformation needs below inputs from the user:

| Input Arguments | Mandatory | Default Value | Description                       |
|:----------------|:---------:|:-------------:|:----------------------------------|
| column-name     |    Yes    |       -       | The name of column to be exploded |

User can configure the `ExplodeColumn` transformation in the below manner:

```hocon
{
  type = "ExplodeColumn"
  column-name = "col_A"
}
```
