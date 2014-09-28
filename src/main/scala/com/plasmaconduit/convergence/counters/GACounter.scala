package com.plasmaconduit.convergence.counters

import com.twitter.algebird._
import com.twitter.algebird.Operators._

final case class GACounter(actors: Map[String, GCounter]) {

  def inc(actor: String): GACounter = {
    val counter = actors.get(actor).map(_.inc).getOrElse(GCounter(1))
    GACounter(actors + Map(actor -> counter))
  }

  def toLong = actors.values.map(_.toLong).sum

}

object GACounter {

  object GACounterSemigroup extends Semigroup[GACounter] {
    def plus(l: GACounter, r: GACounter): GACounter = {
      GACounter(l.actors + r.actors)
    }
  }

  object GACounterMonoid extends Monoid[GACounter] {
    def zero: GACounter = GACounter(Map())
    def plus(l: GACounter, r: GACounter): GACounter = {
      GACounterSemigroup.plus(l, r)
    }
  }

  implicit val gaCounterSemigroup: Semigroup[GACounter] = GACounterSemigroup
  implicit val gaCounterMonoid: Monoid[GACounter] = GACounterMonoid

}