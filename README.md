# Lexical-Analyzer-Parser

Parser &amp; Lexical Analyzer for a simple Programming Language grammar

### Grammar Definition:

statement = {expression ";" } "."

expression = term { ( "+" | "-" ) term }

term = factor {("\*" | "/") factor}

factor = number | "-"number | "(" expression ")"

for learning about programming language grammar check out [Montana State University lecture](https://faculty.ksu.edu.sa/sites/default/files/04-grammars.pdf)

### Description

Accepts expressions from either console or file. Able to evaluate expressions given that the grammar is correct. Raises an exception and exits if grammar is invalid.

Includes Parser and Lexical Analyser which are able to process expressions which can contain:

- numbers
- negative numbers
- expressions in parenthesiss
- addition and subtraction
- multiplication and division
