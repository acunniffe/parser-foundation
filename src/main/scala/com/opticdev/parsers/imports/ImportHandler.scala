package com.opticdev.parsers.imports

import better.files.File

trait ImportHandler {
  val internalAbstractions: Seq[String]
  def importsFromModels(models: Set[ImportModel])(implicit inFile: File, projectDirectory: String, debug: Boolean = false): Set[StandardImport]
}

object DefaultImportHandler extends ImportHandler {
  override val internalAbstractions: Seq[String] = Seq()
  override def importsFromModels(models: Set[ImportModel])(implicit inFile: File, projectDirectory: String, debug: Boolean = false): Set[StandardImport] = Set()
}
