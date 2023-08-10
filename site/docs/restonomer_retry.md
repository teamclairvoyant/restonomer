---
sidebar_position: 9
---

# Retry Mechanism

Restonomer makes sure that its auto retry mechanism makes another attempt to retrieve the relevant information in the 
event of a request failure, depending on the type of status code it receives as a response. 

Your application will be much more robust and be able to deal with potential temporary failures of the services you rely on.

# Configs for retry

Restonomer framework expects user to provide below two arguments in case they want to override the auto retry behaviour 
of restonomer:

* **max-retries** 

  This config tells restonomer the maximum number of attempts to retry in case of a request failure. Default value is `20`.


* **status-codes-to-retry**

  This config provides the restonomer with the list of response status codes for which user wants restonomer to retry the 
  request. Default value is `[403, 429, 502, 401, 500, 504, 503, 408]`

# Default Retry Behaviour

The restonomer framework behaves different for different status codes. 
The below table gives the summary of the default retry behaviour of restonomer for different status codes:

| Response Status Code        |                                                   Retry Behaviour                                                    |
|:----------------------------|:--------------------------------------------------------------------------------------------------------------------:|
| 200 - Ok                    |                                              Returns the response body                                               |
| 204 - NoContent             |                              Throws `RestonomerException` with the message `No Content`                              |
| 302 - Found                 |             Retrieves the new uri from the `Location` response header and retry the request with new uri             |
| **`status-codes-to-retry`** | Retries the request for max `max-retries` attempts. If still the status is not OK, then throws `RestonomerException` |
| Others                      |                              Throws `RestonomerException` with the proper error message                              |






