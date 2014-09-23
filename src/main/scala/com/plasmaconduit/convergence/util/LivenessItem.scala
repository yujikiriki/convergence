package com.plasmaconduit.convergence.util

sealed trait LivenessItem[A]
final case class Fresh[A](item: A) extends LivenessItem[A]
final case class Tombstone[A](item: A) extends LivenessItem[A]
