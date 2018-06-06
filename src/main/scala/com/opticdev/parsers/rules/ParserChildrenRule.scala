package com.opticdev.parsers.rules

import com.opticdev.parsers.graph.AstType

trait ParserChildrenRule extends Rule{
  def rule: ChildrenRuleTypeEnum
  override val isChildrenRule = true
}

case class AllChildrenRule(rule: ChildrenRuleTypeEnum) extends ParserChildrenRule
case class SpecificChildrenRule(edgeType: String, rule: ChildrenRuleTypeEnum) extends ParserChildrenRule
