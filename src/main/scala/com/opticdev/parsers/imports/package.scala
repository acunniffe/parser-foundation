package com.opticdev.parsers

import better.files.File
import play.api.libs.json.JsObject

package object imports {
  case class ImportModel(abstractionId: String, value: JsObject)

  case class StandardImport(
    local: String,
    imported: Option[String], //empty if 'default'
    file: File)

  def AliasImport(alias: (String, String), file: File) = StandardImport(alias._2, Some(alias._1), file)

}
