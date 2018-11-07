package com.opticdev.parsers.sourcegear.basic

import com.opticdev.parsers.ParserBase
import play.api.libs.json.JsValue

abstract class BasicSourceInterface(implicit sourceParser: ParserBase) {

  implicit val basicSourceInterface: BasicSourceInterface = this

  val literals: LiteralInterfaces = LiteralInterfaces()
  val tokens: TokenInterfaces = TokenInterfaces()
  val objectLiterals: ObjectLiteralsInterfaces = ObjectLiteralsInterfaces()
  val arrayLiterals: ArrayLiteralsInterfaces = ArrayLiteralsInterfaces()
}
