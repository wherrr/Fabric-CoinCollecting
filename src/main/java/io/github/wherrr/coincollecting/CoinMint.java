package io.github.wherrr.coincollecting;

import net.minecraft.nbt.Tag;

import java.util.Locale;
import java.util.Random;

public enum CoinMint
{
	PLAINS_VILLAGE("PV"),
	DESERT_VILLAGE("DV"),
	SAVANNA_VILLAGE("SAV"),
	SNOWY_VILLAGE("SNV"),
	TAIGA_VILLAGE("TV");
	
	private final String shortName;
	
	CoinMint()
	{
		this.shortName = "";
	}
	
	CoinMint(String shortName)
	{
		this.shortName = shortName;
	}
	
	public String getShortName()
	{
		return this.shortName;
	}
	
	@Override
	public String toString()
	{
		String name = super.toString();
		StringBuilder newName = new StringBuilder(name.toLowerCase());
		
		newName.setCharAt(0, Character.toUpperCase(newName.charAt(0)));
		
		for (int i = 1; i < name.length() - 1; i++)
		{
			if (name.charAt(i - 1) == '_')
			{
				newName.setCharAt(i - 1, ' ');
				newName.setCharAt(i, Character.toUpperCase(newName.charAt(i)));
			}
		}
		
		return newName.toString();
	}
	
	// Checks if the given name is in the enum and returns it if it is. Otherwise, it returns NA
	public static String checkName(String name)
	{
		try
		{
			CoinMint.valueOf(name);
		}
		catch(Exception ignored)
		{
			return "NA";
		}
		
		return name;
	}
	
	// Checks if the given tag is in the enum and returns it if it is. Otherwise, it returns a string tag with "NA"
	public static Tag checkTag(Tag tag)
	{
		try
		{
			CoinMint.valueOf(tag.asString());
		}
		catch(Exception ignored)
		{
			return null;
		}
		
		return tag;
	}
	
	public static CoinMint getRandom()
	{
		return CoinMint.values()[new Random().nextInt(CoinMint.values().length)];
	}
}
