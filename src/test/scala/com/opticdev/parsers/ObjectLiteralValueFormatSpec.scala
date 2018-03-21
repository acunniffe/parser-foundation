package com.opticdev.parsers

import com.opticdev.parsers.sourcegear.basic.{ObjectLiteralValue, ObjectLiteralValueFormat}
import org.scalatest.FunSpec
import play.api.libs.json._

class ObjectLiteralValueFormatSpec extends FunSpec {

  import ObjectLiteralValueFormat._
  describe("parsing format from json value") {

    it("returns primitive for string") {
      assert(ObjectLiteralValueFormat.formatForJson(Json.parse(""""test"""")) == Primitive)
    }

    it("returns primitive for number") {
      assert(ObjectLiteralValueFormat.formatForJson(Json.parse("12")) == Primitive)
    }

    it("returns primitive for boolean") {
      assert(ObjectLiteralValueFormat.formatForJson(Json.parse("false")) == Primitive)
    }

    it("returns primitive for null") {
      assert(ObjectLiteralValueFormat.formatForJson(Json.parse("null")) == Primitive)
    }

    it("returns primitive for object") {
      assert(ObjectLiteralValueFormat.formatForJson(Json.parse("{}")) == Primitive)
    }

    it("returns primitive for array") {
      assert(ObjectLiteralValueFormat.formatForJson(Json.parse("[]")) == Primitive)
    }

    //with hidden fields
    it("returns token for object with _valueFormat field = 'token'") {
      assert(ObjectLiteralValueFormat.formatForJson(Json.parse("""{"_valueFormat": "token"}""")) == Token)
    }

    it("returns code for object with _valueFormat field = 'code'") {
      assert(ObjectLiteralValueFormat.formatForJson(Json.parse("""{"_valueFormat": "code"}""")) == Code)
    }
  }

  describe("parsing value from json value") {
    it("returns primitive for string") {
      assert(ObjectLiteralValueFormat.valueForJson(Json.parse(""""test"""")) == ObjectLiteralValue(JsString("test"), Primitive))
    }

    it("returns primitive for number") {
      assert(ObjectLiteralValueFormat.valueForJson(Json.parse("12")) == ObjectLiteralValue(JsNumber(12), Primitive))
    }

    it("returns primitive for boolean") {
      assert(ObjectLiteralValueFormat.valueForJson(Json.parse("false")) == ObjectLiteralValue(JsBoolean(false), Primitive))
    }

    it("returns primitive for null") {
      assert(ObjectLiteralValueFormat.valueForJson(Json.parse("null")) == ObjectLiteralValue(JsNull, Primitive))
    }

    it("returns primitive for object") {
      assert(ObjectLiteralValueFormat.valueForJson(Json.parse("{}")) == ObjectLiteralValue(JsObject.empty, Primitive))
    }

    it("returns primitive for array") {
      assert(ObjectLiteralValueFormat.valueForJson(Json.parse("[]")) == ObjectLiteralValue(JsArray.empty, Primitive))
    }

    //with hidden fields
    it("returns token for object with _valueFormat field = 'token'") {
      assert(ObjectLiteralValueFormat.valueForJson(Json.parse("""{"_valueFormat": "token", "value": "response"}""")) == ObjectLiteralValue(JsString("response"), Token))
    }

    it("returns code for object with _valueFormat field = 'code'") {
      assert(ObjectLiteralValueFormat.valueForJson(Json.parse("""{"_valueFormat": "code", "value": "response.this.that"}""")) == ObjectLiteralValue(JsString("response.this.that"), Code))
    }
  }

}
