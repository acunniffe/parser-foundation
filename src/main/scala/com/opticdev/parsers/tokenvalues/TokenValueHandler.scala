package com.opticdev.parsers.tokenvalues

import com.opticdev.common.graph._
import com.opticdev.parsers.ParserBase

trait TokenValueHandler {
  val tokenRules : Seq[TokenRule]

  def evaluate(astNode: CommonAstNode, astGraph: AstGraph, modelNode: BaseNode): Option[TokenRegistryEntry] =
    tokenRules.find(_.withinToken.apply(astNode, astGraph))
      .flatMap(_.extractVariable.apply(astNode, astGraph, modelNode).toOption)
}

object TokenValuePredicates {
  def parentIs(astType: AstType, nodeKey: String)(implicit node: CommonAstNode, astGraph: AstGraph): Boolean = {
    val parent = node.labeledDependencies(astGraph).find(_._2.isAstNode())
    if (parent.isDefined) {
      val child = parent.get._1.asInstanceOf[Child]
      parent.get._2.isASTType(astType) && child.typ == nodeKey
    } else {
      false
    }
  }

}
