This code represents the functionality of Lempel–Ziv–Welch (LZW) algorithm, which is a lossless data compression algorithm.The LZW algorithm reads an input sequence of symbols, groups the symbols into strings, and represents the strings with integer codes. LZW is a greedy algorithm in that it finds the longest string that it has a code for, and then outputs that code.

We have used HashMap data structure. BufferedReader,FileReader and FileWriter are a few other functions which have been used.

In the Encoding part we have used HashMap to store the encoded data in the meantime. After following the algorithm as per the pseudocode,the output will saved with.lzw extension.

In the Decoding part we have used HashMap to store the decoded data in the meantime, After complete decoding as per algorithm,the decoded output will saved as decoded.txt. 
