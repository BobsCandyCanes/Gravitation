
import java.io.File;
import java.util.ArrayList;

/**
 * Provides an interface for interacting with profiles
 * 
 * Allows saving and loading, as well as updating stats
 * 
 * Stores all currently loaded profiles in an arraylist, and keeps track of the currently selected one
 */

public class ProfileManager
{	
	//Default values
	protected final double D_MAX_HEALTH = 75;
	protected final double D_HEALTH_REGEN = 0.1;
	protected final double D_MAX_SPEED = 3;
	protected final double D_ARMOR_VALUE = 1;

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

		if(currentProfile == null)
		{
			currentProfile = profiles.get(0);
		}
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
		System.out.println("Creating profile: " + s);
		
		Profile newProfile = new Profile(s);
		newProfile.save();
		currentProfile = newProfile;
		profiles.add(newProfile);
	}

	public static void deleteCurrentProfile()
	{
		System.out.println("Deleting profile: " + currentProfile);
		
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

	public static Attribute[] getAttributes()
	{
		return currentProfile.getAttributes();
	}
	public static void updateAttributes()
	{
		currentProfile.updateAttributes();
	}
	public static double getMaxHealth()
	{
		return currentProfile.getMaxHealth();
	}
	public static void setMaxHealth(double maxHealth)
	{
		currentProfile.setMaxHealth(maxHealth);
	}
	public static double getHealthRegen()
	{
		return currentProfile.getHealthRegen();
	}
	public static void setHealthRegen(double healthRegen)
	{
		currentProfile.setHealthRegen(healthRegen);
	}
	public static double getMaxSpeed()
	{
		return currentProfile.getMaxSpeed();
	}
	public static void setMaxSpeed(double maxSpeed)
	{
		currentProfile.setMaxSpeed(maxSpeed);
	}
	public static double getArmorValue()
	{
		return currentProfile.getArmorValue();
	}
	public static void setArmorValue(double armorValue)
	{
		currentProfile.setArmorValue(armorValue);
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
