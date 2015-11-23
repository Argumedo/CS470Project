package cs488;



public class LRItemsCreator {
	public static void main(String[] args) 
	{
		
	}

}

/*Thursday night 7:23pm
 * 		//replaces prod by moving the  . 
		for(int i = 0; i < Prod.size(); i++)
		{			
			String t = shift(Prod.get(i));
			if(t.length() > 0 && !Prod.contains(t))
			{
				if(i < Prod.size())
				{
					Prod.set(i, t);
				}
			}
		}
		// adds the remaining Prods with . to end
		for(int i = 0; i < Prod.size(); i++)
		{			
			String t = shift(Prod.get(i));
			if(t.length() > 0 && !Prod.contains(t))
			{
				NonTerm.add(NonTerm.get(i));
				Prod.add(t);
			}
		}
		
 * 
 * 
 */

/* Produce before I messed with it. Thursday 10:55PM

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

					String [] t = newList.get(i).prod.split(" ");
					
					if(isNonTerm(t[0]) && !t[0].equals(avoid))
					{
						
						List<String> recurse = produce(t[0], newList, v);
						
						for(int l = 0; l < recurse.size(); l++)
						{

							if((l%2) == 1)
							{
								boolean y = false;
								if(!Productions.contains(recurse.get(l)))
								{
									y = true;
								}
								else
								{
									String tProd = recurse.get(l);
									String nonT = Productions.get(Productions.indexOf(tProd)-1);
									if(!nonT.equals(recurse.get(l-1)))
									{
										y = true;
									}
								}
								if(y)
								{

									Productions.add(recurse.get(l-1));
									Productions.add(recurse.get(l));
								}
							}
						}
					}
				}
			}
		}

	
		return Productions;
	}
	

*/
