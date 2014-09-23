package com.plasmaconduit.convergence.sets

import com.plasmaconduit.convergence.util.{ARCausalPair, CausalItem}
import com.twitter.algebird.Operators._
import com.twitter.algebird._

final case class LWWSet[A](map: Map[A, ARCausalPair]) {

  def insert(causalItem: CausalItem[A]): LWWSet[A] = {
    LWWSet(map + Map(causalItem.item -> ARCausalPair.fromAddTime(causalItem.time)))
  }

  def toSet = map.filter(n => n._2.recentAdd).keys.toSet

}

object LWWSet {

  def apply[A](items: Seq[CausalItem[A]]): LWWSet[A] = {
    val map = items.foldLeft(Map[A, ARCausalPair]()) {(m, n) =>
      m + (n.item -> ARCausalPair.fromAddTime(n.time))
    }
    LWWSet(map)
  }

  def apply[A](items: Seq[A]): LWWSet[A] = {
    apply(items.map(n => CausalItem(n)))
  }

  def apply[A](items: CausalItem[A]*): LWWSet[A] = {
    LWWSet(items:_*)
  }

  def apply[A](items: A*): LWWSet[A] = {
    LWWSet(items.map(n => CausalItem(n)))
  }

  class LWWSetSemiGroup[A] extends Semigroup[LWWSet[A]] {
    def plus(l: LWWSet[A], r: LWWSet[A]): LWWSet[A] = {
      LWWSet(r.map + l.map)
    }
  }

  class LWWSetMonoid[A](implicit semi: Semigroup[LWWSet[A]]) extends Monoid[LWWSet[A]] {
    def zero: LWWSet[A] = LWWSet(Map.empty[A, ARCausalPair])
    def plus(l: LWWSet[A], r: LWWSet[A]): LWWSet[A] = {
      semi.plus(l, r)
    }
  }

  implicit def implicitSemigroup[A] = new LWWSetSemiGroup[A]
  implicit def implicitMonoid[A] = new LWWSetMonoid[A]

}

