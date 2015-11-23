package cs488;


public class Item 
{
	
	


}

//Copy that SOMEWHAT WORKS
/*
public static void it(List<Grammar> gList)
{		
	List<Grammar> newList = new ArrayList<Grammar>();
	List<String> NonTerm = new ArrayList<String>();
	List<String> Prod = new ArrayList<String>();
	List<String> checked = new ArrayList<String>();
	Map<Integer, List<String>> itemsss = new HashMap<Integer,List<String>>();

	newList.add(new Grammar(gList.get(0).term + "'", gList.get(0).term));
	for(int i = 0; i < gList.size(); i++)
	{
		newList.add(gList.get(i));	
	}
	
	boolean cont = true;
	
	itemsss.put(0, produce(newList.get(0).term, newList, ""));
	List<String> tempList = itemsss.get(0);
	for(int i = 0; i < tempList.size(); i++)
	{
		if((i%2) == 0)
		{
			NonTerm.add(tempList.get(i));
		}
		else if((i%2) == 1)
		{
			Prod.add(tempList.get(i));
			checked.add(tempList.get(i));
		}
	}
	printMap(itemsss);
	
	System.out.println("Prod " + Prod);
	//while loop is useless for now, but not the stuff inside
	
	while(cont)
	{
		//contains current item's strings
		List<String> checker = new ArrayList<String>();
		for(int i = 0; i < Prod.size(); i++)
		{
			String [] prod = Prod.get(i).split(" ");
			String check = "";
			String c = "";
			String t = "";
			boolean skip = false;
			for(int j = 0; j < prod.length; j++)
			{
				if(prod[j].equals("."))
				{
					if(j < prod.length-1)
					{
						check += prod[j+1] + " ";
						c = prod[j+1];
						t = NonTerm.get(i);
						check += ".";
						j++;
					}
					
				}
				else
				{
					check += prod[j];
				}
				if(j < prod.length-1)
				{
					check += " ";
				}
			}

			if(!checked.contains(check))
			{
				checker.add(NonTerm.get(i));
				checker.add(check);
			}

			//Looks through grammar
			for(int j = 0; j < newList.size(); j++)
			{
				//String after . has been moved
				check = "";
				//adds only if it isnt already on the list
				boolean yes = true;
				//if it is 
				
				if(j%2 == 1)
				{
					String s = newList.get(j).prod;
					if(!newList.get(j-1).term.equals(t) && checker.size() == 0)
					{
						skip = true;
					}
					
					if(s.contains(c))
					{
						prod = s.split(" ");
						
						for(int k = 0; k < prod.length; k++)
						{
							
							if(prod[k].equals(c))
							{
								check += prod[k];
								for(int l = 0; l < checker.size(); l++)
								{
									if(!checker.get(l).contains(check) && l%2 == 1)
									{
										yes = false;
									}
								}
								check += " .";
								
							}
							else
							{
								check += prod[k];
							}
							//adds blank space if there is another character
							if(k < prod.length-1)
							{
								check += " ";
							}
						}
						
						if(!checker.contains(check) && check.contains(".") && yes && !skip)
						{
							if(!checked.contains(check))
							{
								checker.add(newList.get(j).term);
								checker.add(check);
							}
							
							String [] te = check.split(" ");
							if(!te[0].equals("."))
							{
								checked.add(check);
							}
						}
					}
				}
			}
			if(skip)
			{
				continue;
			}
			
			System.out.println(checker);
			System.out.println("END");
			checker.clear();

		}
		cont = false;
	}		
}

*/