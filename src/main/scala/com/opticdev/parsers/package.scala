package com.opticdev

import com.opticdev.parsers.graph.{AstType, BaseNode, CommonAstNode}
import scalax.collection.edge.LkDiEdge
import scalax.collection.mutable.Graph

package object parsers {
  type AstGraph = Graph[BaseNode, LkDiEdge]

  case class BlockNodeDesc(nodeType: AstType, propertyPath: String)
  case class BlockNodeTypes(seq: BlockNodeDesc*) {
    lazy val nodeTypes = seq.map(_.nodeType).toSet
    def getPropertyPath(nodeType: AstType): Option[String] = seq.find(_.nodeType == nodeType).map(_.propertyPath)
  }

  type EnterOnPostProcessor = (AstType, AstGraph, CommonAstNode) => (Set[AstType], CommonAstNode)

}
