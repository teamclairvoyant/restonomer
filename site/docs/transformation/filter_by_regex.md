# Filter by Regex

It lets the user use regex to filter on an existing column.

This transformation expects user to provide below inputs:

| Input Arguments  | Mandatory | Default Value | Description                                                                    |
|:-----------------|:---------:|:-------------:|:-------------------------------------------------------------------------------|
| columnName       |    Yes    |       -       | Name of the column from the data to apply regex filter on                      |
| regex            |    Yes    |       -       | regex expression to filter out the values for the specified column             |

For example, consider we have below restonomer response in json:

```json
[
  {
    "id": "10",
    "email": "abc@gmail.com"
  },
  {
    "id": "20",
    "email": "def@yahoo.com"
  }
]
```

Now, suppose the requirement is to filter or get the email with gmail as a domain or a gmail email from the column values.
Then, user can configure the `FilterByRegex` transformation in the below manner:

```hocon
{
  columnName = "email"
  regex = "\\b[A-Za-z0-9._%+-]+@gmail\\.com\\b"
 }
```

The transformed response will have filtered records as desired:

```json
[
  {
    "id": "10",
    "email": "abc@gmail.com"
  }
]
```
