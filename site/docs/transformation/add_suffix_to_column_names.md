# AddSuffixToColumnNames

It lets the user add a desired suffix to select/all column names.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                                                            |
|:----------------|:---------:|:-------------:|:-----------------------------------------------------------------------|
| suffix          |    Yes    |       -       | It defines the desired suffix that will be added to the column name    |
| column-names    |    no     |      all      | It defines the list of column names to which the suffix will get added |

For example, consider we have below restonomer response in json:

```json
{
  "col_A": 5,
  "col_B": 4,
  "col_C": 3
}
```

Now, suppose the requirement is to add suffix to the few of the columns like below:

```text
col_A -> col_A_old
col_B -> col_B_old
```

Then, user can configure the `AddSuffixToColumnNames` transformation in the below manner:

```hocon
{
  type = "AddSuffixToColumnNames"
  suffix = "old"
  column-names = ["col_A", "col_B"]
}
```

The transformed response will now have the columns with the desired suffix like below.
Note that, underscore character ('_') will get added automatically, separating suffix and the column name.

```json
{
  "col_A_old": 5,
  "col_B_old": 4,
  "col_C": 3
}
```
