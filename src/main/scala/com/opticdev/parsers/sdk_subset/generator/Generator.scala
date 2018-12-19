package com.opticdev.parsers.sdk_subset.generator
import com.opticdev.parsers.sdk_subset.{IncludedSDKItem, packageRefFromParser}
import com.opticdev.common.{PackageRef, SchemaRef}
import com.opticdev.parsers.ParserBase
import com.opticdev.sdk.skills_sdk.OMSnippet
import com.opticdev.sdk.skills_sdk.lens.{OMLens, OMLensCodeComponent, OMLensVariableScopeEnum}
import play.api.libs.json.JsObject

case class Generator(id: String,
                     abstractionId: String,
                     snippet: String,
                     value: Map[String, OMLensCodeComponent],
                     variables: Map[String, OMLensVariableScopeEnum] = Map()) extends IncludedSDKItem[OMLens] {

  def toInternal(implicit parser: ParserBase) = {
    OMLens(
      None,
      id,
      OMSnippet(parser.languageName, snippet),
      value,
      variables,
      Map(),
      Left(SchemaRef(Some(packageRefFromParser), abstractionId)),
      JsObject.empty,
      parser.languageName,
      packageRefFromParser,
      1,
      internal = true
    )
  }

  override def isGenerator: Boolean = true

}

