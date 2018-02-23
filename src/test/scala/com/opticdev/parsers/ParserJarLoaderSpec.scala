package com.opticdev.parsers

import org.scalatest.FunSpec

class ParserJarLoaderSpec extends FunSpec {

  describe("Parser Jar Loader") {

    val parserJar = new java.io.File("src/test/resources/fake-parser-0.1.0.jar")
    val invalidJar = new java.io.File("src/test/resources/invalid-fake.jar")

    it("Fails for invalid jars") {
      assertThrows[Exception] {
        SourceParserManager.loadJar(invalidJar)
      }
    }

    it("Finds the object named Parser in the classpath") {
      val parser = SourceParserManager.loadJar(parserJar)
      assert(parser.languageName == "Fake")
    }


  }

}
