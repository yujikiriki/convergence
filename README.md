Convergence
===========

Convergence is a library that implements some of the well known CRDTs as
defined in the paper
[A comprehensive study of Convergent and Commutative Replicated Data Types](http://hal.archives-ouvertes.fr/docs/00/55/55/88/PDF/techreport.pdf),
as well as some additional CRDTs i came up with.

In Abstract Algebra a CRDT is formally known as a [Semilattice](http://en.wikipedia.org/wiki/Semilattice).
All Semilattices must obey the following rules:

  - Associativity: ```x ∧ (y ∧ z) = (x ∧ y) ∧ z```
  - Commutativity: ```x ∧ y = y ∧ x``` 
  - Idempotency: ```x ∧ x = x```

This means that given two or more CRDTs you can merge them in any any order
with any precedence any amount of times and the results will always
consistently converge to the same thing. This is a very useful trick to
manage consistency in distributed systems.

There is a somewhat cannonical paper on CRDTs called A comprehensive study
of Convergent and Commutative Replicated Data Types. This paper contains the
the definitions of the two main families of CRDTs (CvRDT vs CmRDT) along with
the specifications for numerous CRDT counters, registers and sets.

The CRDTs
  - Counters
    + G-Counter: State-based increment-only Counter
    + GA-Counter: Actor-indexed State-based increment-only Counter
    + PN-Counter: State-based Counter
    + PN-Counter: Actor-indexed State-based Counter
  - Registers
    + LWW-Register: Last Writer Wins Register
  - Sets
    + G-Set: Grow-only Set
    + 2P-Set: Two-phase Set
    + LWW-element-Set: Last Writer Wins Element Set
    + OR-Set: Observed-Remove Set
    + MC-Set: Max-Change Set
