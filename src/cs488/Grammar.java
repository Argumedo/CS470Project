package cs488;

public class Grammar 
{
	String term; 
	String prod;
	
	public Grammar(String term, String prod)
	{
		this.term = term;
		this.prod = prod;
	}
	
	public String compareT(String t)
	{
		if (t.equalsIgnoreCase(term))
		{
			return term;
		}
		else if(t.equalsIgnoreCase(prod))
		{
			return prod;
		}
		
		return null;
	}

	public void print()
	{
		System.out.println("Term: " + term + " Production: " + prod);
	}
}
