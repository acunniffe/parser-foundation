package com.opticdev.parsers.sourcegear.basic

import com.opticdev.parsers.{AstGraph, ParserBase}
import com.opticdev.parsers.graph.CommonAstNode
import play.api.libs.json.{JsString, JsValue}

import scala.util.{Failure, Try}

abstract class Token extends NodeInterface {
  def isValidValue(value: String) : Boolean
}

case class TokenInterfaces(tokens: Token*)(implicit basicSourceInterface: BasicSourceInterface, sourceParser: ParserBase) extends NodeInterfaceGroup {
  val tokenTypes = tokens.map(_.astType).toSet

  def interfaceFor(astPrimitiveNode: CommonAstNode): Boolean = tokenTypes.contains(astPrimitiveNode.nodeType)

  def parseNode(astPrimitiveNode: CommonAstNode, astGraph: AstGraph, raw: String): Try[JsValue] = {
    val tokenInterfaceOption = tokens.find(_.astType == astPrimitiveNode.nodeType)
    if (tokenInterfaceOption.isDefined) {
      Try(tokenInterfaceOption.get.parser.apply(astPrimitiveNode, astGraph, raw, basicSourceInterface))
    } else {
      Failure(new Error("No Token interface for node type: "+astPrimitiveNode.nodeType))
    }
  }

  def mutateNode(astPrimitiveNode: CommonAstNode, astGraph: AstGraph, raw: String, newValue: JsValue) : Try[String] = {
    val tokenInterfaceOption = tokens.find(_.astType == astPrimitiveNode.nodeType)
    if (tokenInterfaceOption.isDefined) {
      Try(tokenInterfaceOption.get.mutator.apply(astPrimitiveNode, astGraph, raw, newValue, sourceParser, basicSourceInterface))
    } else {
      Failure(new Error("No Token interface for node type: "+astPrimitiveNode.nodeType))
    }
  }

  override def generateFrom(value: JsValue) = Try {
    tokens.head.generator(value, sourceParser, basicSourceInterface)
  }

}

