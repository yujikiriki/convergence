package com.plasmaconduit.convergence.sets

import com.plasmaconduit.convergence.util._
import com.twitter.algebird.Operators._
import com.twitter.algebird._
import Ordering.Implicits._

final case class RoshiSet[A](fresh: LWWSet[A], tombs: LWWSet[A]) {

  def insert(causalItem: CausalItem[A]): RoshiSet[A] = {
    tombs.map.get(causalItem.item) match {
      case None    => RoshiSet(fresh.insert(causalItem), tombs)
      case Some(t) =>
        if (t.get >= causalItem) this
        else RoshiSet(fresh.insert(causalItem), LWWSet(tombs.map - causalItem.item))
    }
  }

  def remove(causalItem: CausalItem[A]): RoshiSet[A] = {
    if (fresh.map.get(causalItem.item).filter(n => n.get < causalItem).isDefined) {
      RoshiSet(LWWSet(fresh.map - causalItem.item), tombs.insert(causalItem))
    } else {
      RoshiSet(fresh, tombs.insert(causalItem))
    }
  }

  def insert(roshiItem: LivenessItem[CausalItem[A]]): RoshiSet[A] = roshiItem match {
    case Fresh(item)     => insert(item)
    case Tombstone(item) => remove(item)
  }

  def toSet = fresh.map.values.foldLeft(Set[A]()) {(m, n) =>
    tombs.map.get(n.get.item) match {
      case None => m + n.get.item
      case Some(t) =>
        if (t.get >= n.get) m
        else m + n.get.item
    }
  }

}

object RoshiSet {

  def apply[A](items: Seq[LivenessItem[CausalItem[A]]])(implicit m: Monoid[LWWSet[A]]): RoshiSet[A] = {
    items.foldLeft(RoshiSet(m.zero, m.zero)) {(m, n) => m.insert(n) }
  }

  class RoshiSetSemiGroup[A](implicit m: Monoid[LWWSet[A]]) extends Semigroup[RoshiSet[A]] {
    def plus(l: RoshiSet[A], r: RoshiSet[A]): RoshiSet[A] = {
      val newFresh = l.fresh + r.fresh
      val newTombs = l.tombs + r.tombs
      newTombs.map.values.foldLeft(RoshiSet(newFresh, m.zero)) {(m,n) =>
        m.insert(Tombstone(n.get))
      }
    }
  }

  class RoshiSetMonoid[A](implicit semi: Semigroup[RoshiSet[A]], m: Monoid[LWWSet[A]]) extends Monoid[RoshiSet[A]] {
    def zero: RoshiSet[A] = RoshiSet(m.zero, m.zero)
    def plus(l: RoshiSet[A], r: RoshiSet[A]): RoshiSet[A] = semi.plus(l, r)
  }

  implicit def implicitSemigroup[A] = new RoshiSetSemiGroup[A]
  implicit def implicitMonoid[A] = new RoshiSetMonoid[A]

}