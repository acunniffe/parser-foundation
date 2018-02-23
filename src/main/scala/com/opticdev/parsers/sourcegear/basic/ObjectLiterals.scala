package com.opticdev.parsers.sourcegear.basic

import com.opticdev.parsers.{AstGraph, ParserBase}
import com.opticdev.parsers.graph.CommonAstNode
import play.api.libs.json.JsValue

import scala.util.{Failure, Try}

abstract class ObjectLiterals extends NodeInterface

case class ObjectLiteralsInterfaces(objectLiterals: ObjectLiterals*)(implicit basicSourceInterface: BasicSourceInterface, sourceParser: ParserBase) extends NodeInterfaceGroup {
  val objectLiteralsType = objectLiterals.map(_.astType).toSet

  def interfaceFor(astPrimitiveNode: CommonAstNode): Boolean = objectLiteralsType.contains(astPrimitiveNode.nodeType)

  def parseNode(astPrimitiveNode: CommonAstNode, astGraph: AstGraph, fileContents: String): Try[JsValue] = {
    val interfaceOptions = objectLiterals.find(_.astType == astPrimitiveNode.nodeType)
    if (interfaceOptions.isDefined) {
      Try(interfaceOptions.get.parser.apply(astPrimitiveNode, astGraph, fileContents, basicSourceInterface))
    } else {
      Failure(new Error("No Object Literal interface for node type: "+astPrimitiveNode.nodeType))
    }
  }

  def mutateNode(astPrimitiveNode: CommonAstNode, astGraph: AstGraph, fileContents: String, newValue: JsValue) : Try[String] = {
    val interfaceOptions = objectLiterals.find(_.astType == astPrimitiveNode.nodeType)
    if (interfaceOptions.isDefined) {
      Try(interfaceOptions.get.mutator.apply(astPrimitiveNode, astGraph, fileContents, newValue, sourceParser, basicSourceInterface))
    } else {
      Failure(new Error("No Object Literal interface for node type: "+astPrimitiveNode.nodeType))
    }
  }

  override def generateFrom(value: JsValue) = Try {
    objectLiterals.head.generator(value, sourceParser, basicSourceInterface)
  }
}
