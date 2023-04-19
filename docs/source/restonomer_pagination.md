# What is Restonomer Pagination ?

Restonomer provides support for pagination while fetching huge datasets from the REST API.
Some APIs are not able to provide the complete data in a single request, and hence they make use of pagination to load 
the data in consecutive pages. 
A separate http request gets created for each page.

Each API has its own custom pagination scheme, and Restonomer internally implements the solution to deal with the same.
The users just need to provide few configurations without letting themselves know about the internal details of the implementation.

# Types of Restonomer Pagination

## Page Number Based Pagination

In this pagination mechanism, the huge datasets are split into multiple pages.

The Rest API returns you a response with partial datasets in a single request and then again consecutive requests needs 
to be triggered in order to get the complete dataset.

Along with the dataset, the api provides other details that are required in order to fetch the next page, such as:

*   Total number of records in the complete dataset
*   Current page number

Apart from above details, user should be aware of the below details:

*   Limitation on maximum number of records that api return per page
*   The query param name for the next page

Once we know above details, user can configure pagination in the below manner. Consider we are getting below response 
from API:

```json
{
  "data": {
    "total": {
      "numberItems": 3
    },
    "page": 1,
    "items": [
      {
        "col_A": "val_1",
        "col_B": "val_2",
        "col_C": "val_3"
      }
    ]
  }
}
```

In the above response: 

*   the total number of records in the complete dataset is represented by `data.total.numberItems`
*   the current page number is represented by `data.page`
*   assume that maximum number of records that api return per page is `1`
*   the query param name for next page is `page`

Then, the pagination details can be captured in the checkpoint file in the below manner:

```hocon
    pagination = {
      type = "PageNumberBasedPagination"
      total-number-of-records-attribute = "$.data.total.numberItems"
      current-page-number-attribute = "$.data.page"
      max-records-per-page = 1
      page-token-name = "page"
    }
```

The `total-number-of-records-attribute` and `current-page-number-attribute` are represented as [JsonPath](https://support.smartbear.com/alertsite/docs/monitors/api/endpoint/jsonpath.html).

The complete example of checkpoint file including pagination is:

```hocon
name = "checkpoint_page_number_based_pagination"

data = {
  data-request = {
    url = "http://localhost:8080/page_number_based_pagination"
  }

  data-response = {
    body = {
      type = "JSON"
      data-column-name = "data.items"
    }

    pagination = {
      type = "PageNumberBasedPagination"
      total-number-of-records-attribute = "$.data.total.numberItems"
      current-page-number-attribute = "$.data.page"
      max-records-per-page = 1
      page-token-name = "page"
    }

    persistence = {
      type = "FileSystem"
      file-format = "JSON"
      file-path = "/tmp/pagination"
    }
  }
}
```