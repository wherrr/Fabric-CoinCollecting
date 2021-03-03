package io.github.wherrr.coincollecting;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.time.Year;
import java.util.List;

public class CoinItem extends Item
{
	protected final boolean graded;
	protected final int year;
	protected final CoinMint mint;
	protected final float scratches;
	protected final float bigScratches;
	protected final float dirt;
	
	public CoinItem(Settings settings)
	{
		super(settings);
		
		this.graded = true;
		
		// Year between 1.0 release date and current date
		int yearMin = 2011;
		int yearMax = Year.now().getValue();
		this.year = (int) (Math.random() * (yearMax - yearMin) + (yearMin));
		System.out.println(year);
		
		this.mint = CoinMint.getRandom();
		this.scratches = (float) Math.random();
		this.bigScratches = (float) Math.random();
		this.dirt = (float) Math.random();
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext)
	{
		if (graded)
		{
			tooltip.add(new TranslatableText("item.coin_collecting.coin.year", year));
		}
	}
}
