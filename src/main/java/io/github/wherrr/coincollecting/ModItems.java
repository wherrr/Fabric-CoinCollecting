package io.github.wherrr.coincollecting;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems
{
	public static final Coin COIN = new Coin(new FabricItemSettings().group(Main.COIN_COLLECTING_TAB).maxCount(1));
	public static final CoinPurseItem COIN_PURSE = new CoinPurseItem(new FabricItemSettings().group(Main.COIN_COLLECTING_TAB).maxCount(1));
	
	public static <T extends Item> void registerItem(String identifierPath, T item)
	{
		Registry.register(Registry.ITEM, new Identifier(Main.MOD_ID, identifierPath), item);
	}
	
	public static void registerItems()
	{
		registerItem("coin", COIN);
		registerItem("coin_purse", COIN_PURSE);
	}
	
	
}