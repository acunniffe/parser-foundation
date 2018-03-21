package com.opticdev.parsers.sourcegear.basic

import com.opticdev.parsers.sourcegear.basic.ObjectLiteralValueFormat.ObjectLiteralValueFormat
import play.api.libs.json._

object ObjectLiteralValueFormat extends Enumeration {
  type ObjectLiteralValueFormat = Value
  val Token, Code, Primitive = Value
  def formatForJson(jsValue: JsValue) : ObjectLiteralValueFormat = jsValue match {
    case i: JsString => Primitive
    case i: JsNumber => Primitive
    case i: JsBoolean => Primitive
    case JsNull => Primitive

    case i: JsArray => Primitive

    case i: JsObject => {
      val valueFormat = i \ "_valueFormat"
      if (valueFormat.isDefined) {
        valueFormat.get match {
          case JsString("token") => Token
          case JsString("code") => Code
          case _ => Primitive
        }
      } else Primitive
    }
  }

  def valueForJson(jsValue: JsValue) : ObjectLiteralValue = {
    val format = formatForJson(jsValue)

    format match {
      case Primitive => ObjectLiteralValue(jsValue, format)
      case i: ObjectLiteralValueFormat => ObjectLiteralValue((jsValue.as[JsObject] \ "value").get, i)
    }
  }

}

case class ObjectLiteralValue(value: JsValue, format: ObjectLiteralValueFormat) {
  import ObjectLiteralValueFormat._
  require(!(format != Primitive && !value.isInstanceOf[JsString]), "Code and Token Formats must include a value field of type 'string'")
}