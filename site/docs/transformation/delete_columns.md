# DeleteColumns

It lets the user delete specific columns from the restonomer response dataframe.

This transformation needs below inputs from the user:

| Input Arguments  | Mandatory | Default Value | Description                        |
|:-----------------|:---------:|:-------------:|:-----------------------------------|
| column-names     |    Yes    |       -       | List of column names to be deleted |

User can configure the `DeleteColumns` transformation in the below manner:

```hocon
{
  type = "DeleteColumns"
  column-names = ["col_A", "col_B"]
}
```
