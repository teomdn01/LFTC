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

Regex:
    Identifier: "^[a-z]+[a-z|A-z|0-9]*$" --> token needs to start with lowercase letter (at least one) an can be followed by lower/uppercase letters or digits (zero or more)

    Constants: "TRUE|FALSE" --> matches boolean
                "^(POZ0|(NEG|POZ)[1-9]+[0-9]*)$" --> integer constant starts with POZ/NEG corresponding to the sign followed by digits from 1-9, then any digits. 0 is represented as POZ0.
                "^\"[a-z|A-Z|0-9| |_]+\"$"); --> non-empty strings containing letters, spaces, digits or _"