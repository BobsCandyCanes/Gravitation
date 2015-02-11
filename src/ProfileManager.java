
import java.io.File;
import java.util.ArrayList;


public class ProfileManager
{
	private static ArrayList<Profile> profiles = new ArrayList<Profile>();

	private static Profile currentProfile;

	private static String directory = "Profiles/";

	public static void initializeProfiles()
	{
		File dir = new File(directory);
		
		for (File file : dir.listFiles()) 
		{
			if (file.getName().endsWith((".txt"))) 
			{
				profiles.add(new Profile(file));
			}
		}
		
		if(profiles.size() == 0)
		{
			createProfile("Default");
		}
		
		currentProfile = profiles.get(0);
	}

	public static void loadProfile(String s)
	{		
		for(Profile p : profiles)
		{
			if(p.getName().equals(s))
			{
				currentProfile = p;
				p.load();
				break;
			}
		}
	}

	public static void saveProfile()
	{
		System.out.println("Saving profile: " + currentProfile.getName());

		currentProfile.save();
	}	   
	
	public static void createProfile(String s)
	{
		Profile newProfile = new Profile(s);
		newProfile.save();
		profiles.add(newProfile);
		currentProfile = newProfile;
	}
	
	public static void deleteCurrentProfile()
	{
		profiles.remove(currentProfile);
		currentProfile.delete();
		
		if(profiles.size() == 0)
		{
			createProfile("Default");
		}
		else
		{
			currentProfile = profiles.get(0);
		}
	}

	public static String[] getProfileNames()
	{
		String[] names = new String[profiles.size()];
		
		for(int i = 0; i < names.length; i++)
		{
			names[i] = profiles.get(i).getName();
		}
		
		return names;
	}
	
	public static Profile getCurrentProfile()
	{
		return currentProfile;
	}
	
	public static int getHighScore()
	{
		return currentProfile.getHighScore();
	}
	
	public static void setHighScore(int i)
	{
		currentProfile.setHighScore(i);
	}
	
	public static int getExperience()
	{
		return currentProfile.getExperience();
	}
	
	public static void addExperience(int i)
	{
		currentProfile.addExperience(i);
	}
}	
