# LFTC

## Documentation

The symbol table uses a HashTable as an internal representation.
The hash function computes the mod % key.size() of the mod % key.size() of the sums for each ascii code
in the key
findTermPosition function searches for collisions inside the hash table. If the current term does not
exist, returns null, otherwise returns the position of the current term
add function returns the positon of a term if it already exists inside the hashtable, otherwise computes
the hash of the key and stores the new term.

The PIF uses a list of Pairs, where the first element from the Pair is the id/const (or the token itself) 
and the second element is the position of the id/const inside the SymbolTable (or (-1, -1) in case of OPERATOR/SEPARATOR/RESERVED_WORD)

# Regex:
## Identifier: 
        "^[a-z]+[a-z|A-z|0-9]*$" --> token needs to start with lowercase letter (at least one) an can be followed by lower/uppercase letters or digits (zero or more)

## Constants: 
        TRUE|FALSE --> matches boolean
        ^(P0|(N|P)[1-9]+[0-9]*)$ --> integer constant starts with POZ/NEG corresponding to the sign followed by digits from 1-9, then any digits. 0 is represented as P0.
        ^\"[a-z|A-Z|0-9| |_]+\"$); --> non-empty strings containing letters, spaces, digits or _"

# Finite Automata:
    In order to identify integer constants and identifiers, we defined the following finite automatas:
    
## Input Lab FA:
set of states: A B C

symbols: 0 1 

starting state: A 

final states: B C 

transitions: 
(A,0) -> B 
(A,1) -> C
(B,0) -> B
(B,1) -> C 

nonterminals: B, C, DFA

terminals: “0”, “1”

B = “0” {“0”}

C = B “1” | “1”

DFA = B | C

DFA = (“0” {“0”} [“1”]) | “1”


## Identfier:
        Q : {A;B;}
        SIGMA : {a;b;c;d;e;f;g;h;i;j;k;l;m;n;o;p;q;r;s;t;u;v;w;x;y;z;A;B;C;D;E;F;G;H;I;J;K;L;M;N;O;P;Q;R;S;T;U;V;W;X;Y;Z;_;0;1;2;3;4;5;6;7;8;9;}
        F : {B}
        q : A
        TRANSITIONS : {
        A->a->B;A->b->B;A->c->B;A->d->B;A->e->B;A->f->B;A->g->B;A->h->B;A->i->B;A->j->B;A->k->B;A->l->B;A->m->B;A->n->B;A->o->B;A->p->B;A->q->B;A->r->B;A->s->B;
        A->t->B;A->u->B;A->v->B;A->w->B;A->x->B;A->y->B;A->z->B;A->A->B;A->B->B;A->C->B;A->D->B;A->E->B;A->F->B;A->G->B;A->H->B;A->I->B;A->J->B;A->K->B;A->L->B;
        A->M->B;A->N->B;A->O->B;A->P->B;A->Q->B;A->R->B;A->S->B;A->T->B;A->U->B;A->V->B;A->W->B;A->X->B;A->Y->B;A->Z->B;B->a->B;B->b->B;B->c->B;B->d->B;B->e->B;
        B->f->B;B->g->B;B->h->B;B->i->B;B->j->B;B->k->B;B->l->B;B->m->B;B->n->B;B->o->B;B->p->B;B->q->B;B->r->B;B->s->B;B->t->B;B->u->B;B->v->B;B->w->B;B->x->B;
        B->y->B;B->z->B;B->A->B;B->B->B;B->C->B;B->D->B;B->E->B;B->F->B;B->G->B;B->H->B;B->I->B;B->J->B;B->K->B;B->L->B;B->M->B;B->N->B;B->O->B;B->P->B;B->Q->B;
        B->R->B;B->S->B;B->T->B;B->U->B;B->V->B;B->W->B;B->X->B;B->Y->B;B->Z->B;B->_->B;B->0->B;B->1->B;B->2->B;B->3->B;B->4->B;B->5->B;B->6->B;B->7->B;B->8->B;
        B->9->B;
        }
    
## Integer Constant:
        Q : {A;B;C;D;}
        SIGMA : {P;N;#;0;1;2;3;4;5;6;7;8;9;}
        F : {C;D}
        q : A
        TRANSITIONS : {
        A->P->B;A->N->B;A->0->C;A->1->C;A->2->C;A->3->C;A->4->C;A->5->C;A->6->C;A->7->C;A->8->C;A->9->C;B->0->C;B->1->C;B->2->C;B->3->C;B->4->C;B->5->C;B->6->C;
        B->7->C;B->8->C;B->9->C;C->0->C;C->1->C;C->2->C;C->3->C;C->4->C;C->5->C;C->6->C;C->7->C;C->8->C;C->9->C;}
    
We check the sequencies against these deterministic finite automatas. 


## 1. Compute First Set:
- Representation: Map<String, Set<String>>
- each non-terminal will be a key in the Map
- Firstly, for each non-terminal we will iterate through its productions and we will initialize the
Map with the terminals that appear on the first position in the RHS
- Then, for each non-terminal we will iterate through its productions and compute the value
of First in the following way:
1. if the RHS of the production is empty, then an Exception is thrown
2. if the RHS contains only one element, the previous value from the FirstSet(RHS) is
used
3. if the RHS contains more than one element, a concatenation of length 1 will be
performed between all elements of the RHS
 This step is performed until there is no change in the First Set

## 2. Compute Follow Set:
- Representation: Map<String, set<String>>
- Initialization: every non-terminal will have an Empty Set as value in the Map, besides the
starting symbol which will have the symbol “$”
- For each non-terminal, we look for its occurrences in all productions RHS.
For each production RHS, we make the corresponding adjustments for the current nonterminal based on the pre-established rules:
Considering productions are of the form A -> a B b:
Follow(B) = First(b) + Follow(A)
If Epsilon is in First(b) or b does not exist, then Follow(B) = Follow(B) + Follow(A)
This step is performed until there is no change in the Follow Set.

## 3. Compute Parse Table
- representation: Map<String, Map<String, Pair<List<String>, Integer>>>
- each cell of the table will have a List of Strings representing the RHS of a production and an
Integer representing the code of that production or one of the messages “pop” / “accept”
- there are 3 rules that need to be followed in order to construct the Parse Table:
1. cell [$, $] = accept
2. cell[terminal, terminal] = pop
3. for each production we will get its LHS and its production code.
If RHS = Epsilon, then for each element of Follow(LHS), cell [LHS, element] = (Epsilon,
production code)
Else, for each element of First(LHS), cell [LHS, element] = (RHS, production code)
In both cases, if there is already a value in the cell [LHS, element], an Exception is
thrown

## 4. Parsing a sequence
- We need the Parse Table, an input stack, a working stack and an output stack, all 3 stacks
being Lists of Strings
- We look for an entry in the Parse Table for the pair formed between the top of the working
stack and the top of the input stack
If that entry corresponds to a push operation, we will go through each element ( different
from Epsilon) of the RHS of the production we found in that cell and add it in the working
stack. We will also add in the output stack the production code.
If that entry corresponds to a pop operation, we remove the top from the input stack
If that entry corresponds to an accept operation and all we have left in both input stack and
working stack is the symbol “$”, then parsing is done and the sequence is accepted