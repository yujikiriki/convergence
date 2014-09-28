package com.plasmaconduit.convergence.registers

import com.plasmaconduit.convergence.util.CausalItem
import com.twitter.algebird.Semigroup
import Ordering.Implicits._

final case class LWWRegister[A](causalItem: CausalItem[A]) {

  def get = causalItem.item

}

object LWWRegister {

  class LWWRegisterSemigroup[A] extends Semigroup[LWWRegister[A]] {
    def plus(l: LWWRegister[A], r: LWWRegister[A]): LWWRegister[A] = {
      if (l.causalItem > r.causalItem) l
      else r
    }
  }

  implicit def lwwRegisterSemigroup[A]: Semigroup[LWWRegister[A]] = new LWWRegisterSemigroup[A]

}
