package io.github.wherrr.coincollecting;

import java.util.Random;

public enum CoinMint
{
	PlainsVillage,
	DesertVillage,
	SavannaVillage,
	SnowyVillage,
	TaigaVillage;
	
	public static CoinMint getRandom()
	{
		return CoinMint.values()[new Random().nextInt(CoinMint.values().length)];
	}
}
