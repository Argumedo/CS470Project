package cs488;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class Compilers {

	public static void main(String[] args) 
	{
		List<Grammar> gList = getGrammar(); 
		Map<String, List<String>> firstList = firstList(gList);
		Map<String, List<String>> followList = followList(gList);
		printFList(firstList, followList , gList);
		Map<Integer, List<String>> items = it(gList);
		printMap(items);
		Map<Integer, List<String>> slrTable = createSLRTable(items);
		printSLR(slrTable);
		
		List<String> code = parseCode();
		for(int i = 0; i < code.size(); i++)
		{
			System.out.println(code.get(i));
		}
//		parser(code, slrTable);
		
	}	
	
	public static void parser(List<String> code , Map<Integer, List<String>> slr)
	{
		System.out.println("--------------------");
		List<String> stack = new ArrayList<String>();
		Stack<String> stack1 = new Stack<String>();
		ParserLibrary symbols = new ParserLibrary();

		List<Grammar> grammars = getGrammar();
		stack.add("0");
		stack1.push("0");
		
		for(int i = 0; i < code.size(); i++)
		{
			String [] line = code.get(i).split(" ");
			for(int j = 0; j < line.length; j++)
			{
				System.out.println(line[j]);
			}
		}

	}
	
	
	public static List<String> parseCode()
	{
		List<String> inputList = new ArrayList<String>(); 
		ParserLibrary symbols = new ParserLibrary();

		String File = "Sample1.txt";
		String line = null;
		try
		{
			FileReader fr = new FileReader(File);

			BufferedReader br = new BufferedReader(fr);
			String add = "";

			boolean comment = false;
			while((line = br.readLine()) != null)
			{
				String [] s = line.split(" ");
				for(int i = 0; i < s.length; i++)
				{
					if(s[i].contains("/*"))
					{
						comment = true;
					}		
					
					if(!comment)
					{
						add += s[i];
						if(i < s.length-1 && add.length() > 0)
						{
							add += " ";
						}
						
//						if(symbols.terminalList.contains(s[i]))
//						{
//							inputList.add(s[i]);
//						}
//						else
//						{
//							String [] split = s[i].split("");
//							System.out.println(".....");
//							for(int j = 0; j < split.length; j++)
//							{
//								if(split[j].length() > 0)
//								{
//									
//								}
//							}
//						}
					}
					
					if(s[i].contains("*/"))
					{
						comment = false;
					}
				}
				if(add.length() > 0)
				{
					inputList.add(add);
				}
			
				br.close();
			}
		}
		catch(FileNotFoundException E)
		{
			System.out.println("File Not Found");
		}
		catch(IOException E)
		{
			System.out.println("Error Reading File");
		}
		return inputList;
	}
	
	public static Map<Integer, List<String>> createSLRTable(Map<Integer, List<String>> items)
	{
		Map<Integer, List<String>> slrTable = new HashMap<Integer, List<String>>();
		for(int i = 0; i < items.size(); i++)
		{
			List<String> item = items.get(i);
			List<String> checked = new ArrayList<String>();
			List<String> tableList = new ArrayList<String>();

//			System.out.println("------------ State " + i + " -------------");
			for(int j = 0; j < item.size(); j++)
			{
				String [] split = item.get(j).split(" ");
				for(int k = 0; k < split.length; k++)
				{
					if(split[k].equals("."))
					{
						if(k < split.length-1)
						{
							if(!checked.contains(split[k+1]) && !split[k+1].equals("empty"))
							{
								String t = action(items, i, split[k+1]);
//								System.out.println("Symbol : " + split[k+1]);
//								System.out.println("Command : " + t);
								tableList.add(split[k+1]);
								tableList.add(t);
								
								checked.add(split[k+1]);
							}
							else if(split[k+1].equals("empty"))
							{
								String t =  action(items, i, split[k+1]);
								String [] splitTemp = t.split(" ");
								String action = splitTemp[splitTemp.length-2] + " " + splitTemp[splitTemp.length-1];
								for(int sp = 0; sp < splitTemp.length-2; sp++)
								{
//									System.out.println("Symbol : " + splitTemp[sp]);
//									System.out.println("Command : " + action + " \n");
									
									tableList.add(splitTemp[sp]);
									tableList.add(action);
								}
							}
						}
						else if(split.length-1 == k)
						{
							String t =  action(items, i, split[k-1]);
							String [] splitTemp = t.split(" ");
							if(splitTemp.length <= 2)
							{
//								System.out.println("Symbol : " + split[k-1]);
//								System.out.println("Command : " + t);
								tableList.add(split[k-1]);
								tableList.add(t);
								checked.add(split[k-1]);
							}
							else
							{
								String action = splitTemp[splitTemp.length-2] + " " + splitTemp[splitTemp.length-1];
								for(int sp = 0; sp < splitTemp.length-2; sp++)
								{
//									System.out.println("Symbol : " + splitTemp[sp]);
//									System.out.println("Command : " + action + " \n");
									tableList.add(splitTemp[sp]);
									tableList.add(action);
								}
							}
						}
					}
				}
				
			}
			
			slrTable.put(i, tableList);
		}
		return slrTable;
	}
	
	public static String action(Map<Integer, List<String>> items, int item,String a)
	{
		String act = "";
		if(!isNonTerm(a))
		{
			List<String> temp = new ArrayList<String>();
			
			if(!a.equals("empty"))
			{
				temp = goToF(items.get(item), a);
			}
			
			if(!temp.isEmpty())
			{
				for(int i = 0; i < items.size(); i++)
				{
					
					if(items.get(i).equals(temp) || contain(items.get(i), temp))
					{
						
						act = "Shift " + i + "\n";
					}
				}	
			}
			else
			{
				List<Grammar> gList = getGrammar();
				String produ = items.get(item).get(0);
				if(a.equals("empty"))
				{
					for(int i = 0; i < items.get(item).size(); i++)
					{
						if(i%2 == 1)
						{
							if(items.get(item).get(i).contains("empty"));
								produ = items.get(item).get(i-1);
							
						}
					}
				}
				List<String> follow = findFollow(produ, gList, "");

				for(int i = 0; i < follow.size(); i++)
				{
					act += follow.get(i);
					
					if(i<follow.size()-1)
					{
						act += " ";
					}
				}
				
				act += " Reduce ";
				List<String> tem = new ArrayList<String>();
				for(int i = 0 ; i < items.get(item).size(); i++)
				{
					if(i%2 == 1 )
					{
						String [] t = items.get(item).get(i).split(" ");
						String tempo = "";
						
						boolean okay = false;
						for(int j = 0; j < t.length; j++)
						{
							if(!t[j].equals(".") && t[t.length-1].equals("."))
							{
								okay = true;
								tempo += t[j];
								if(j < t.length-1)
								{
									if(!t[j+1].equals("."))
									{
										tempo += " ";
									}
								}
							}
							else if(t[t.length-1].equals("empty") && !t[j].equals("."))
							{
								okay = true;
								tempo += t[j];
								if(j < t.length-1)
								{
									if(!t[j+1].equals("."))
									{
										tempo += " ";
									}
								}
							}
						}
						if(okay)
						{
							tem.add(items.get(item).get(i-1));
							tem.add(tempo);
						}
					}
				}
				
				
				for(int i = 0; i < gList.size(); i++)
				{
					
					if(tem.size() > 1)
					{
						if(gList.get(i).term.equals(tem.get(0)) && gList.get(i).prod.equals(tem.get(1)))
						{
							act += (i+1) + "\n";
						}
					}
				}
			}
		}
		else if(isNonTerm(a))
		{
			List<String> temp = goToF(items.get(item), a);
			List<Grammar> gList = getGrammar();
			
			if(a.equals(gList.get(0).term) && temp.isEmpty())
			{
				act = "Accept";
				
			}
			else if(!temp.isEmpty())
			{
				for(int i = 0; i < items.size(); i++)
				{
					if(items.get(i).equals(temp) || contain(items.get(i), temp))
					{
						act = "Go To " + i + "\n";
					}
				}
			}
			else
			{
				String produ = items.get(item).get(0);
				List<String> follow = findFollow(produ, gList, "");
				for(int i = 0; i < follow.size(); i++)
				{
					act += follow.get(i);
					
					if(i<follow.size()-1)
					{
						act += " ";
					}
				}
				
				act += " Reduce ";
				List<String> tem = new ArrayList<String>();
				for(int i = 0 ; i < items.get(item).size(); i++)
				{
					if(i%2 == 1 )
					{
						String [] t = items.get(item).get(i).split(" ");
						String tempo = "";
						boolean okay = false;
						for(int j = 0; j < t.length; j++)
						{
							if(!t[j].equals(".") && t[t.length-1].equals("."))
							{
								okay = true;
								tempo += t[j];
								if(j < t.length-1)
								{
									if(!t[j+1].equals("."))
									{
										tempo += " ";
									}
								}
							}
						}
						if(okay)
						{
							tem.add(items.get(item).get(i-1));
							tem.add(tempo);
						}
					}
				}
				for(int i = 0; i < gList.size(); i++)
				{
					
					if(gList.get(i).term.equals(tem.get(0)) && gList.get(i).prod.equals(tem.get(1)))
					{
						act += (i+1) + "\n";
					}
				}
			}
		}
		else
		{
		}
		
		return act;
	}
	
	public static boolean contain(List<String> bigger, List<String> smaller)
	{
		boolean yes = false;
		if(bigger.size() > smaller.size())
		{
			List<String> temp = new ArrayList<String>();
			
			for(int i = 0; i < smaller.size(); i++)
			{
				temp.add(bigger.get(i));
			}
			if(temp.equals(smaller))
			{
				yes = true;
			}
		}
		
		return yes;
	}
	
	public static List<String> goToF(List<String> item, String look)
	{
		List<String> temp = goTo(item, look);
		List<String> nTemp = new ArrayList<String>();
		for(int i = 0; i < temp.size(); i++)
		{
			if(i%2 == 1)
			{
				String newProd = shift(temp.get(i));
				nTemp.add(temp.get(i-1));
				nTemp.add(newProd);
			}
		}
		return nTemp;
	}
	
	public static List<String> goTo(List<String> item, String look)
	{
		List<String> found = new ArrayList<String>();
		for(int i = 0; i < item.size(); i++)
		{
			if(i%2 == 1)
			{
				
				String [] find = item.get(i).split(" ");
				for(int j = 0; j < find.length; j++)
				{	
					if(find[j].equals(".") && j < find.length-1)
					{
						//&& !found.contains(item.get(i))
						if(find[j+1].equals(look))
						{
							boolean yes = false;
							if(!found.contains(item.get(i)))
							{
								yes = true;
							}
							else
							{
								String nProd = item.get(i);
								String tNon = item.get(item.indexOf(nProd)-1);
								if(!tNon.equals(item.get(i-1)))
								{
									yes= true;
								}
							}
							
							if(yes)
							{
								found.add(item.get(i-1));
								found.add(item.get(i));
							}
						}
					}
				}
			}
		}
		
		return found;
	}
	

	
	public static Map<Integer, List<String>> it(List<Grammar> gList)
	{		
		List<Grammar> newList = new ArrayList<Grammar>();
		Map<Integer, List<String>> itemsss = new HashMap<Integer,List<String>>();

		newList.add(new Grammar(gList.get(0).term + "'", gList.get(0).term));
		
		for(int i = 0; i < gList.size(); i++)
		{
			newList.add(gList.get(i));			
		}
				
		itemsss.put(0, produce(newList.get(0).term, newList, ""));
		
		int countItems = 1;
		
		for(int s = 0; s < itemsss.size(); s++)
		{
			List<String> itemList = itemsss.get(s);
			List<String> checkedSym = new ArrayList<String>();
			for(String item : itemList)
			{
				String [] spTemp = item.split(" ");
				for(int c = 0; c < spTemp.length; c++)
				{

					if(spTemp[c].equals(".") && c < spTemp.length-1)
					{
						List<String> tempAdd = new ArrayList<String>();

						if(!checkedSym.contains(spTemp[c+1]))
						{
							tempAdd = goTo(itemList, spTemp[c+1]);
							checkedSym.add(spTemp[c+1]);
						}
						else
							continue;

						//Shifts the . in  tempAdd
						for(int i = 0; i < tempAdd.size(); i++)
						{
							if(i%2 == 1)
							{
								String temp = shift(tempAdd.get(i));
								tempAdd.set(i, temp);
							}
						}
						
						for(int i = 0; i < tempAdd.size(); i++)
						{
							if(i%2 == 1)
							{
								String [] spTemp2 = tempAdd.get(i).split(" ");
								for(int j = 0; j < spTemp2.length; j++)
								{
									if(spTemp2[j].equals(".") && j < spTemp2.length-1)
									{
										if(isNonTerm(spTemp2[j+1]))
										{
											List<String> tList = produce(spTemp2[j+1], gList, "");
								
											for(int te = 0; te < tList.size(); te++)
											{

												if(te % 2 == 1)
												{
													
													String tPro = tList.get(te);
													if(!tempAdd.contains(tList.get(te)))
													{														
														tempAdd.add(tList.get(te-1));
														tempAdd.add(tList.get(te));
													}
													else
													{
														boolean okay = true;
														for(int a = 0; a < tempAdd.size(); a++)
														{
															if(a%2 == 1)
															{
																String NonTerm = tempAdd.get(a-1);
																String Prod = tempAdd.get(a);
																
																if(tPro.equals(Prod))
																{
																	if(NonTerm.equals(tList.get(te-1)))
																	{
																		okay = false;
																	}
																}
															}
														}
														
														if(okay)
														{
															tempAdd.add(tList.get(te-1));
															tempAdd.add(tList.get(te));
														}	
													}													
												}
											}
										}
									}
								}
							}
						}
						if(tempAdd.size() != 0)
						{
							if(!itemsss.containsValue(tempAdd))
							{
								if(!tempAdd.get(1).equals("empty ."))
								{
//									System.out.println("----------- Item " + countItems + " -------------- ");
									itemsss.put(countItems, tempAdd);
									countItems++;
								}
							}
						}
					}
				}
			}
		}
		return itemsss;
	}
	
	public static String shift(String sent)
	{
		String [] split = sent.split(" ");
		String temp = "";
		for(int i = 0; i < split.length; i++)
		{
			if(split[i].equals("."))
			{
				if(i < split.length-1)
				{
					temp += split[i+1] + " .";
					i++;
				}
				else
				{
					temp = "";
					break;
				}
			}
			else
			{
				temp += split[i];
			}
			
			if(i < split.length-1)
			{
				temp += " ";
			}
		}
		return temp;
	}
	
	public static void printList(List<String> stuff)
	{
		for(int i = 0; i < stuff.size(); i++)
		{
			if(i%2 == 1)
			{
				System.out.println(stuff.get(i-1) + " -> " + stuff.get(i));
			}
		}
	}
	
	public static void printSLR(Map<Integer, List<String>> items)
	{		
		try{
			File fout = new File("SLRTable.txt");
			FileOutputStream fos = new FileOutputStream(fout);
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			for(int i = 0; i < items.size(); i++)
			{
				String write = "------------------ State " + i + " ------------------ \n";
				List<String> s = items.get(i);	
				bw.write(write);
				for(int j = 0; j < s.size(); j++)
				{
					if(j%2 == 1)
					{
						String w = "Symbol : " + s.get(j-1) + "\nCommand : " + s.get(j) + "\n";
						bw.write(w);
					}
				}
				bw.newLine();
			}
			
			bw.close();
			
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File Not Found");
		}
		catch(IOException e)
		{
			System.out.println("Error Creating file");
		}	
	}
	
	public static void printMap(Map<Integer, List<String>> items)
	{		
		try{
			File fout = new File("ItemList.txt");
			FileOutputStream fos = new FileOutputStream(fout);
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			for(int i = 0; i < items.size(); i++)
			{
				String write = "------------------ State " + i + " ------------------ \n";
				List<String> s = items.get(i);	
				bw.write(write);
				for(int j = 0; j < s.size(); j++)
				{
					if(j%2 == 1)
					{
						String w = s.get(j-1) + " -> " + s.get(j) + "\n";
						bw.write(w);
					}
				}
				bw.newLine();
			}
			
			bw.close();
			
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File Not Found");
		}
		catch(IOException e)
		{
			System.out.println("Error Creating file");
		}
//		for(int i = 0 ; i < items.size(); i++)
//		{
//			List<String> temp = items.get(i);
//			
//			for(int j = 0; j < temp.size(); j++)
//			{
//				if(j == 1)
//				{
//					System.out.println("Item " + i + " : " + temp.get(j-1) + " -> " + temp.get(j));
//				}
//				if(j%2 == 1 && j > 1)
//				{
//					System.out.println("          " + temp.get(j-1) + " -> " + temp.get(j));
//				}
//			}
//			
//		}
		
		
	}
	
	
	public static List<String> produce(String v, List<Grammar> newList, String avoid)
	{
		List<String> Productions = new ArrayList<String>();
		for(int i = 0; i < newList.size(); i++)
		{
			if(v.equals(newList.get(i).term))
			{
				boolean yes = false;
				String temp = ". "+newList.get(i).prod;
				if(!Productions.contains(temp))
				{
					yes = true;
				}
				else
				{
					String tNon = Productions.get(Productions.indexOf(temp)-1);
					if(!tNon.equals(newList.get(i).term))
					{
						yes = true;
					}
				}
				
				if(yes)
				{
					Productions.add(newList.get(i).term);
					Productions.add(temp);
				}
			}
		}
		
		for(int i = 0; i < Productions.size(); i++)
		{
			String [] spTemp = Productions.get(i).split(" ");
			for(int j = 0; j < spTemp.length; j++)
			{
				if(spTemp[j].equals(".") && j < spTemp.length-1)
				{
					if(isNonTerm(spTemp[j+1]))
					{
						for(int k = 0; k < newList.size(); k++)
						{
							String nonTerm = spTemp[j+1];
							if(nonTerm.equals(newList.get(k).term))
							{
								String temp = ". " + newList.get(k).prod;
								
								boolean yes = false;
								
								if(!Productions.contains(temp))
								{
									yes = true;
								}
								else
								{
									String tNon = Productions.get(Productions.indexOf(temp)-1);
									if(!tNon.equals(newList.get(k).term))
									{
										yes = true;
									}
								}
								
								if(yes)
								{
									Productions.add(newList.get(k).term);
									Productions.add(temp);
								}
							}
						}
					}
				}
			}
			
		}

	
		return Productions;
	}
	
	public static boolean isNonTerm(String v)
	{
		ParserLibrary symbols = new ParserLibrary();
		boolean term = false;
		for(int k = 25 ; k < symbols.idList.size(); k++)
		{
			if(v.substring(0,1).equals(symbols.idList.get(k)))
			{
				term = true;
			}	
		}
		return term;
	}
	
	
	
	
	public static void printFList(Map<String, List<String>> firstList, Map<String, List<String>> followList, List<Grammar> gList)
	{
		List<String> checked = new ArrayList<String>();
		
		try{
			File fout = new File("FirstAndFollow.txt");
			FileOutputStream fos = new FileOutputStream(fout);
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			String title = "NonTerminal     First              "
					+ "                     Follow";
			
			bw.write(title);
			bw.newLine();
			for(int i = 0; i < gList.size(); i++)
			{
				String s = gList.get(i).term;			
				if(!checked.contains(s))
				{
					String var = "";
					if(s.length() == 1)
					{
						var += "     " + s + "          ";
					}
					else
					{
						var += "     " + s + "         ";
					}
					
					String first = "";
					for(int j = 0; j < firstList.get(s).size(); j++)
					{
						first += firstList.get(s).get(j) + " ";
					}
					
					if(first.length() < 40)
					{
						int g = 40 - first.length();
						for(int j = 0; j < g; j++)
						{
							first += " ";
						}
					}
					
					String follow = "";
					for(int j = 0; j < followList.get(s).size(); j++)
					{
						follow += followList.get(s).get(j) + " ";
					}
					
					bw.write(var + first + follow);
					bw.newLine();
				}
				checked.add(s);	
			}
			
			bw.close();
			
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File Not Found");
		}
		catch(IOException e)
		{
			System.out.println("Error Creating file");
		}
	}
	
	
	
	public static Map<String, List<String>> followList(List<Grammar> gList)
	{
		Map<String, List<String>> FollowList= new HashMap<String, List<String>>();
		
		for(int i = 0; i < gList.size(); i++)
		{
			String s = gList.get(i).term;
			
			if(!FollowList.containsKey(s))
			{
				List<String> first = findFollow(gList.get(i).term, gList, "");
				FollowList.put(s, first);
			}			
		}
		
		return FollowList;
	} 
	
	public static List<String> findFollow(String v, List<Grammar> gList, String x)
	{
		List<String> Follow = new ArrayList<String>();
		
		boolean finish = true;
		for(int i = 0; i < gList.size(); i++)
		{
			String [] temp = gList.get(i).prod.split(" ");
			
			for(int j = 0; j < temp.length; j++)
			{
				if(temp[j].equals(v))
				{
					finish = false;
					if(j < temp.length-1)
					{
						if(isNonTerm(temp[j+1]))
						{
							List<String> t = findFirst(temp[j+1], gList);

							for(int k = 0; k < t.size(); k++)
							{
								if(!Follow.contains(t.get(k)))
								{
									if(t.get(k).equals("empty"))
									{	
										List <String> te = findFollow(gList.get(i).term, gList, v);
										
										for(int h = 0; h < te.size(); h++)
										{
											if(!Follow.contains(te.get(h)))
											{
												Follow.add(te.get(h));
											}
										}
									}
									else
									{
										Follow.add(t.get(k));
									}
								}
							}
						}
						else
						{

							if(!Follow.contains(temp[j+1]))
							{
								Follow.add(temp[j+1]);
							}
						}
					}
					else
					{
						List<String> tFollow = new ArrayList<String>(0);
						if(!gList.get(i).term.equals(x))
						{
							tFollow = findFollow(gList.get(i).term, gList, v);
						}
						
						for(int k = 0; k < tFollow.size(); k++)
						{
							if(!Follow.contains(tFollow.get(k)))
							{
								Follow.add(tFollow.get(k));
							}
						}
					}
				}
			}
			
		}
		
		if(finish)
		{
			Follow.add("$");
		}
		
		return Follow;
	}
	
	
	
	public static Map<String, List<String>> firstList(List<Grammar> gList)
	{
		Map<String, List<String>> FirstList= new HashMap<String, List<String>>();
		
		for(int i = 0; i < gList.size(); i++)
		{
			String s = gList.get(i).term;
			
			if(!FirstList.containsKey(s))
			{
				List<String> first = findFirst(gList.get(i).term, gList);
				FirstList.put(s, first);
			}			
		}
		
		return FirstList;
	}
	
	public static List<String> findFirst(String v, List<Grammar> l)
	{
		List<String> First = new ArrayList<String>();
		
		for(int i = 0; i < l.size(); i++)
		{
			if(v.equals(l.get(i).term))
			{
				String [] s = l.get(i).prod.split(" ");
				
				if(!isNonTerm(s[0]))
				{
					First.add(s[0]);
				}
	
				if(s[0].equalsIgnoreCase("empty"))
				{
					Grammar previous = l.get(i-1);
					String [] stemp = previous.prod.split(" ");
					
					if(v.equalsIgnoreCase(stemp[0]) && stemp.length > 1)
					{
						List<String> other = findFirst(stemp[1], l);
						
						for(int j = 0; j < other.size(); j++)
						{
							First.add(other.get(j));
						}
					}		
				}
								
				if(!v.equals(s[0]))
				{	
					if(isNonTerm(s[0]))
					{
						List<String> temp = findFirst(s[0], l);
						
						for(int j = 0; j < temp.size(); j++)
						{
							if(!First.contains(temp.get(j)))
							{
								First.add(temp.get(j));
							}
							
						}
					}
				}
			}		
		}
		return First;
	}
	
	
	public static List<Grammar> getGrammar()
	{
		List<Grammar> gList = new ArrayList<Grammar>(); 
		//	ParserLibrary symbols = new ParserLibrary();

		String grammarFile = "GrammarList.txt";
		String line = null;
		try
		{
			FileReader fr = new FileReader(grammarFile);

			BufferedReader br = new BufferedReader(fr);

			while((line = br.readLine()) != null)
			{
				String [] s = line.split("::");
				String s1 = s[1].substring(1);
				gList.add(new Grammar(s[0].substring(0,s[0].length()-1), s1));
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

		return gList;
	}
}