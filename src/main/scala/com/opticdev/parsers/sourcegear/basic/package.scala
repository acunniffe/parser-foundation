package com.opticdev.parsers.sourcegear

import com.opticdev.parsers.{AstGraph, ParserBase}
import com.opticdev.parsers.graph.{CommonAstNode, AstType}
import play.api.libs.json.JsValue

import scala.util.Try

package object basic {

  trait NodeInterface {
    val astType : AstType
    val parser : SourceParser
    val mutator : SourceMutator
    val generator : SourceGenerator
  }

  trait NodeInterfaceGroup {
    def parseNode(astPrimitiveNode: CommonAstNode, astGraph: AstGraph, raw: String): Try[JsValue]
    def mutateNode(astPrimitiveNode: CommonAstNode, astGraph: AstGraph, raw: String, newValue: JsValue) : Try[String]
    def generateFrom(value: JsValue) : Try[String]
  }

  type SourceParser  = (CommonAstNode, AstGraph, String, BasicSourceInterface) => JsValue
  type SourceMutator = (CommonAstNode, AstGraph, String, JsValue, ParserBase, BasicSourceInterface) => String
  type SourceGenerator = (JsValue, ParserBase, BasicSourceInterface) => String
}
