# Filter by Regex

It lets the user use regex to filter records on an existing column.

This transformation expects user to provide below inputs:

| Input Arguments | Mandatory | Default Value | Description                                                     |
|:----------------|:---------:|:-------------:|:----------------------------------------------------------------|
| column-name     |    Yes    |       -       | Name of the column against which regex needs to be matched      |
| regex           |    Yes    |       -       | The regex expression to be matched against the specified column |

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

Now, suppose the requirement is to filter those records only that have email address ending with `@gmail.com`.
Then, user can configure the `FilterByRegex` transformation in the below manner:

```hocon
{
  column-name = "email"
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
