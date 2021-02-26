package io.github.wherrr.coincollecting;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class Main implements ModInitializer
{
	public static final String MOD_ID = "coin_collecting";
	public static final ItemGroup COIN_COLLECTING_TAB = FabricItemGroupBuilder.build(new Identifier(Main.MOD_ID, "general"), () -> new ItemStack(ModItems.OVERWORLD_COIN));
	
	@Override
	public void onInitialize()
	{
		ModItems.registerItems();
		ModBlocks.registerBlocks();
	}
}