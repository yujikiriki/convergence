package com.plasmaconduit.convergence.util

import com.twitter.algebird.Semigroup
import Ordering.Implicits._

final case class ARCausalPair(a: CausalTime, r: CausalTime) {
  def recentAdd = a > r
  def recentRemove = a <= r
  def higherTime = a max r
}

object ARCausalPair {

  def fromAddTime(time: CausalTime): ARCausalPair = {
    ARCausalPair(time, time.copy(time = 0))
  }

  def fromRemoveTime(time: CausalTime): ARCausalPair = {
    ARCausalPair(time.copy(time = 0), time)
  }

  implicit object ARCausalPairSemigroup extends Semigroup[ARCausalPair] {
    def plus(l: ARCausalPair, r: ARCausalPair): ARCausalPair = {
      ARCausalPair(l.a max r.a, l.r max r.r)
    }
  }

}
