package com.opticdev.parsers.sourcegear.basic

import com.opticdev.parsers.{AstGraph, ParserBase}
import com.opticdev.parsers.graph.CommonAstNode
import play.api.libs.json.JsValue

import scala.util.{Failure, Try}

abstract class ArrayLiterals extends NodeInterface

case class ArrayLiteralsInterfaces(arrayLiterals: ArrayLiterals*)(implicit basicSourceInterface: BasicSourceInterface, sourceParser: ParserBase) extends NodeInterfaceGroup {
  val arrayLiteralsType = arrayLiterals.map(_.astType).toSet

  def interfaceFor(astPrimitiveNode: CommonAstNode): Boolean = arrayLiteralsType.contains(astPrimitiveNode.nodeType)

  def parseNode(astPrimitiveNode: CommonAstNode, astGraph: AstGraph, fileContents: String): Try[JsValue] = {
    val interfaceOptions = arrayLiterals.find(_.astType == astPrimitiveNode.nodeType)
    if (interfaceOptions.isDefined) {
      Try(interfaceOptions.get.parser.apply(astPrimitiveNode, astGraph, fileContents, basicSourceInterface))
    } else {
      Failure(new Error("No Array Literal interface for node type: "+astPrimitiveNode.nodeType))
    }
  }

  def mutateNode(astPrimitiveNode: CommonAstNode, astGraph: AstGraph, fileContents: String, newValue: JsValue) : Try[String] = {
    val interfaceOptions = arrayLiterals.find(_.astType == astPrimitiveNode.nodeType)
    if (interfaceOptions.isDefined) {
      Try(interfaceOptions.get.mutator.apply(astPrimitiveNode, astGraph, fileContents, newValue, sourceParser, basicSourceInterface))
    } else {
      Failure(new Error("No Array Literal interface for node type: "+astPrimitiveNode.nodeType))
    }
  }

  override def generateFrom(value: JsValue) = Try {
    arrayLiterals.head.generator(value, sourceParser, basicSourceInterface)
  }
}
