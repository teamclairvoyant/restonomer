package com.clairvoyant.restonomer.core.common

import scala.language.implicitConversions

object RestonomerContextConfigTypes extends Enumeration {

  val CHECKPOINT: RestonomerContextConfigTypesDetails = RestonomerContextConfigTypesDetails("checkpoints")

  implicit def valueToRestonomerContextConfigTypesDetails(x: Value): RestonomerContextConfigTypesDetails =
    x.asInstanceOf[RestonomerContextConfigTypesDetails]

  protected case class RestonomerContextConfigTypesDetails(configDirectoryName: String)
      extends super.Val(configDirectoryName)

}
