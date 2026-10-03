# huffmanCompression

This is BIL212 Data Structures Homework 4, completed on 22 January 2019.

The program compresses a text file with Huffman coding and restores it. Encoding writes a `.huff` file and deletes the original. Decoding restores the text file and deletes the `.huff` file.

The original handout is `bil212summer2018hw4.pdf`.

## Requirements

Java 8 or later (`javac` and `java` on the PATH).

## Run

```bash
cd src
javac TreeNode.java HeapPQ.java HuffCode.java
java HuffCode encode input.txt
java HuffCode decode input.txt.huff
```

`encode` reads `input.txt`, writes `input.txt.huff`, and deletes `input.txt`.
`decode` reads `input.txt.huff`, writes `input.txt`, and deletes `input.txt.huff`.
