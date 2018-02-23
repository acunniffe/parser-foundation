package com.opticdev.parsers.graph.path

import com.opticdev.parsers.graph.CommonAstNode
import play.api.libs.json.{JsObject, JsValue}
import play.api.libs.json._

class PropertyPathWalker(val jsObject: JsObject) {

  def this(astPrimitiveNode: CommonAstNode) = {
    this(astPrimitiveNode.properties.as[JsObject])
  }

  def hasProperty(path: Seq[String]) : Boolean = getPropertySearch(path).isDefined

  def getProperty(path: Seq[String]) : Option[JsValue] = getPropertySearch(path)

  private def getPropertySearch(keyVec: Seq[String]) : Option[JsValue] = {

    var lookup : JsLookupResult = null

    keyVec.foreach(key=> {
      if (lookup == null) {
        lookup = jsObject \ key
      } else
        lookup = lookup \ key
    })

    lookup.toOption
  }

}
