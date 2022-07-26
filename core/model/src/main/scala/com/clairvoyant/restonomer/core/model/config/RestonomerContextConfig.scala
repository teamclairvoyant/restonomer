package com.clairvoyant.restonomer.core.model.config

case class RestonomerContextConfig(
    checkpoints: List[CheckpointConfig],
    requests: List[RequestConfig],
    authentications: List[AuthenticationConfig]
)
