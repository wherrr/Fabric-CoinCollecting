package io.github.wherrr.coincollecting;

import com.google.common.base.Function;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.time.Year;
import java.util.List;

public class CoinItem extends Item
{
	public CoinItem(Settings settings)
	{
		super(settings);
	}
	
	public static CompoundTag GenerateTags()
	{
		// Main tags
		CompoundTag compoundTag = new CompoundTag();
		CompoundTag coinInfo = new CompoundTag();
		
		// Info tags
		CompoundTag year = new CompoundTag();
		CompoundTag mint = new CompoundTag();
		CompoundTag scratches = new CompoundTag();
		CompoundTag bigScratches = new CompoundTag();
		CompoundTag dirt = new CompoundTag();
		
		// Set all values to disabled by default
		year.putBoolean("Enabled", true);
		mint.putBoolean("Enabled", true);
		scratches.putBoolean("Enabled", true);
		bigScratches.putBoolean("Enabled", true);
		dirt.putBoolean("Enabled", true);
		
		// Randomize info
		// Year between 1.0 release date and current date
		int yearMin = 2011;
		int yearMax = Year.now().getValue();
		year.putInt("Value", (int) (Math.random() * (yearMax - yearMin) + (yearMin)));
		
		mint.putString("Value", CoinMint.getRandom().toString());
		scratches.putFloat("Value", (float) Math.random());
		bigScratches.putFloat("Value", (float) Math.random());
		dirt.putFloat("Value", (float) Math.random());
		
		// Put info into the coin info
		coinInfo.put("Year", year);
		coinInfo.put("Mint", mint);
		coinInfo.put("Scratches", scratches);
		coinInfo.put("BigScratches", bigScratches);
		coinInfo.put("Dirt", dirt);
		
		// Put coin info into the main tag
		compoundTag.put("CoinInfo", coinInfo);
		
		return compoundTag;
	}
	
	@Override
	public void appendTooltip(ItemStack coin, World world, List<Text> tooltip, TooltipContext tooltipContext)
	{
		CompoundTag coinTags = coin.getOrCreateTag();
		
		// If no coinInfo exists don't add any tooltips
		if (!coinTags.contains("CoinInfo"))
		{
			return;
		}
		
		CompoundTag coinInfo = coinTags.getCompound("CoinInfo");
		
		Formatting formatting = Formatting.GRAY;
		
		// These will be structured like this {Enabled:,Value:}
		addTooltip(tooltip, formatting, coinInfo, "Year");
		addTooltip(tooltip, formatting, coinInfo, "Mint");
		addTooltip(tooltip, formatting, coinInfo, "Scratches");
		addTooltip(tooltip, formatting, coinInfo, "BigScratches");
		addTooltip(tooltip, formatting, coinInfo, "Dirt");
		
		/*
		CompoundTag scratches = coinInfo.getCompound("Scratches");
		CompoundTag bigScratches = coinInfo.getCompound("BigScratches");
		
		if (scratches.getBoolean("Enabled") && bigScratches.getBoolean("Enabled"))
		{
			// Big scratches are 3 times as worse as regular scratches when looking at quality
			float quality = 1 - (((bigScratches.getFloat("Value") * 3) + scratches.getFloat("Value")) / 4);
		}
		*/
	}
	
	private void addTooltip(List<Text> tooltip, Formatting formatting, CompoundTag parentTag, String childTagName)
	{
		CompoundTag childTag = parentTag.getCompound(childTagName);
		
		if (childTag.getBoolean("Enabled"))
		{
			tooltip.add(new TranslatableText("item.coin_collecting.coin." + childTagName.toLowerCase(), childTag.getString("Value")).formatted(formatting));
		}
	}
	
	private float getQuality(CompoundTag[] compoundTags)
	{
		return 0f;
	}
}
