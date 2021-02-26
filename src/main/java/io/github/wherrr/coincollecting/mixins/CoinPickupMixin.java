package io.github.wherrr.coincollecting.mixins;

import io.github.wherrr.coincollecting.CoinItem;
import io.github.wherrr.coincollecting.CoinPurseItem;
import io.github.wherrr.coincollecting.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
		ItemStack itemStack = ((ItemEntity) (Object) this).getStack();
		Item item = itemStack.getItem();
		if (pickupDelay == 0 && item instanceof CoinItem)
		{
			// Get the slot of the player's coin purse (if the have one)
			PlayerInventory playerInventory = player.getInventory();
			ItemStack newCoinPurse = new ItemStack(ModItems.COIN_PURSE);
			int coinPurseSlot = playerInventory.getSlotWithStack(newCoinPurse);
			
			// If the player doesn't have a coin purse, give them one
			if (coinPurseSlot == -1)
			{
				CoinPurseItem.addToPurse(newCoinPurse, itemStack);
				
				playerInventory.insertStack(newCoinPurse);
			}
			// If the player does have a coin purse, add the coin to it
			else
			{
				ItemStack inventoryCoinPurse = playerInventory.getStack(coinPurseSlot);
				
				CoinPurseItem.addToPurse(inventoryCoinPurse, itemStack);
			}
			
			itemStack.setCount(0);
		}
	}
}
