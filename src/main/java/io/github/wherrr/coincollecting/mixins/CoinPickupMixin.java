package io.github.wherrr.coincollecting.mixins;

import io.github.wherrr.coincollecting.CoinItem;
import io.github.wherrr.coincollecting.CoinPurseItem;
import io.github.wherrr.coincollecting.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.SummonCommand;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class CoinPickupMixin extends Entity
{
	public CoinPickupMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}
	
	@Shadow	private int pickupDelay;
	
	@Inject(at = @At(value = "FIELD", target = "pickupDelay"), method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V", cancellable = true)
	private void onPlayerCollision(PlayerEntity player, CallbackInfo info)
	{
		ItemEntity itemEntity = (ItemEntity) (Object) this;
		ItemStack itemStack = (itemEntity.getStack());
		Item item = itemStack.getItem();
		if (pickupDelay == 0 && item instanceof CoinItem)
		{
			// Keep track of if we pick up the coin
			boolean success = false;
			
			// Get the slot of the player's coin purse (if the have one)
			PlayerInventory playerInventory = player.getInventory();
			ItemStack newCoinPurse = new ItemStack(ModItems.COIN_PURSE);
			int coinPurseSlot = getSlotWithStackIgnoreTags(newCoinPurse, playerInventory);
			
			// If the player doesn't have a coin purse, and they're inventory isn't full, give them one
			if (coinPurseSlot == -1)
			{
				int emptySlot = playerInventory.getEmptySlot();
				
				if (emptySlot != -1)
				{
					CoinPurseItem.addToPurse(newCoinPurse, itemStack);
					playerInventory.insertStack(emptySlot, newCoinPurse);
					
					success = true;
				}
			}
			// If the player does have a coin purse, add the coin to it
			else
			{
				ItemStack inventoryCoinPurse = playerInventory.getStack(coinPurseSlot);
				
				CoinPurseItem.addToPurse(inventoryCoinPurse, itemStack);
				
				success = true;
			}
			
			// If the player successfully picked up the coin, delete it, play an animation and sound, and handle other triggers
			if (success)
			{
				itemStack.setCount(0);
				
				player.sendPickup(itemEntity, 1);
				
				player.increaseStat(Stats.PICKED_UP.getOrCreateStat(item), 1);
				player.triggerItemPickedUpByEntityCriteria(itemEntity);
			}
		}
	}
	
	@Environment(EnvType.CLIENT)
	public int getSlotWithStackIgnoreTags(ItemStack stack, PlayerInventory playerInventory)
	{
		for(int i = 0; i < playerInventory.main.size(); ++i)
		{
			ItemStack slotStack = (ItemStack)playerInventory.main.get(i);
			if (!slotStack.isEmpty() && stack.isOf(slotStack.getItem()))
			{
				return i;
			}
		}
		
		return -1;
	}
}
