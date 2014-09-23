package com.plasmaconduit.convergence.util

final case class CausalItem[A](item: A, time: CausalTime)

object CausalItem {

  def apply[A](item: A): CausalItem[A] = {
    CausalItem(item, CausalTime("_Global", 0))
  }

  class CausalItemOrdering[A](implicit ord: Ordering[CausalItem[A]]) extends Ordering[CausalItem[A]] {
    def compare(x: CausalItem[A], y: CausalItem[A]): Int = {
      ord.compare(x, y)
    }
  }

  implicit def implicitOrdering[A] = new CausalItemOrdering[A]

}