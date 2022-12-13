package com.clairvoyant.restonomer.core.model

case class RetryConfig(
    maxRetries: Int = 20,
    statusCodesToRetry: List[Int] = List(403, 429, 502, 401, 500, 504, 503, 408)
)
