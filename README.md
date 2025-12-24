# dsaa-lab04-cycled-ordered-list-and-doc-parser

This repository contains the solution for **DSAA Lab 04**.  
The project is a **command-line Java application** that implements a two-way cycled ordered list with a sentinel and parses documents to extract weighted links.

---

## Features

### 1. Two-Way Cycled Ordered List with Sentinel
- Custom implementation of a **two-way cycled ordered list**
- Uses a **sentinel node**
- Maintains elements in **sorted order**
- Supports forward and backward traversal

---

### 2. Document Reading & Link Parsing
- Reads a document line by line from **standard input**
- Document reading ends when the line `eod` is entered
- Extracts tokens starting with `link=`
- Supports **weighted links**, e.g. `link=abc(3)`
- Valid identifier rule: starts with a letter, then letters, digits, or `_`

---

## Project Structure

```text
dsaa.lab04
├── Main.java
├── Document.java
├── Link.java
└── TwoWayCycledOrderedListWithSentinel.java
```