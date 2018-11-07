package com.opticdev.parsers

import com.opticdev.parsers.graph.AstType
import com.opticdev.parsers.rules.{ChildrenRuleTypeEnum, ParserChildrenRule}
import com.opticdev.parsers.sourcegear.ParseProxy
import com.opticdev.parsers.sourcegear.advanced.MarvinSourceInterface
import com.opticdev.parsers.tokenvalues.TokenValueHandler
import sourcegear.basic.{BasicSourceInterface, LiteralInterfaces, TokenInterfaces}

import scala.util.Try

/**
  * Trait implemented by all Optic Language Parsers
  *
  * - Provides parsing from raw code to AST Graphs
  * - Defines meta information to describe their parser
  * - Defines simple source interfaces for Literals, Tokens, Object Literals & Array Literals
  * - Includes more complex source interfaces learned by Marvin
  */

trait ParserBase {

  /** The name of your language, must be alphanumeric  */
  def languageName : String
  require(languageName.matches("^[A-Za-z_][A-Za-z\\d]*$"), "Language name must be alphanumeric")

  /** The semantic version of this parser package  */
  def parserVersion: String

  /** A set of file extensions this parser should try to parse */
  def fileExtensions : Set[String]

  /** The AST Type for root node for this language */
  def programNodeType : AstType


  /** A list of the block nodes in this language with their AST Property Path to children
    * Example for Javascript
    *  BlockNodeTypes(
    *    BlockNodeDesc(AstType("BlockStatement", languageName), "body"),
    *    BlockNodeDesc(AstType("Program", languageName), "body")
    *  )
    * */
  def blockNodeTypes : BlockNodeTypes


  /** The AST Type for the token node in this language and a path to its value
    * Example for Javascript
    *  IdentifierNodeDesc(AstType("Identifier", languageName), Seq("name"))
    * */
  def identifierNodeDesc : IdentifierNodeDesc

  /** This language's inline comment syntax  */
  def inlineCommentPrefix : String = "//"

  /** Manually programmed interface for literals, tokens, object literals & arrays */
  def basicSourceInterface : BasicSourceInterface

  /** Learned interface for all the rest of the nodes */
  def marvinSourceInterface : MarvinSourceInterface

  /** Parse string and return graph */
  def parseString(contents: String): ParserResult

  final def parserRef = ParserRef(languageName, parserVersion)

  override def equals(obj: scala.Any): Boolean = obj match {
    case p: ParserBase => parserRef == p.parserRef
    case _=> super.equals(obj)
  }

  /** Parse string with proxies and return graph. Should only be used during sourcegear compilation stages */
  def parseStringWithProxies(contents: String) : Try[ParserResult] = {
    sourcegearParseProxies
      .find(_.shouldUse(contents, this)).map(_.parse(contents, this))
      .getOrElse(Try(parseString(contents)))
  }

  /** Parser Proxies */
  def sourcegearParseProxies : Vector[ParseProxy] = Vector()

  /** Token Value Handler */
  def tokenValueHandler : TokenValueHandler

  /** Paths relative to project root that should never be parsed. */
  def excludedPaths: Seq[String] = Seq.empty[String]

  /** Optional Post Processors for calculating Node Types **/
  def enterOnPostProcessor: Map[AstType, EnterOnPostProcessor] = Map.empty

  /** Optional Default Children Rules Applied By Node Type **/
  def defaultChildrenRules: Map[AstType, Vector[ParserChildrenRule]] = Map.empty
}
