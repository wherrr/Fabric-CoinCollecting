package io.github.wherrr.coincollecting;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.item.Item;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetNbtLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModItems
{
	public static final CoinItem OVERWORLD_COIN = new CoinItem(new FabricItemSettings().group(Main.COIN_COLLECTING_TAB).maxCount(1).fireproof());
	public static final CoinPurseItem COIN_PURSE = new CoinPurseItem(new FabricItemSettings().group(Main.COIN_COLLECTING_TAB).maxCount(1).fireproof());
	
	private static <T extends Item> void registerItem(String identifierPath, T item)
	{
		Registry.register(Registry.ITEM, new Identifier(Main.MOD_ID, identifierPath), item);
	}
	
	public static void registerItems()
	{
		registerItem("overworld_coin", OVERWORLD_COIN);
		registerItem("coin_purse", COIN_PURSE);
	}
	
	public static void registerLootTables()
	{
		final Identifier ZOMBIE_LOOT_TABLE_ID = new Identifier("minecraft","entities/zombie");
		
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) ->
		{
			if (ZOMBIE_LOOT_TABLE_ID.equals(id))
			{
				FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
						.rolls(ConstantLootNumberProvider.create(1))
						.withCondition(RandomChanceWithLootingLootCondition.builder(0.10f, 0.05f).build())
						.with(ItemEntry.builder(OVERWORLD_COIN))
						.withFunction(SetNbtLootFunction.builder(CoinItem.generateEmptyTags()).build())
						.withFunction(RandomizeNbtNumberLootFunction.builder("CoinInfo.Year.Value", CoinItem.YEAR_RANGE).build())
						.withFunction(RandomizeNbtStringLootFunction.builder("CoinInfo.Mint.Value", CoinMints.getNames(CoinMints.COIN_MINT_OVERWORLD)).build())
						.withFunction(RandomizeNbtNumberLootFunction.builder("CoinInfo.Quality.Scratches", UniformLootNumberProvider.create(0, 1)).build())
						.withFunction(RandomizeNbtNumberLootFunction.builder("CoinInfo.Quality.BigScratches", UniformLootNumberProvider.create(0, 1)).build())
						.withFunction(RandomizeNbtNumberLootFunction.builder("CoinInfo.Cleanliness.Dirt", UniformLootNumberProvider.create(0, 1)).build());
				
				supplier.withPool(poolBuilder.build());
			}
		});
	}
}