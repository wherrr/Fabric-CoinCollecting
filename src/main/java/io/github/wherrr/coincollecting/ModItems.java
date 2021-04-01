package io.github.wherrr.coincollecting;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.item.Item;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootTableEntry;
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
	
	public static void registerCoinLootTable(Identifier editTable, Identifier mergeTable, CoinItem coin, CoinMint[] coinMint)
	{
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) ->
		{
			if (editTable.equals(id))
			{
				
				FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
						.with(LootTableEntry.builder(mergeTable))
						.with(ItemEntry.builder(coin))
						.withFunction(SetNbtLootFunction.builder(CoinItem.generateEmptyTags()).build())
						.withFunction(RandomizeNbtNumberLootFunction.builder("CoinInfo.Year.Value", CoinItem.YEAR_RANGE).build())
						.withFunction(RandomizeNbtStringLootFunction.builder("CoinInfo.Mint.Value", CoinMints.getNames(coinMint)).build())
						.withFunction(RandomizeNbtNumberLootFunction.builder("CoinInfo.Quality.Scratches", UniformLootNumberProvider.create(0, 1)).build())
						.withFunction(RandomizeNbtNumberLootFunction.builder("CoinInfo.Quality.BigScratches", UniformLootNumberProvider.create(0, 1)).build())
						.withFunction(RandomizeNbtNumberLootFunction.builder("CoinInfo.Quality.Luster", UniformLootNumberProvider.create(0, 1)).build())
						.withFunction(RandomizeNbtNumberLootFunction.builder("CoinInfo.Cleanliness.Dirt", UniformLootNumberProvider.create(0, 1)).build());
				
				supplier.withPool(poolBuilder.build());
			}
		});
	}
	
	public static void registerLootTables()
	{
		final Identifier CREEPER_LOOT_TABLE_ID = new Identifier("minecraft","entities/creeper");
		final Identifier DROWNED_LOOT_TABLE_ID = new Identifier("minecraft","entities/drowned");
		final Identifier ENDERMAN_LOOT_TABLE_ID = new Identifier("minecraft","entities/enderman");
		final Identifier EVOKER_LOOT_TABLE_ID = new Identifier("minecraft","entities/evoker");
		final Identifier HUSK_LOOT_TABLE_ID = new Identifier("minecraft","entities/husk");
		final Identifier PILLAGER_LOOT_TABLE_ID = new Identifier("minecraft","entities/pillager");
		final Identifier SKELETON_LOOT_TABLE_ID = new Identifier("minecraft","entities/skeleton");
		final Identifier STRAY_LOOT_TABLE_ID = new Identifier("minecraft","entities/stray");
		final Identifier VINDICATOR_LOOT_TABLE_ID = new Identifier("minecraft","entities/vindicator");
		final Identifier WITCH_LOOT_TABLE_ID = new Identifier("minecraft","entities/witch");
		final Identifier ZOMBIE_LOOT_TABLE_ID = new Identifier("minecraft","entities/zombie");
		final Identifier ZOMBIE_VILLAGER_LOOT_TABLE_ID = new Identifier("minecraft","entities/zombie_villager");
		final Identifier CREEPER_OVERWORLD_COIN_LOOT_TABLE_ID = new Identifier(Main.MOD_ID,"shared/creeper_overworld_coin");
		final Identifier DROWNED_OVERWORLD_COIN_LOOT_TABLE_ID = new Identifier(Main.MOD_ID,"shared/drowned_overworld_coin");
		final Identifier ENDERMAN_OVERWORLD_COIN_LOOT_TABLE_ID = new Identifier(Main.MOD_ID,"shared/enderman_overworld_coin");
		final Identifier EVOKER_OVERWORLD_COIN_LOOT_TABLE_ID = new Identifier(Main.MOD_ID,"shared/evoker_overworld_coin");
		final Identifier HUSK_OVERWORLD_COIN_LOOT_TABLE_ID = new Identifier(Main.MOD_ID,"shared/husk_overworld_coin");
		final Identifier PILLAGER_OVERWORLD_COIN_LOOT_TABLE_ID = new Identifier(Main.MOD_ID,"shared/pillager_overworld_coin");
		final Identifier SKELETON_OVERWORLD_COIN_LOOT_TABLE_ID = new Identifier(Main.MOD_ID,"shared/skeleton_overworld_coin");
		final Identifier STRAY_OVERWORLD_COIN_LOOT_TABLE_ID = new Identifier(Main.MOD_ID,"shared/stray_overworld_coin");
		final Identifier VINDICATOR_OVERWORLD_COIN_LOOT_TABLE_ID = new Identifier(Main.MOD_ID,"shared/vindicator_overworld_coin");
		final Identifier WITCH_OVERWORLD_COIN_LOOT_TABLE_ID = new Identifier(Main.MOD_ID,"shared/witch_overworld_coin");
		final Identifier ZOMBIE_OVERWORLD_COIN_LOOT_TABLE_ID = new Identifier(Main.MOD_ID,"shared/zombie_overworld_coin");
		final Identifier ZOMBIE_VILLAGER_OVERWORLD_COIN_LOOT_TABLE_ID = new Identifier(Main.MOD_ID,"shared/zombie_villager_overworld_coin");
		
		registerCoinLootTable(CREEPER_LOOT_TABLE_ID, CREEPER_OVERWORLD_COIN_LOOT_TABLE_ID, OVERWORLD_COIN, CoinMints.COIN_MINT_OVERWORLD);
		registerCoinLootTable(DROWNED_LOOT_TABLE_ID, DROWNED_OVERWORLD_COIN_LOOT_TABLE_ID, OVERWORLD_COIN, CoinMints.COIN_MINT_OVERWORLD);
		registerCoinLootTable(ENDERMAN_LOOT_TABLE_ID, ENDERMAN_OVERWORLD_COIN_LOOT_TABLE_ID, OVERWORLD_COIN, CoinMints.COIN_MINT_OVERWORLD);
		registerCoinLootTable(EVOKER_LOOT_TABLE_ID, EVOKER_OVERWORLD_COIN_LOOT_TABLE_ID, OVERWORLD_COIN, CoinMints.COIN_MINT_OVERWORLD);
		registerCoinLootTable(HUSK_LOOT_TABLE_ID, HUSK_OVERWORLD_COIN_LOOT_TABLE_ID, OVERWORLD_COIN, CoinMints.COIN_MINT_OVERWORLD);
		registerCoinLootTable(PILLAGER_LOOT_TABLE_ID, PILLAGER_OVERWORLD_COIN_LOOT_TABLE_ID, OVERWORLD_COIN, CoinMints.COIN_MINT_OVERWORLD);
		registerCoinLootTable(SKELETON_LOOT_TABLE_ID, SKELETON_OVERWORLD_COIN_LOOT_TABLE_ID, OVERWORLD_COIN, CoinMints.COIN_MINT_OVERWORLD);
		registerCoinLootTable(STRAY_LOOT_TABLE_ID, STRAY_OVERWORLD_COIN_LOOT_TABLE_ID, OVERWORLD_COIN, CoinMints.COIN_MINT_OVERWORLD);
		registerCoinLootTable(VINDICATOR_LOOT_TABLE_ID, VINDICATOR_OVERWORLD_COIN_LOOT_TABLE_ID, OVERWORLD_COIN, CoinMints.COIN_MINT_OVERWORLD);
		registerCoinLootTable(WITCH_LOOT_TABLE_ID, WITCH_OVERWORLD_COIN_LOOT_TABLE_ID, OVERWORLD_COIN, CoinMints.COIN_MINT_OVERWORLD);
		registerCoinLootTable(ZOMBIE_LOOT_TABLE_ID, ZOMBIE_OVERWORLD_COIN_LOOT_TABLE_ID, OVERWORLD_COIN, CoinMints.COIN_MINT_OVERWORLD);
		registerCoinLootTable(ZOMBIE_VILLAGER_LOOT_TABLE_ID, ZOMBIE_VILLAGER_OVERWORLD_COIN_LOOT_TABLE_ID, OVERWORLD_COIN, CoinMints.COIN_MINT_OVERWORLD);
	}
}