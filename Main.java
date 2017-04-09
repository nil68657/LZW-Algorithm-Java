/*LZW Algorithm
Author : Nilanjan Chatterjee */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
	
	public static void main(String[] args) throws IOException {
		String inputFileName = args[0];
		String outputFileName = args[0].split("\\.")[0];
		StringBuilder fileText, decodedText;
		String[] encodedTextCodes;
		StringBuilder encodedText = new StringBuilder();
		StringBuilder binaryFormat = new StringBuilder();
		int bitLength = Integer.parseInt(args[1]);
		if(bitLength <= 0) return;
		ArrayList<Integer> encodedValues = new ArrayList<>();
		
		fileText  = readFromFile(inputFileName);
		if(fileText.length() == 0) return;
		System.out.println("\nInput: " + fileText);
		System.out.print("\nEncoded text: ");
		encodedText = encodeText(fileText.toString(), bitLength);

		encodedTextCodes = encodedText.toString().split(" ");
		if(encodedTextCodes.length == 0 || encodedTextCodes[0].length() == 0) return;
		for(String characterCode : encodedTextCodes) {
			if(characterCode.contains("null")) continue;
			int code = Integer.parseInt(characterCode);
			binaryFormat.append(String.format("%016d", Integer.parseInt(Integer.toBinaryString(code))) + " ");
		}
		writeToFile(outputFileName + ".lzw", binaryFormat.toString());
		System.out.println("\nBinary: " + binaryFormat.toString());
		encodedTextCodes = readFromFile(outputFileName + ".lzw").toString().split(" ");
		for(String characterCode : encodedTextCodes)
			encodedValues.add(Integer.parseInt(characterCode, 2));
		
		if(encodedValues.size() == 0) return;
		System.out.print("\nDecoded text: ");
		decodedText = decodeText(encodedValues, bitLength);
		writeToFile(outputFileName + "_decoded.txt", decodedText.toString());
	}
	
	private static void writeToFile(String fileName, String fileText) throws IOException {
		File outputFile = new File(fileName);
		if(!outputFile.exists())
			outputFile.createNewFile();
		FileWriter fileWriter = new FileWriter(outputFile);
		fileWriter.write(fileText);
		fileWriter.close();
	}
	
	private static StringBuilder readFromFile(String fileName) throws IOException {	
		String line = "";
		StringBuilder fileText = new StringBuilder();
		File inputFile = new File(fileName);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
		while((line = bufferedReader.readLine()) != null)
			fileText.append(line.toString() + "\n");
		if(fileText.length() != 0)
			fileText.deleteCharAt(fileText.length()-1);
		bufferedReader.close();
		return fileText;
	}
	
	public static StringBuilder encodeText(String fileText, int bitLength) {
		String STRING;
		char SYMBOL;
		int MAX_TABLE_SIZE;
		int index = 0, textLength = fileText.length();
		if(textLength==0)
			return null;
		StringBuilder encodedText = new StringBuilder();
		Map<String, Integer> mapTable = new HashMap<>();
		for(int i = 0; i<256; i++)
			mapTable.put(Character.toString((char) i), i);
		
		MAX_TABLE_SIZE = (int) Math.pow(2, bitLength);
		STRING = "";
		while(index < textLength) {
			SYMBOL = fileText.charAt(index++);
			if(mapTable.containsKey(STRING + SYMBOL))
				STRING = STRING + SYMBOL;
			else {
				System.out.print(mapTable.get(STRING) + " ");
				encodedText.append(mapTable.get(STRING) + " ");
				if(mapTable.size() < MAX_TABLE_SIZE)
					mapTable.put(STRING + SYMBOL, mapTable.size());
				STRING = Character.toString(SYMBOL);
			}
		}
		System.out.println(mapTable.get(STRING));
		encodedText.append(mapTable.get(STRING));
		return encodedText;
	}
	
	private static StringBuilder decodeText(ArrayList<Integer> encodedValues, int bitLength) {
		String STRING, NEW_STRING;
		int MAX_TABLE_SIZE;
		int CODE, index = 0, codeLength = encodedValues.size();
		StringBuilder decodedText = new StringBuilder();
		Map<Integer, String> mapTable = new HashMap<>();
		for(int i = 0; i<256; i++)
			mapTable.put(i, Character.toString((char) i));
		
		MAX_TABLE_SIZE = (int) Math.pow(2, bitLength);
		CODE = encodedValues.get(index++);
		STRING = mapTable.get(CODE);
		System.out.print(STRING);
		decodedText.append(STRING);
		while(index < codeLength) {
			CODE = encodedValues.get(index++);
			if(!mapTable.containsKey(CODE))
				NEW_STRING = STRING + STRING.charAt(0);
			else
				NEW_STRING = mapTable.get(CODE);
			System.out.print(NEW_STRING);
			decodedText.append(NEW_STRING);
			if(mapTable.size() < MAX_TABLE_SIZE)
				mapTable.put(mapTable.size(), STRING + NEW_STRING.charAt(0));
			STRING = NEW_STRING;
		}
		return decodedText;
	}
}
