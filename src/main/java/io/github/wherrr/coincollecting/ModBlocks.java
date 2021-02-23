package io.github.wherrr.coincollecting;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks
{
	public static final Block COIN_GRADER = new Block(FabricBlockSettings
			.of(Material.METAL)
			.breakByTool(FabricToolTags.PICKAXES, 1)
			.requiresTool()
			.strength(3.5f, 5.83f)
			.sounds(BlockSoundGroup.METAL));
	
	public static <T extends Block> void registerBlock(String identifierPath, T block, FabricItemSettings itemSettings)
	{
		Registry.register(Registry.BLOCK, new Identifier(Main.MOD_ID, identifierPath), block);
		Registry.register(Registry.ITEM, new Identifier(Main.MOD_ID, identifierPath), new BlockItem(block, itemSettings));
	}
	
	public static void registerBlocks()
	{
		registerBlock("coin_grader", COIN_GRADER, new FabricItemSettings().group(Main.COIN_COLLECTING_TAB));
	}
}