# Sending body data
You will need to send body data with requests whenever you need to add or update structured data. For example, if you're sending a request to add a new customer to a database, you might include the customer details in plain text.

By default, the response will select None—leave it selected if you don't need to send a body with your request.

```scala

  def withBody(body: Option[String] = None): RestonomerRequestBuilder = {
    copy(httpRequest = body.map(s => httpRequest.body(s)).getOrElse(httpRequest))
  }

```