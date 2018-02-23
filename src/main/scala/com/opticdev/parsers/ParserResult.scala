package com.opticdev.parsers

/** Parser Results for all languages must be delivered in this format
  * @param graph the AstGraph of this file
  * @param language the language name
  * @param elapsedTime time of parsing operation in nanoseconds
  * */
case class ParserResult(graph: AstGraph, language: String, elapsedTime: Double)
