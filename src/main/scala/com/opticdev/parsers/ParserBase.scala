package com.opticdev.parsers

import com.opticdev.parsers.graph.AstType
import com.opticdev.parsers.sourcegear.advanced.MarvinSourceInterface
import sourcegear.basic.{BasicSourceInterface, LiteralInterfaces, TokenInterfaces}

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
  val languageName : String
  require(languageName.matches("^[A-Za-z_][A-Za-z\\d]*$"), "Language name must be alphanumeric")

  /** The semantic version of this parser package  */
  val parserVersion: String

  /** A set of file extensions this parser should try to parse */
  val fileExtensions : Set[String]

  /** The AST Type for root node for this language */
  val programNodeType : AstType


  /** A list of the block nodes in this language with their AST Property Path to children
    * Example for Javascript
    *  BlockNodeTypes(
    *    BlockNodeDesc(AstType("BlockStatement", languageName), "body"),
    *    BlockNodeDesc(AstType("Program", languageName), "body")
    *  )
    * */
  val blockNodeTypes : BlockNodeTypes


  /** The AST Type for the token node in this language and a path to its value
    * Example for Javascript
    *  IdentifierNodeDesc(AstType("Identifier", languageName), Seq("name"))
    * */
  val identifierNodeDesc : IdentifierNodeDesc

  /** This language's inline comment syntax  */
  val inlineCommentPrefix : String = "//"

  /** Manually programmed interface for literals, tokens, object literals & arrays */
  val basicSourceInterface : BasicSourceInterface

  /** Learned interface for all the rest of the nodes */
  val marvinSourceInterface : MarvinSourceInterface

  /** The AST Type for the token node in this language and a path to its value
    * @param contents raw code to parse into a ParserResult object
    * */
  def parseString(contents: String): ParserResult


  final def parserRef = ParserRef(languageName, parserVersion)
}
