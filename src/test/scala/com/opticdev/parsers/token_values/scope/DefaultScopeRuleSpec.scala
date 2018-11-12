package com.opticdev.parsers.token_values.scope

import org.scalatest.FunSpec

class DefaultScopeRuleSpec extends FunSpec {

  it("can determine if two nodes have the same scope from same parentage") {
    assert(DefaultScopeRule.genericInScopeAlgo[String](
      Seq("A", "B", "C"),
      Seq("A", "B", "C")
    ))
  }

  it("can determine if two nodes with different parentage are still in have scope") {
    assert(DefaultScopeRule.genericInScopeAlgo[String](
      Seq("A", "B", "C"),
      Seq("A", "B")
    ))

    assert(DefaultScopeRule.genericInScopeAlgo[String](
      Seq("A", "B", "C", "D", "E", "F"),
      Seq("A", "B")
    ))
  }

  it("can determine if two noes are in different scopes") {
    assert(!DefaultScopeRule.genericInScopeAlgo[String](
      Seq("A"),
      Seq("A", "B")
    ))

    assert(!DefaultScopeRule.genericInScopeAlgo[String](
      Seq("A", "B", "C", "D"),
      Seq("A", "B", "E")
    ))
  }

}
