# SelectColumns

It lets the user select a list of columns from the dataframe.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                                |
|:----------------|:---------:|:-------------:|:-------------------------------------------|
| column-names    |    Yes    |       -       | It is the list of columns required by user |

For example, consider we have below restonomer response in json:

```json
{
  "col_A": 5,
  "col_B": 4,
  "col_C": 3.4678
}
```

Now, suppose the requirement is to select 2 columns from dataframe :

```text

  "col_B": 4,
  "col_C": 3.4678

```

Then, user can configure the `SelectColumns` transformation in the below manner:

```hocon
{
  type = "SelectColumns"
  column-names= ["col_B", "col_C"]
  }
```

The transformed response will select the desired column from dataframe as shown below.

```json
{
  "col_B": 4,
  "col_C": 3.4678
}
```
