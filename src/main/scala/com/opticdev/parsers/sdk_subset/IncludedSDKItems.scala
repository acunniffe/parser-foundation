package com.opticdev.parsers.sdk_subset

import com.opticdev.parsers.ParserBase
import com.opticdev.sdk.descriptions.PackageExportable
import com.opticdev.sdk.skills_sdk.lens.OMLens
import com.opticdev.sdk.skills_sdk.schema.OMSchema

case class IncludedSDKItems(items: IncludedSDKItem[_ >: OMLens with OMSchema <: PackageExportable]*) {
  require(items.map(_.id).distinct.size == items.size, "Internal SDK Items can not have duplicate ids.")

  def abstractions = items.collect{ case i if i.isAbstraction => i.asInstanceOf[IncludedSDKItem[OMSchema]] }
  def generators = items.collect{ case i if i.isGenerator => i.asInstanceOf[IncludedSDKItem[OMLens]] }

  def packageRef(parser: ParserBase) = packageRefFromParser(parser)
}
