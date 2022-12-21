package com.clairvoyant.restonomer.core.model

case class CheckpointConfig(
    name: String,
    token: Option[TokenConfig],
    data: DataConfig
)
