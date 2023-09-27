# CoalesceColumns

It lets the user create a new field with the value being formed by getting the first non null value from the list of columns provided by the user.

This transformation expects user to provide below inputs:

| Input Arguments     | Mandatory | Default Value | Description                                           |
| :------------------ | :-------: | :-----------: | :---------------------------------------------------- |
| new-column-name     |    Yes    |       -       | The name of new column to be created.                 |
| columns-to-coalesce |    Yes    |       -       | The list of columns to perform coalesce operation on. |

For example, consider we have below restonomer response in json:

```json
{
  "col_A": null,
  "col_B": "Pune",
  "col_C": null
}
```

Now, suppose the requirement is to create a new column `col_D` having the value which is equal to the first non null value from the list of columns `col_A`, `col_B` and `col_C`. 
Then, user can configure the `CoalesceColumns` transformation in the below manner:

```hocon
{
  type = "CoalesceColumns"
  new-column-name = "col_D"
  columns-to-coalesce = ["col_A", "col_B", "col_C"]
}
```

The transformed response will now have the `col_D` field as below:

```json
{
  "col_A": null,
  "col_B": "Pune",
  "col_C": null,
  "col_D": "Pune"
}
```
