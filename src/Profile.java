import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Profile
{
	private String name;
	private String filePath;
	private int highScore;
	private int experience;
	
	//Custom values
	protected double maxHealth = 75;
	protected double healthRegen = 0.1;
	protected double maxSpeed = 3;
	protected double armorValue = 1;
	
	protected Attribute[] attributes =
			{
				new Attribute("Health"),
				new Attribute("Regen"),
				new Attribute("Speed"),
				new Attribute("Armor")
			};

	public Profile(String name)
	{
		this.name = name;
		filePath = "Profiles/" + name + ".txt";
	}
	
	public Profile(File f)
	{
		String fileName = f.getName();
		
		name = fileName.replace(".txt", "");
		filePath = "Profiles/" + fileName;
		load();
	}

	public void save()
	{
		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath));

			out.write(name + "\n");
			out.write("High Score: "   + highScore   + "\n");
			out.write("Experience: "   + experience  + "\n");  
			out.write("Max Health: "   + maxHealth   + "\n");
			out.write("Health Regen: " + healthRegen + "\n");  
			out.write("Max Speed: "    + maxSpeed    + "\n");
			out.write("Armor Value: "  + armorValue  + "\n");  

			out.close();
		} 
		catch(FileNotFoundException ex) 
		{
			System.out.println("Unable to open file: " + filePath);                
		}
		catch(IOException ex) 
		{
			System.out.println("Error reading file: " + filePath);                   
		}
	}
	
	public void load()
	{
		System.out.println("Loading profile: " + name);

		try 
		{
			FileReader fileReader = new FileReader(filePath);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while((bufferedReader.readLine()) != null) 
			{  	
				//Read in Strings and parse for number values
				
				String highScoreString = bufferedReader.readLine();
				highScoreString = highScoreString.replace("High Score: ", "");
				highScore = Integer.parseInt(highScoreString); 

				String experienceString = bufferedReader.readLine();
				experienceString = experienceString.replace("Experience: ", "");
				experience = Integer.parseInt(experienceString);
				
				String maxHealthString = bufferedReader.readLine();
				maxHealthString = maxHealthString.replace("Max Health: ", "");
				maxHealth = Double.parseDouble(maxHealthString);
				attributes[0].setValue(maxHealth);
				
				String healthRegenString = bufferedReader.readLine();
				healthRegenString = healthRegenString.replace("Health Regen: ", "");
				healthRegen = Double.parseDouble(healthRegenString);
				attributes[1].setValue(healthRegen);
				
				String maxSpeedString = bufferedReader.readLine();
				maxSpeedString = maxSpeedString.replace("Max Speed: ", "");
				maxSpeed = Double.parseDouble(maxSpeedString);
				attributes[2].setValue(maxSpeed);
				
				String armorValueString = bufferedReader.readLine();
				armorValueString = armorValueString.replace("Armor Value: ", "");
				armorValue = Double.parseDouble(armorValueString);
				attributes[3].setValue(armorValue);
			}
			bufferedReader.close();  
		}    
		catch(FileNotFoundException ex) 
		{
			System.out.println("Unable to open file: " + filePath);                
		}
		catch(IOException ex) 
		{
			System.out.println("Error reading file: " + filePath);                   
		}
	}
	
	public void delete()
	{
    	try
    	{	 
    		File file = new File(filePath);
 
    		if(file.delete())
    		{
    			System.out.println("Deleted " + file.getName());
    		}
    		else
    		{
    			System.out.println("Failed to delete " + file.getName());
    		}
 
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}	
	}
	
	public void updateAttributes()
	{
		maxHealth   = attributes[0].getValue();
		healthRegen = attributes[1].getValue();
		maxSpeed    = attributes[2].getValue();
		armorValue  = attributes[3].getValue();
	}
	
	public Attribute[] getAttributes()
	{
		return attributes;
	}
	public double getMaxHealth()
	{
		return maxHealth;
	}
	public void setMaxHealth(double maxHealth)
	{
		this.maxHealth = maxHealth;
	}
	public double getHealthRegen()
	{
		return healthRegen;
	}
	public void setHealthRegen(double healthRegen)
	{
		this.healthRegen = healthRegen;
	}
	public double getMaxSpeed()
	{
		return maxSpeed;
	}
	public void setMaxSpeed(double maxSpeed)
	{
		this.maxSpeed = maxSpeed;
	}
	public double getArmorValue()
	{
		return armorValue;
	}
	public void setArmorValue(double armorValue)
	{
		this.armorValue = armorValue;
	}
	public String getName()
	{
		return name;
	}
	public int getHighScore()
	{
		return highScore;
	}
	public void setHighScore(int i)
	{
		highScore = i;
	}
	public int getExperience()
	{
		return experience;
	}
	public void addExperience(int i)
	{
		experience += i;
	}
	public String toString()
	{
		return name;
	}
}

