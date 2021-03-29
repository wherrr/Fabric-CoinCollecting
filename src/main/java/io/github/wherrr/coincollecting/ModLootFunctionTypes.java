package io.github.wherrr.coincollecting;

import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.registry.Registry;

public class ModLootFunctionTypes
{
	public static final LootFunctionType RANDOMIZE_NBT_NUMBER = new LootFunctionType(new RandomizeNbtNumberLootFunction.Serializer());
	
	private static void registerLootFunctionType(String identifierPath, LootFunctionType lootFunctionType)
	{
		Registry.register(Registry.LOOT_FUNCTION_TYPE, new Identifier(Main.MOD_ID, identifierPath), lootFunctionType);
	}
	
	public static void registerLootFunctionTypes()
	{
		registerLootFunctionType("randomize_nbt_number", RANDOMIZE_NBT_NUMBER);
	}
}
