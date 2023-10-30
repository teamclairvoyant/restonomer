# Concat Columns

It lets the user create a new field with the value being formed by concatenating the values of other existing fields.

This transformation expects user to provide below inputs:

| Input Arguments            | Mandatory | Default Value | Description                                                     |
|:---------------------------|:---------:|:-------------:|:----------------------------------------------------------------|
| new-column-name            |    Yes    |       -       | The name of new column to be created.                           |
| columns-to-be-concatenated |    Yes    |       -       | The list of columns to be concatenated.                         |
| separator                  |    No     | Empty String  | The character by which concatenated values should be separated. |

For example, consider we have below restonomer response in json:

```json
{
  "street": "Baner Road",
  "city": "Pune",
  "country" : "India"
}
```

Now, suppose the requirement is to create a new column `address` having the value formed by concatenating the values of columns `street`, `city` and `country` separated by `comma(,)`.

Then, user can configure the `ConcatColumns` transformation in the below manner:

```hocon
{
  type = "ConcatColumns"
  new-column-name = "address"
  columns-to-be-concatenated = ["street", "city", "country"]
  separator = ","
}
```

The transformed response will now have the `address` field as below:

```json
{
  "street": "Baner Road",
  "city": "Pune",
  "country": "India",
  "address": "Baner Road,Pune,India"
}
```
