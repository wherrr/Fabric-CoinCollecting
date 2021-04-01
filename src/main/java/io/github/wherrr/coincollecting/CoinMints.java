package io.github.wherrr.coincollecting;

import java.util.Random;

public class CoinMints
{
	// CoinMint array declarations
	public static final CoinMint[] COIN_MINT_OVERWORLD =
	{
			new CoinMint("Plains Village","PV"),
			new CoinMint("Desert Village","DV"),
			new CoinMint("Savanna Village","SAV"),
			new CoinMint("Snowy Village","SNV"),
			new CoinMint("Taiga VIllage","TV")
	};
	
	// Convert a CoinMint array to a String array using CoinMint.name
	public static String[] getNames(CoinMint[] coinMints)
	{
		String[] names = new String[coinMints.length];
		
		for (int i = 0; i < coinMints.length; i++)
		{
			names[i] = coinMints[i].name;
		}
		
		return names;
	}
	
	public static CoinMint getRandom(CoinMint[] coinMints)
	{
		return coinMints[new Random().nextInt(coinMints.length)];
	}
}
