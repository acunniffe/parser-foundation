package com.opticdev.parsers

import com.opticdev.parsers.graph.{AstType, CommonAstNode}
import play.api.libs.json.{JsString, JsValue}

import scala.util.Try

case class IdentifierNodeDesc(nodeType: AstType, path: Seq[String]) {
  def parse(node: CommonAstNode) : Try[String] = Try {
    require(node.isASTType(nodeType))

    val walked = path.foldLeft(node.properties: JsValue) { case (i, a) => (i \ a).get}
    walked.as[JsString].value
  }
}
