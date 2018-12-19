package com.opticdev.parsers

import better.files.File
import com.opticdev.common.graph.{AstGraph, AstType, BaseNode, CommonAstNode}
import com.opticdev.parsers.token_values.scope.{DefaultScopeRule, ScopeRule}

import scala.util.Try

package object token_values {

  sealed trait TokenRegistryEntry {
    def key: String
    def model: BaseNode
    def isInternal = false
    def isExternal = false
    def isImported = false

    def inScope(node: CommonAstNode, candidate: CommonAstNode, astGraph: AstGraph, parserBase: ParserBase): Boolean
  }

  case class External(key: String, model: BaseNode) extends TokenRegistryEntry {
    override def inScope(node: CommonAstNode, candidate: CommonAstNode, astGraph: AstGraph, parserBase: ParserBase): Boolean = true
    override def isExternal: Boolean = true
  }
  case class Internal(key: String, parentNode: CommonAstNode, model: BaseNode, scopeRule: ScopeRule = DefaultScopeRule) extends TokenRegistryEntry {
    override def inScope(node: CommonAstNode, candidate: CommonAstNode, astGraph: AstGraph, parserBase: ParserBase): Boolean = scopeRule.inScope(node, candidate, astGraph, parserBase)
    override def isInternal: Boolean = true
  }

  case class Imported[A](key: String, model: BaseNode, file: File, context: A) extends TokenRegistryEntry {
    override def isImported = true
    override def inScope(node: CommonAstNode, candidate: CommonAstNode, astGraph: AstGraph, parserBase: ParserBase): Boolean =
      DefaultScopeRule.inScope(node, candidate, astGraph, parserBase)
  }

  type EfficientWithinToken = (CommonAstNode, AstGraph) => Boolean
  type ExtractTokenValue = (CommonAstNode, AstGraph, BaseNode) => Try[TokenRegistryEntry]

  case class TokenRule(withinToken: EfficientWithinToken, extractVariable: ExtractTokenValue)

}
