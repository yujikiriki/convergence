package com.plasmaconduit.convergence.sets

import com.twitter.algebird.{Monoid, Semigroup}

final case class GSet[A](set: Set[A]) {

  def insert(item: A): GSet[A] = {
    GSet(set + item)
  }

  def toSet = set

}

object GSet {

  def apply[A](items: Seq[A]) = {
    GSet(items.toSet)
  }

  def apply[A](items: A*): GSet[A] = {
    GSet(items:_*)
  }

  class GSetSemigroup[A] extends Semigroup[GSet[A]] {

    def plus(l: GSet[A], r: GSet[A]): GSet[A] = {
      GSet(l.set ++ r.set)
    }

  }

  class GSetMonoid[A](implicit semi: Semigroup[GSet[A]]) extends Monoid[GSet[A]] {

    def zero: GSet[A] = {
      GSet(Set.empty[A])
    }

    def plus(l: GSet[A], r: GSet[A]): GSet[A] = {
      semi.plus(l, r)
    }

  }

  implicit def semigroupImplicit[A] = new GSetSemigroup[A]
  implicit def monoidImplicit[A] = new GSetMonoid[A]

}
