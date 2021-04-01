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

import java.util.Random;

public class RandomizeNbtStringLootFunction extends ConditionalLootFunction
{
	// target will be a string that is . delimited
	private final String target;
	private final String[] strings;
	
	private RandomizeNbtStringLootFunction(LootCondition[] conditions, String target, String[] strings)
	{
		super(conditions);
		this.target = target;
		this.strings = strings;
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
			if (targetElement != null && targetElement.getType() == 8)
			{
				String randomString = strings[new Random().nextInt(strings.length)];
				
				currentCompound.putString(targetName, randomString);
			}
		}
		
		return stack;
	}
	
	public static Builder<?> builder(String target, String[] strings)
	{
		return builder((conditions) -> new RandomizeNbtStringLootFunction(conditions, target, strings));
	}
	
	@Override
	public LootFunctionType getType()
	{
		return null;
	}
	
	public static class Serializer extends ConditionalLootFunction.Serializer<RandomizeNbtStringLootFunction>
	{
		public Serializer() {}
		
		public void toJson(JsonObject json, RandomizeNbtStringLootFunction lootFunction, JsonSerializationContext context)
		{
			super.toJson(json, lootFunction, context);
			json.addProperty("target", lootFunction.target);
			json.add("strings", context.serialize(lootFunction.strings));
		}
		
		public RandomizeNbtStringLootFunction fromJson(JsonObject json, JsonDeserializationContext context, LootCondition[] lootConditions)
		{
			String target = JsonHelper.getString(json, "target");
			String[] strings = JsonHelper.deserialize(json, "strings", context, String[].class);
			return new RandomizeNbtStringLootFunction(lootConditions, target, strings);
		}
	}
}
