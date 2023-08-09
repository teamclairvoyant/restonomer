# FlattenSchema

It lets the user flatten the schema of the restonomer response. If any of the column is of StructType or is nested,
this transformation removes the nested structure and represent each nested attribute at a root level.

This transformation expects 0 inputs from the user.

For example, consider we have below restonomer response in json:

```json
{
  "rewardApprovedMonthPeriod": {
    "from": "2021-09",
    "to": "2021-10"
  }
}
```

Now, the agenda is to get rid of nested schema and bring the `from` and `to` columns at the root level.

User can configure the `FlattenSchema` transformation in the below manner:

```hocon
{
  type = "FlattenSchema"
}
```

The transformed response will now have the flat schema as below:

```json
{
  "rewardApprovedMonthPeriod_from": "2021-09",
  "rewardApprovedMonthPeriod_to": "2021-10"
}
```
