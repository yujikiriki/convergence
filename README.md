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