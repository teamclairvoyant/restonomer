# RenameColumns

It lets the user rename one or multiple column(s) at once.

This transformation expects user to provide below inputs:

| Input Arguments      | Mandatory | Default Value | Description                                                    |
|:---------------------|:---------:|:-------------:|:---------------------------------------------------------------|
| rename-column-mapper |    Yes    |       -       | It defines the mapping of the existing and desired column name |

Now, suppose the requirement is to rename above columns like below:

```text
col_A -> test_col_A
col_B -> COL_b
col_C -> my_column
```

Then, user can configure the `RenameColumns` transformation in the below manner:

```hocon
{
  type = "RenameColumns"
  rename-column-mapper = {
    "col_A" = "test_col_A"
    "col_B" = "COL_b"
    "col_C" = "my_column"
  }
}
```

The transformed response will now have the columns with the desired name:

```json
{
  "test_col_A": 5,
  "COL_b": 4,
  "my_column": 3.4678
}
```
