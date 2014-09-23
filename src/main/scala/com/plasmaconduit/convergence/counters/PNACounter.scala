package com.plasmaconduit.convergence.counters

import com.twitter.algebird.Operators._
import com.twitter.algebird.{Monoid, Semigroup}

final case class PNACounter(actors: Map[String, PNCounter]) {

  def inc(actor: String): PNACounter = {
    val counter = actors.get(actor).map(_.inc).getOrElse(PNCounter(1, 0))
    PNACounter(actors + Map(actor -> counter))
  }

  def dec(actor: String): PNACounter = {
    val counter = actors.get(actor).map(_.dec).getOrElse(PNCounter(0, 1))
    PNACounter(actors + Map(actor -> counter))
  }

  def toLong = actors.values.map(_.toLong).sum

}

object PNACounter {

  implicit object PNACounterSemigroup extends Semigroup[PNACounter] {
    def plus(l: PNACounter, r: PNACounter): PNACounter = {
      PNACounter(l.actors + r.actors)
    }
  }

  implicit object PNACounterMonoid extends Monoid[PNACounter] {
    def zero: PNACounter = PNACounter(Map())
    def plus(l: PNACounter, r: PNACounter): PNACounter = {
      PNACounterSemigroup.plus(l, r)
    }
  }

}