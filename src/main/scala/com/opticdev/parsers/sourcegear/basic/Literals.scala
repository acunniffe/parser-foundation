package com.opticdev.parsers.sourcegear.basic

import com.opticdev.common.graph.{AstGraph, CommonAstNode}
import com.opticdev.parsers.ParserBase
import play.api.libs.json.JsValue

import scala.util.{Failure, Try}

abstract class Literal extends NodeInterface

case class LiteralInterfaces(literals: Literal*)(implicit basicSourceInterface: BasicSourceInterface, sourceParser: ParserBase) extends NodeInterfaceGroup {
  val literalTypes = literals.map(_.astType).toSet

  def interfaceFor(astPrimitiveNode: CommonAstNode): Boolean = literalTypes.contains(astPrimitiveNode.nodeType)

  def parseNode(astPrimitiveNode: CommonAstNode, astGraph: AstGraph, raw: String): Try[JsValue] = {
    val literalinterfaceOption = literals.find(_.astType == astPrimitiveNode.nodeType)
    if (literalinterfaceOption.isDefined) {
      Try(literalinterfaceOption.get.parser.apply(astPrimitiveNode, astGraph, raw, basicSourceInterface))
    } else {
      Failure(new Error("No Literal interface for node type: "+astPrimitiveNode.nodeType))
    }
  }

  def mutateNode(astPrimitiveNode: CommonAstNode, astGraph: AstGraph, raw: String, newValue: JsValue) : Try[String] = {
    val literalinterfaceOption = literals.find(_.astType == astPrimitiveNode.nodeType)
    if (literalinterfaceOption.isDefined) {
      Try(literalinterfaceOption.get.mutator.apply(astPrimitiveNode, astGraph, raw, newValue, sourceParser, basicSourceInterface))
    } else {
      Failure(new Error("No Literal interface for node type: "+astPrimitiveNode.nodeType))
    }
  }

  override def generateFrom(value: JsValue) = Try {
    literals.head.generator(value, sourceParser, basicSourceInterface)
  }

}
