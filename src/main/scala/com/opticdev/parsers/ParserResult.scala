package com.opticdev.parsers

import com.opticdev.common.graph.AstGraph

/** Parser Results for all languages must be delivered in this format
 *
  * @param graph the AstGraph of this file
  * @param language the language name
  * @param elapsedTime time of parsing operation in nanoseconds
  * @param parserBase the parser that was used on this file
  * */
case class ParserResult(graph: AstGraph, language: String, elapsedTime: Double, parserBase: ParserBase)
