package com.opticdev.parsers.sdk_subset

import com.opticdev.sdk.descriptions.PackageExportable
import com.opticdev.sdk.skills_sdk.lens.OMLens
import com.opticdev.sdk.skills_sdk.schema.OMSchema

case class IncludedSDKItems(items: IncludedSDKItem[_ >: OMLens with OMSchema <: PackageExportable]*) {
  require(items.map(_.id).distinct.size == items.size, "Internal SDK Items can not have duplicate ids.")
}
