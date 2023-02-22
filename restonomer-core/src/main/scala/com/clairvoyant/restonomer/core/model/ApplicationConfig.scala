package com.clairvoyant.restonomer.core.model

import zio.config._
import zio.config.magnolia._

case class ApplicationConfig(sparkConfigs: Option[Map[String, String]])

object ApplicationConfig {

  implicit val applicationConfigDescriptor: ConfigDescriptor[ApplicationConfig] =
    descriptorForPureConfig[ApplicationConfig]
      .mapKey(toKebabCase)

}
