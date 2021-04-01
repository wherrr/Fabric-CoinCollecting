package io.github.wherrr.coincollecting;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.time.Year;
import java.util.*;

public class CoinItem extends Item
{
	// Year between 1.0 release date and current date
	public static final UniformLootNumberProvider YEAR_RANGE = UniformLootNumberProvider.create(2011, Year.now().getValue());
	
	// This map defines how much each info tag affects the quality (not all tags do)
	public static final Map<String, Integer> QUALITY_TAGS;
	
	static
	{
		Map<String, Integer> map = new HashMap<>();
		map.put("Scratches", -1);
		map.put("BigScratches", -3);
		map.put("Luster", 1);
		
		QUALITY_TAGS = Collections.unmodifiableMap(map);
	}
	
	// This map defines how much each info tag affects the cleanliness (not all tags do)
	public static final Map<String, Integer> CLEAN_TAGS;
	
	static
	{
		Map<String, Integer> map = new HashMap<>();
		map.put("Dirt", -1);
		
		CLEAN_TAGS = Collections.unmodifiableMap(map);
	}
	
	public CoinItem(Settings settings)
	{
		super(settings);
	}
	
	public static NbtCompound generateEmptyTags()
	{
		// Main tags
		NbtCompound compoundTag = new NbtCompound();
		NbtCompound coinInfo = new NbtCompound();
		
		// Info tags
		NbtCompound year = new NbtCompound();
		NbtCompound mint = new NbtCompound();
		NbtCompound quality = new NbtCompound();
		NbtCompound cleanliness = new NbtCompound();
		
		// Set all values to disabled by default
		year.putBoolean("Enabled", true);
		mint.putBoolean("Enabled", true);
		quality.putBoolean("Enabled", true);
		cleanliness.putBoolean("Enabled", true);
		
		// Set values to undefined
		year.putInt("Value", -1);
		mint.putString("Value", "undefined");
		
		// Put qualities into the quality info
		quality.putFloat("Scratches", -1f);
		quality.putFloat("BigScratches", -1f);
		quality.putFloat("Luster", -1f);
		
		// Put clean qualities into the cleanliness info
		cleanliness.putFloat("Dirt", -1f);
		
		// Put info into the coin info
		coinInfo.put("Year", year);
		coinInfo.put("Mint", mint);
		coinInfo.put("Quality", quality);
		coinInfo.put("Cleanliness", cleanliness);
		
		// Put coin info into the main tag
		compoundTag.put("CoinInfo", coinInfo);
		
		return compoundTag;
	}
	
	@Override
	public void appendTooltip(ItemStack coin, World world, List<Text> tooltip, TooltipContext tooltipContext)
	{
		NbtCompound coinTags = coin.getOrCreateTag();
		
		// If no coinInfo exists don't add any tooltips
		if (!coinTags.contains("CoinInfo"))
		{
			return;
		}
		
		NbtCompound coinInfo = coinTags.getCompound("CoinInfo");
		
		Formatting formatting = Formatting.GRAY;
		
		// These will be structured like this {Enabled:,Value:}
		addTooltip(tooltip, formatting, coinInfo, "Year");
		addTooltip(tooltip, formatting, coinInfo, "Mint");
		
		// The quality and cleanliness tags are structured like this {Enabled:, {Value:, Value:}}
		NbtCompound qualityInfo = coinInfo.getCompound("Quality");
		NbtCompound cleanlinessInfo = coinInfo.getCompound("Cleanliness");
		
		addQualityTooltip(tooltip, formatting, qualityInfo, QUALITY_TAGS, "quality");
		addQualityTooltip(tooltip, formatting, cleanlinessInfo, CLEAN_TAGS, "cleanliness");
	}
	
	private void addTooltip(List<Text> tooltip, Formatting formatting, NbtCompound parentTag, String childTagName)
	{
		NbtCompound childTag = parentTag.getCompound(childTagName);
		String childTagValue = Objects.requireNonNull(childTag.get("Value")).asString();
		
		if (childTag.getBoolean("Enabled"))
		{
			tooltip.add(new TranslatableText("item.coin_collecting.coin." + childTagName.toLowerCase(), childTagValue).formatted(formatting));
		}
	}
	
	private void addQualityTooltip(List<Text> tooltip, Formatting formatting, NbtCompound qualityInfo, Map<String, Integer> qualityMap, String translationText)
	{
		if (qualityInfo.getBoolean("Enabled"))
		{
			float quality = getQuality(qualityInfo, qualityMap) * 100;
			String qualityString = String.format("%.00f", quality);
			
			tooltip.add(new TranslatableText("item.coin_collecting.coin." + translationText, qualityString).formatted(formatting));
		}
	}
	
	private float getQuality(NbtCompound qualityInfo, Map<String, Integer> qualityMap)
	{
		float maxQuality = 0;
		float quality = 0;
		
		for (String tag : qualityMap.keySet())
		{
			int weight = qualityMap.get(tag);
			float value = qualityInfo.getFloat(tag);
			
			// Start the quality at the max quality
			maxQuality += Math.abs(weight);
			quality += Math.abs(weight);
			
			if (weight <= 0)
			{
				// Subtract any negative qualities from the total quality
				quality -= value * Math.abs(weight);
			}
			else
			{
				// Subtract the lack of positive qualities (1 - value) from the total quality
				quality -= (1 - value) * weight;
			}
		}
		
		return quality / maxQuality;
	}
}
