# ReplaceEmptyStringsWithNulls

This transformation lets users replace all occurrences of empty strings with nulls in a dataframe.

For example, consider we have below restonomer response in json:

```json
{
  "col_A": "",
  "col_B": 4,
  "col_C": ""
}
```

Now, if the requirement is to replace empty strings in `col_A` and `col_C` with `null`, 
then user can configure the `ReplaceEmptyStringsWithNulls` transformation in the below manner:

```hocon
transformations = [ "ReplaceEmptyStringsWithNulls" ]
```

The transformed response will look like:

```json
{
  "col_A": null,
  "col_B": 4,
  "col_C": null
}
```
