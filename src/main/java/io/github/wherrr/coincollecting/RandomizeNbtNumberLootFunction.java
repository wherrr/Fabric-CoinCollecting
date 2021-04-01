package io.github.wherrr.coincollecting;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.JsonHelper;

import java.util.Arrays;
import java.util.List;

public class RandomizeNbtNumberLootFunction extends ConditionalLootFunction
{
	// target will be a string that is . delimited
	private final String target;
	private final UniformLootNumberProvider range;
	
	private RandomizeNbtNumberLootFunction(LootCondition[] conditions, String target, UniformLootNumberProvider range)
	{
		super(conditions);
		this.target = target;
		this.range = range;
	}
	
	@Override
	protected ItemStack process(ItemStack stack, LootContext context)
	{
		// Split the target into an array based on a . delimiter
		String[] targetArray = target.split("[.]");
		
		// Define things about the target
		int targetDepth = targetArray.length - 1;
		String targetName = targetArray[targetDepth];
		
		// Find the target
		NbtCompound currentCompound = stack.getTag();
		
		if (currentCompound != null)
		{
			for (int i = 0; i < targetDepth; i++)
			{
				currentCompound = currentCompound.getCompound(targetArray[i]);
			}
			
			// Handle randomizing the target
			NbtElement targetElement = currentCompound.get(targetName);
			if (targetElement != null)
			{
				float random = range.nextFloat(context);
				switch (targetElement.getType())
				{
					case 1:
						currentCompound.putByte(targetName, (byte) random);
						break;
					case 2:
						currentCompound.putShort(targetName, (short) random);
						break;
					case 3:
						currentCompound.putInt(targetName, (int) random);
						break;
					case 4:
						currentCompound.putLong(targetName, (long) random);
						break;
					case 5:
						currentCompound.putFloat(targetName, random);
						break;
					case 6:
						currentCompound.putDouble(targetName, random);
						break;
				}
			}
		}
		
		return stack;
	}
	
	public static Builder<?> builder(String target, UniformLootNumberProvider range)
	{
		return builder((conditions) -> new RandomizeNbtNumberLootFunction(conditions, target, range));
	}
	
	@Override
	public LootFunctionType getType()
	{
		return null;
	}
	
	public static class Serializer extends ConditionalLootFunction.Serializer<RandomizeNbtNumberLootFunction>
	{
		public Serializer() {}
		
		public void toJson(JsonObject json, RandomizeNbtNumberLootFunction lootFunction, JsonSerializationContext context)
		{
			super.toJson(json, lootFunction, context);
			json.addProperty("target", lootFunction.target);
			json.add("range", context.serialize(lootFunction.range));
		}
		
		public RandomizeNbtNumberLootFunction fromJson(JsonObject json, JsonDeserializationContext context, LootCondition[] lootConditions)
		{
			String target = JsonHelper.getString(json, "target");
			UniformLootNumberProvider range = JsonHelper.deserialize(json, "range", context, UniformLootNumberProvider.class);
			return new RandomizeNbtNumberLootFunction(lootConditions, target, range);
		}
	}
}
