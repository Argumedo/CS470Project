package cs488;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserLibrary {
	List<String> keywords;
	List<String> spSymbols;
	List<String> idList;
	List<Integer> numList;
	List<String> terminalList;

	public ParserLibrary() 
	{
		keywords = new ArrayList<String>(); 
		spSymbols = new ArrayList<String>();
		idList = new ArrayList<String>();
		numList = new ArrayList<Integer>();
		terminalList = new ArrayList<String>();

		
		
		keywords.add("else");
		keywords.add("if");
		keywords.add("int");
		keywords.add("return");
		keywords.add("void");
		keywords.add("while");
		
		spSymbols.add("+");
		spSymbols.add("-");
		spSymbols.add("*");
		spSymbols.add("/");
		spSymbols.add("<");
		spSymbols.add("<=");
		spSymbols.add(">");
		spSymbols.add(">=");
		spSymbols.add("==");
		spSymbols.add("!=");
		spSymbols.add("=");
		spSymbols.add(";");
		spSymbols.add(",");
		spSymbols.add("(");
		spSymbols.add(")");
		spSymbols.add("[");
		spSymbols.add("]");
		spSymbols.add("{");
		spSymbols.add("}");
		spSymbols.add("/*");
		spSymbols.add("*/");
		
		
		for(int i = 0; i < 26; i++)
		{
			char a = (char)(97 + i);
			idList.add(""+a);
		}
		for(int i = 0; i < 26; i++)
		{
			char a = (char)(65 + i);
			idList.add(""+a);
		}
		
		for(int i = 0; i < 10; i++)
		{
			numList.add(i);
		}
		
		String line = null;
		try
		{
			FileReader fr = new FileReader("TerminalList.txt");
			
			BufferedReader br = new BufferedReader(fr);
			
			while((line = br.readLine()) != null)
			{
				terminalList.add(line);
			}
			
			br.close();
		}
		catch(FileNotFoundException E)
		{
			System.out.println("File Not Found");
		}
		catch(IOException E)
		{
			System.out.println("Error Reading File");
		}
		
	}

}
