
/**
 * Attribute is a wrapper class for the four upgradeable values
 * The wrapper is necessary so you can share the reference between the game and the profile
 * It also makes it easier to save and load profiles
 */

public class Attribute
{
	String name;
	double value, min, max, increment;

	public Attribute(String name)
	{
		//Good lord this is awful, but it works and I'm sick of dealing with it
		//Don't judge me
		
		if(name.equals("Health"))
		{
			value = 75;
			max = 120;
			min = 75;
			increment = 5;
		}
		else if(name.equals("Regen"))
		{
			value = 0.1;
			max = 0.2;
			min = 0.1;
			increment = 0.01;
		}
		else if(name.equals("Speed"))
		{
			value = 3;
			max = 4;
			min = 3;
			increment = 0.1;
		}
		else if(name.equals("Armor"))
		{
			value = 1;
			max = 5;
			min = 1;
			increment = 0.5;
		}
		
		this.name = name;
	}
	
	public double getValue()
	{
		return value;
	}
	
	public String getName()
	{
		return name;
	}

	public double getMin()
	{
		return min;
	}

	public double getMax()
	{
		return max;
	}

	public double getIncrement()
	{
		return increment;
	}

	public void setValue(double v)
	{
		value = v;
	}
}
