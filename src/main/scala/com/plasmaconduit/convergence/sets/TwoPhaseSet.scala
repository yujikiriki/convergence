package com.plasmaconduit.convergence.sets

import com.plasmaconduit.convergence.util.LivenessItem
import com.twitter.algebird.{Monoid, Semigroup}

final case class TwoPhaseSet[A](fresh: Set[A], tombs: Set[A]) {

  def insert(item: A): TwoPhaseSet[A] = {
    TwoPhaseSet((fresh + item).diff(tombs), tombs)
  }

  def remove(item: A): TwoPhaseSet[A] = {
    TwoPhaseSet(fresh.diff(Set(item)), tombs + item)
  }

  def insert(livenessItem: LivenessItem[A]): TwoPhaseSet[A] = livenessItem match {
    case Fresh(item)     => insert(item)
    case Tombstone(item) => remove(item)
  }

  def toSet = fresh.diff(tombs)

}

object TwoPhaseSet {

  def apply[A](items: Seq[A]): TwoPhaseSet[A] = {
    TwoPhaseSet(items.toSet, Set[A]())
  }

  def apply[A](items: A*): TwoPhaseSet[A] = {
    TwoPhaseSet(items:_*)
  }

  class TwoPhaseSetSemigroup[A] extends Semigroup[TwoPhaseSet[A]] {

    def plus(l: TwoPhaseSet[A], r: TwoPhaseSet[A]): TwoPhaseSet[A] = {
      val newTombs = l.tombs ++ r.tombs
      val newFresh = (l.fresh ++ r.fresh).diff(newTombs)
      TwoPhaseSet(newFresh, newTombs)
    }

  }

  class TwoPhaseSetMonoid[A](implicit semi: Semigroup[TwoPhaseSet[A]]) extends Monoid[TwoPhaseSet[A]] {

    def zero: TwoPhaseSet[A] = {
      TwoPhaseSet(Set.empty[A], Set.empty[A])
    }

    def plus(l: TwoPhaseSet[A], r: TwoPhaseSet[A]): TwoPhaseSet[A] = {
      semi.plus(l, r)
    }

  }

  implicit def implicitSemigroup[A] = new TwoPhaseSetSemigroup[A]

  implicit def implicitMonoid[A] = new TwoPhaseSetMonoid[A]

}