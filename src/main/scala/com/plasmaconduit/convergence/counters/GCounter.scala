package com.plasmaconduit.convergence.counters

import scala.math.max
import com.twitter.algebird.{Monoid, Semigroup}

final case class GCounter(num: Long) {
  def inc = GCounter(num + 1)
  def toLong = num
}

object GCounter {

  def apply(num: Int): GCounter = {
    GCounter(num.toLong)
  }

  implicit object GCounterSemigroup extends Semigroup[GCounter] {
    def plus(l: GCounter, r: GCounter): GCounter = {
      GCounter(max(l.num, r.num))
    }
  }

  class GCounterMonoid(implicit semi: Semigroup[GCounter]) extends Monoid[GCounter] {
    def zero: GCounter = GCounter(0)
    def plus(l: GCounter, r: GCounter): GCounter = semi.plus(l, r)
  }

  implicit def implicitMonoid = new GCounterMonoid()

}

