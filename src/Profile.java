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
			out.write("High Score: " + highScore + "\n");
			out.write("Experience: " + experience + "\n");  

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
				String highScoreString = bufferedReader.readLine();
				highScoreString = highScoreString.replace("High Score: ", "");
				highScore = Integer.parseInt(highScoreString); 

				String experienceString = bufferedReader.readLine();
				experienceString = experienceString.replace("Experience: ", "");
				
				experience = Integer.parseInt(experienceString);
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
    	try{
    		 
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
}
