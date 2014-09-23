package com.plasmaconduit.convergence.util

final case class CausalTime(actor: String, time: Long) {
  def next = copy(time = time + 1)
}

object CausalTime {

  def apply(actor: String): CausalTime = {
    CausalTime(actor, 0)
  }

  implicit object CausalTimeOrdering extends Ordering[CausalTime] {
    def compare(x: CausalTime, y: CausalTime): Int = {
      val result = Ordering.String.compare(x.actor, y.actor)
      if (result == 0) Ordering.Long.compare(x.time, y.time)
      else result
    }
  }

}