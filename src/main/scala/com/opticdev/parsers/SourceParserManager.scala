package com.opticdev.parsers

import java.net.URLClassLoader
import java.security.MessageDigest
import java.util.jar.JarFile
import collection.JavaConverters._

import com.opticdev.parsers.graph.NodeType

import scala.util.{Success, Try}

object SourceParserManager {

  private var parsers : Set[ParserBase] = Set()

  def hasParserFor(lang: String) : Boolean = installedParsers.exists(_.languageName == lang)

  def enableParser(instance: ParserBase) : ParserBase = {
    parsers = parsers + instance
    instance
  }

  def parserByLanguageName(lang: String): Option[ParserBase] = {
    parsers.find(_.languageName == lang)
  }

  def disableParser(instance: ParserBase) = {
    parsers = parsers.filterNot(_==instance)
  }

  def clearParsers = parsers = Set()
  def installedParsers : Set[ParserBase] = parsers
  def selectParserForFileName(name: String): Option[ParserBase] = {
    //@note does not support multiple things having same patterns
    parsers.find(i=> i.fileExtensions.exists(name.endsWith))
  }

  def parseString(contents: String, language: String, fileHash: String = "SPACE"): Try[ParserResult] = {
    val parser = parserByLanguageName(language)
    Try(if (parser.isDefined) {
      parser.get.parseString(contents)
    } else throw new Error("No parser found for "+language))
  }

  def parseStringWithProxies(contents: String, language: String, fileHash: String = "SPACE"): Try[ParserResult] = {
    val parser = parserByLanguageName(language)
    Try(if (parser.isDefined) {
      parser.get.parseStringWithProxies(contents).get
    } else throw new Error("No parser found for "+language))
  }

  def installParser(pathToParser: String) : Try[ParserBase] = {
    val parserTry = verifyParser(pathToParser)
    if (parserTry.isSuccess) {
      enableParser(parserTry.get)
      Success(parserTry.get)
    } else parserTry
  }

  def parserById(parserRef: ParserRef) : Option[ParserBase] = installedParsers.find(_.languageName == parserRef.languageName)

  def verifyParser(pathToParser: String) : Try[ParserBase] = {
    Try({
      val file = new java.io.File(pathToParser)
      if (file.exists() && file.canRead) {
        try {
          loadJar(file)
        } catch {
          case e: Exception => {
            println(e)
            throw new Error("Invalid Parser .jar")
          }
        }
      } else {
        throw new Error("Unable to install parser. File not found at " + pathToParser)
      }
    })
  }

  @throws(classOf[Exception])
  def loadJar(parserJar: java.io.File): ParserBase = {

    val jar = new JarFile(parserJar)
    val parserObjectOptions = jar.entries.asScala.find(_.getName.endsWith("OpticParser.class"))

    if (parserObjectOptions.isEmpty) throw new Error("OpticParser class not found in jar")

    val fullPath = parserObjectOptions.get.getName
    val className = fullPath.substring(0, fullPath.length - 6).replaceAll("/", ".")

    val child = new URLClassLoader(Array(parserJar.toURL), this.getClass().getClassLoader())

    val classToLoad = child.loadClass(className)
    val instance = classToLoad.newInstance().asInstanceOf[ParserBase]
    instance
  }


}
