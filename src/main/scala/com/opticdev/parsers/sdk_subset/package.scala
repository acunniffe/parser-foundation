package com.opticdev.parsers

import com.opticdev.common.PackageRef
import com.opticdev.sdk.descriptions.PackageExportable

package object sdk_subset {
  def packageRefFromParser(implicit parserBase: ParserBase) =
    PackageRef("host-lang-"+parserBase.languageName, parserBase.parserVersion)


  trait IncludedSDKItem[A <: PackageExportable] {
    def toInternal(implicit parser: ParserBase): A
    def id: String
  }

}
