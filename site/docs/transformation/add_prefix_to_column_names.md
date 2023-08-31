# AddPrefixToColumnNames

It lets the user add a desired prefix to select/all column names.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                                                            |
|:----------------|:---------:|:-------------:|:-----------------------------------------------------------------------|
| prefix          |    Yes    |       -       | It defines the desired prefix that will be added to the column name    |
| column-names    |    No     |      all      | It defines the list of column names to which the prefix will get added |


For example, consider we have below restonomer response in json:

```json
{
  "col_A": 5,
  "col_B": 4,
  "col_C": 3
}
```

Now, suppose the requirement is to add prefix to the columns like below:

```text
col_A -> test_col_A
col_B -> test_col_B
```

Then, user can configure the `AddPrefixToColumnNames` transformation in the below manner:

```hocon
{
  type = "AddPrefixToColumnNames"
  suffix = "test"
  column-names = ["col_A", "col_B"]
}
```

The transformed response will now have the columns with the desired prefix like below.
Note that, underscore character (_) will get added automatically, separating prefix and column name part.

```json
{
  "test_col_A": 5,
  "test_col_B": 4,
  "col_C": 3
 }
```
