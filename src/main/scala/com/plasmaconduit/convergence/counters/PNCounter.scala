package com.plasmaconduit.convergence.counters

import com.twitter.algebird.{Monoid, Semigroup}
import com.twitter.algebird.Operators._
import scala.math.abs

final case class PNCounter(p: GCounter, n: GCounter) {
  def inc = copy(p = p.inc)
  def dec = copy(n = n.inc)
  def toLong = p.toLong - n.toLong
}

object PNCounter {

  def apply(num: Int): PNCounter = {
    if (num >= 0) PNCounter(GCounter(num), GCounter(0))
    else PNCounter(GCounter(0), GCounter(abs(num)))
  }

  def apply(num: Long): PNCounter = {
    PNCounter(GCounter(num), GCounter(0))
  }

  def apply(p: Long, n: Long): PNCounter = {
    PNCounter(GCounter(p), GCounter(n))
  }

  object PNCounterSemigroup extends Semigroup[PNCounter] {
    def plus(l: PNCounter, r: PNCounter): PNCounter = {
      PNCounter(l.p + r.p, l.n + l.p)
    }
  }

  class PNCounterMonoid(implicit semi: Semigroup[PNCounter]) extends Monoid[PNCounter] {
    def zero: PNCounter = PNCounter(GCounter(0), GCounter(0))
    def plus(l: PNCounter, r: PNCounter): PNCounter = semi.plus(l, r)
  }

  implicit val pnCounterSemigroup: Semigroup[PNCounter] = PNCounterSemigroup
  implicit val pnCounterMonoid: Monoid[PNCounter] = new PNCounterMonoid

}
