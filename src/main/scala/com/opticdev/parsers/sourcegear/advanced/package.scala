package com.opticdev.parsers.sourcegear

package object advanced {

  trait MarvinSourceInterface {
    val mapping : Map[String, BaseAstMutator]
  }

  trait BaseAstMutator

}
