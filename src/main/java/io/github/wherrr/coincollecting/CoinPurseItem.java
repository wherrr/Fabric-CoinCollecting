package io.github.wherrr.coincollecting;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CoinPurseItem extends Item
{
	public CoinPurseItem(Settings settings)
	{
		super(settings);
	}
	
	// Holding purse and right-clicking a slot
	@Override
	public boolean onStackClicked(ItemStack purse, Slot slot, ClickType clickType, PlayerInventory playerInventory)
	{
		if (clickType != ClickType.RIGHT)
		{
			return false;
		}
		else
		{
			ItemStack slotItem = slot.getStack();
			
			// Place in an empty slot
			if (slotItem.isEmpty())
			{
				// I believe method_32756 tries to set the slot to the item passed to it
				removeRandomCoin(purse).ifPresent(slot::method_32756);
			}
			// Pick up from a non-empty slot only if it is a coin
			else if (slotItem.getItem() instanceof CoinItem)
			{
				addToPurse(purse, slot.takeStack(1));
			}
			
			return true;
		}
	}
	
	// Right-clicking the purse
	@Override
	public boolean onClicked(ItemStack purse, ItemStack heldStack, Slot slot, ClickType clickType, PlayerInventory playerInventory)
	{
		if (clickType == ClickType.RIGHT && slot.method_32754(playerInventory.player))
		{
			// Grab from the purse if hand is empty
			if (heldStack.isEmpty())
			{
				removeRandomCoin(purse).ifPresent(playerInventory::setCursorStack);
			}
			// Put item in purse if it is a coin
			else if (heldStack.getItem() instanceof CoinItem)
			{
				addToPurse(purse, heldStack);
				heldStack.decrement(1);
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static void addToPurse(ItemStack purse, ItemStack coin)
	{
		CompoundTag purseTags = purse.getOrCreateTag();
		ListTag items = new ListTag();
		
		// Add the Items tag if it doesn't exist
		if (!purseTags.contains("Items"))
		{
			purseTags.put("Items", items);
		}
		else
		{
			items = purseTags.getList("Items", 10);
		}
		
		// Write the coin to a CompoundTag and add that to the items
		CompoundTag coinTags = new CompoundTag();
		coin.writeNbt(coinTags);
		items.add(0,coinTags);
	}
	
	private Optional<ItemStack> removeRandomCoin(ItemStack purse)
	{
		CompoundTag purseTags = purse.getOrCreateTag();
		if (!purseTags.contains("Items"))
		{
			// Return nothing if the purse it empty
			return Optional.empty();
		}
		else
		{
			ListTag items = purseTags.getList("Items", 10);
			if (items.isEmpty())
			{
				return Optional.empty();
			}
			else
			{
				int numberOfItems = items.size();
				int random = new Random().nextInt(numberOfItems);
				CompoundTag item = items.getCompound(random);
				ItemStack itemStack = ItemStack.fromNbt(item);
				
				items.remove(random);
				
				if (items.isEmpty())
				{
					purse.removeSubTag("Items");
				}
				
				return Optional.of(itemStack);
			}
		}
	}
	
	private static int getPurseOccupancy(ItemStack purse) {
		CompoundTag purseTags = purse.getTag();
		if (purseTags == null) {
			return 0;
		} else {
			ListTag listTag = purseTags.getList("Items", 10);
			return listTag.size();
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack purse, World world, List<Text> tooltip, TooltipContext context)
	{
		tooltip.add(new TranslatableText("item.coin_collecting.coin_purse.fullness", getPurseOccupancy(purse)).formatted(Formatting.GOLD));
	}
}
