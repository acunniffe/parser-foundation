package com.opticdev.parsers.sdk_subset

import com.fasterxml.jackson.databind.JsonNode
import com.github.fge.jsonschema.main.JsonSchemaFactory
import com.opticdev.common.SchemaRef
import com.opticdev.parsers.ParserBase
import com.opticdev.sdk.skills_sdk.schema.OMSchema
import play.api.libs.json.JsObject

case class Abstraction(id: String, schema: JsObject) extends IncludedSDKItem[OMSchema] {
  require(Abstraction.isValid(schema), "invalid schema specified "+ Abstraction.validatorFactory.getSyntaxValidator.validateSchema(schema.as[JsonNode]).toString)

  override def toInternal(implicit parser: ParserBase): OMSchema = {
    OMSchema(
      SchemaRef(Some(packageRefFromParser), id),
      schema,
      internal = true
    )
  }

  override def isAbstraction: Boolean = true
}

object Abstraction {
  val validatorFactory = JsonSchemaFactory.newBuilder().freeze()
  def isValid(schema: JsObject): Boolean = validatorFactory.getSyntaxValidator.schemaIsValid(schema.as[JsonNode])
}