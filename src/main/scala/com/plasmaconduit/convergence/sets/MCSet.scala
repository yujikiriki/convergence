package com.plasmaconduit.convergence.sets

import com.twitter.algebird.{Monoid, Semigroup, Max}
import com.twitter.algebird.Operators._

final case class MCSet[A](map: Map[A, Max[Long]]) {

  def insert(item: A): MCSet[A] = {
    val n = map.get(item).map(n => if (n.get % 2 == 0) n.get + 1 else n.get).getOrElse(1L)
    MCSet(map + (item -> Max(n)))
  }

  def remove(item: A): MCSet[A] = {
    val n = map.get(item).map(n => if (n.get % 2 == 0) n.get else n.get + 1).getOrElse(0L)
    MCSet(map + (item -> Max(n)))
  }

  def toSet = map.filter(n => n._2.get % 2 != 0).map(_._1).toSet

}

object MCSet {

  def apply[A](items: Seq[A]): MCSet[A] = {
    val map = items.foldLeft(Map[A, Max[Long]]()) {(m, n) =>
      m + (n -> Max(0))
    }
    MCSet(map)
  }

  class MCSetSemigroup[A] extends Semigroup[MCSet[A]] {
    def plus(l: MCSet[A], r: MCSet[A]): MCSet[A] = {
      MCSet(l.map + r.map)
    }
  }

  class MCSetMonoid[A](implicit semi: Semigroup[MCSet[A]]) extends Monoid[MCSet[A]] {
    def zero: MCSet[A] = MCSet(Map[A, Max[Long]]())
    def plus(l: MCSet[A], r: MCSet[A]): MCSet[A] = semi.plus(l, r)
  }

  implicit def mcSetSemigroup[A]: Semigroup[MCSet[A]] = new MCSetSemigroup[A]
  implicit def mcSetMonoid[A]: Monoid[MCSet[A]] = new MCSetMonoid[A]

}
