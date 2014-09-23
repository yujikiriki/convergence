package com.plasmaconduit.convergence.sets

import com.plasmaconduit.convergence.util.{LivenessItem, Tombstone, Fresh, CausalTime}
import com.twitter.algebird.Operators._
import com.twitter.algebird.Semigroup

final case class ORSet[A](time: CausalTime, fresh: Map[A, Set[CausalTime]], tombs: Map[A, Set[CausalTime]]) {

  def insert(item: A): ORSet[A] = {
    ORSet(time.next, fresh + Map(item -> Set(time)), tombs)
  }

  def remove(item: A): ORSet[A] = {
    val newTombs = fresh.get(item).map(n => Map(item -> n)).getOrElse(Map())
    ORSet(time.next, fresh, tombs + newTombs)
  }

  def insert(livenessItem: LivenessItem[A]): ORSet[A] = livenessItem match {
    case Fresh(item)     => insert(item)
    case Tombstone(item) => remove(item)
  }

  def toSet = fresh
    .filter(n => tombs.get(n._1).map(m => n._2.diff(m)).size > 0)
    .map(n => n._1)

}

object ORSet {

  def apply[A](actor: String, items: Seq[A]): ORSet[A] = {
    items.foldLeft(ORSet[A](CausalTime(actor), Map(), Map())) {(m, n) =>
      m.insert(n)
    }
  }

  class ORSetSemigroup[A] extends Semigroup[ORSet[A]] {
    def plus(l: ORSet[A], r: ORSet[A]): ORSet[A] = {
      ORSet(l.time, l.fresh + r.fresh, l.tombs + r.tombs)
    }
  }

  implicit def implicitSemigroup[A] = new ORSetSemigroup[A]

}
