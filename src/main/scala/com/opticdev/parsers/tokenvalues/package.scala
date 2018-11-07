package com.opticdev.parsers

import com.opticdev.parsers.graph.{AstType, BaseNode, CommonAstNode}
import com.opticdev.parsers.tokenvalues.scope.{DefaultScopeRule, ScopeRule}

import scala.util.Try

package object tokenvalues {

  sealed trait TokenRegistryEntry {
    def key: String
    def model: BaseNode
    def isInternal = this.isInstanceOf[Internal]
    def isExternal = this.isInstanceOf[External]

    def inScope(node: CommonAstNode, candidate: CommonAstNode, astGraph: AstGraph, parserBase: ParserBase): Boolean
  }

  case class External(key: String, model: BaseNode) extends TokenRegistryEntry {
    override def inScope(node: CommonAstNode, candidate: CommonAstNode, astGraph: AstGraph, parserBase: ParserBase): Boolean = true
  }
  case class Internal(key: String, parentNode: CommonAstNode, model: BaseNode, scopeRule: ScopeRule = DefaultScopeRule) extends TokenRegistryEntry {
    override def inScope(node: CommonAstNode, candidate: CommonAstNode, astGraph: AstGraph, parserBase: ParserBase): Boolean = scopeRule.inScope(node, candidate, astGraph, parserBase)
  }

  type EfficientWithinToken = (CommonAstNode, AstGraph) => Boolean
  type ExtractTokenValue = (CommonAstNode, AstGraph, BaseNode) => Try[TokenRegistryEntry]

  case class TokenRule(withinToken: EfficientWithinToken, extractVariable: ExtractTokenValue)

}
