# FilterRecords

It lets the user filter records from the response based on a provided filter condition.

This transformation expects user to provide below inputs:

| Input Arguments  | Mandatory | Default Value | Description                                                                    |
|:-----------------|:---------:|:-------------:|:-------------------------------------------------------------------------------|
| filter-condition |    Yes    |       -       | It is a condition expression which will be used for filtering required records |

For example, consider we have below restonomer response in json:

```json
[
  {
    "student_name": "John",
    "marks": 10
  },
  {
    "student_name": "Bob",
    "marks": 30
  }
]
```

Now, suppose the requirement is to filter student records having marks more than 20 from the data.
Then, user can configure the `FilterRecords` transformation in the below manner:

```hocon
{
  type = "FilterRecords"
  filter-condition = "marks > 20"
 }
```

The transformed response will have filtered records as desired:

```json
[
  {
    "student_name": "Bob",
    "marks": 30
  }
]
```
