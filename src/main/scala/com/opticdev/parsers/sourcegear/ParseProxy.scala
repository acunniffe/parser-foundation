package com.opticdev.parsers.sourcegear

import com.opticdev.parsers.{ParserBase, ParserResult}

import scala.util.Try

abstract class ParseProxy {
  def shouldUse(input: String, parser: ParserBase) : Boolean
  def parse(input: String, parser: ParserBase) : Try[ParserResult]
}
