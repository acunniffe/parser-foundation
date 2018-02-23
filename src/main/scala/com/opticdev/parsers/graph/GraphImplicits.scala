package com.opticdev.parsers.graph

import com.opticdev.parsers.AstGraph

object GraphImplicits {

  implicit class GraphInstance(graph: AstGraph) {
    def root : Option[CommonAstNode] = graph
      .nodes
      .filter(i=> i.value.isAstNode && i.value.asInstanceOf[CommonAstNode].parent(graph).isEmpty)
      .map(_.value.asInstanceOf[CommonAstNode])
      .headOption
  }

}
