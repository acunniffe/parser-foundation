package com.opticdev.parsers.utils

object Profiling {
  case class TimedResult[R](elapsedTime: Long, result: R)
  def time[R](block: => R): TimedResult[R] = {
    val t0 = System.nanoTime()
    val result = block
    val t1 = System.nanoTime()
    TimedResult( t1 - t0, result )
  }
}