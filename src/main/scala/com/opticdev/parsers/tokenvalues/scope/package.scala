package com.opticdev.parsers.tokenvalues

import com.opticdev.parsers.{AstGraph, ParserBase}
import com.opticdev.parsers.graph.CommonAstNode

package object scope {
  trait ScopeRule {
    def inScope(node: CommonAstNode, candidate: CommonAstNode, astGraph: AstGraph, parserBase: ParserBase): Boolean
  }

  object DefaultScopeRule extends ScopeRule {
    override def inScope(node: CommonAstNode, candidate: CommonAstNode, astGraph: AstGraph, parser: ParserBase): Boolean = {
      import com.opticdev.parsers.graph.GraphImplicits._

      val nParents = node.parents(astGraph).reverse.filter(p => parser.blockNodeTypes.nodeTypes.contains(p.nodeType))
      val cParents = candidate.parents(astGraph).reverse.filter(p => parser.blockNodeTypes.nodeTypes.contains(p.nodeType))

      genericInScopeAlgo(nParents, cParents) && node.range.start > candidate.range.end
    }

    def genericInScopeAlgo[A](nodeParents: Seq[A], candidateParents: Seq[A]) : Boolean = {
      val firstDeviation = nodeParents.zipAll(candidateParents, null, null).find {
        case (a, b) => a != b
      }

      if (firstDeviation.isEmpty) {
        true
      } else {
        val firstDeviationIndex = nodeParents.indexOf(firstDeviation.get._1)
        candidateParents.size == firstDeviationIndex
      }
    }

  }

}
