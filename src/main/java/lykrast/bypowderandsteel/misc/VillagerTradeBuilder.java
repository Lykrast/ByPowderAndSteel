package lykrast.bypowderandsteel.misc;

import java.util.function.Function;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.registries.RegistryObject;

public class VillagerTradeBuilder {
	//Inspired by PNC:R's RandomTradeBuilder
	//https://github.com/TeamPneumatic/pnc-repressurized/blob/1.20.1/src/main/java/me/desht/pneumaticcraft/common/util/RandomTradeBuilder.java

	private Function<RandomSource, ItemStack> priceA, priceB, sale;
	private int maxTrades, xp;
	private float priceMult;
	private static final Function<RandomSource, ItemStack> NONE = (random) -> ItemStack.EMPTY;
	
	public VillagerTradeBuilder(int maxTrades, int xp, double priceMult) {
		this.maxTrades = maxTrades;
		this.xp = xp;
		this.priceMult = (float) priceMult; //I don't like writing f
		priceB = NONE;
	}
	
	public VillagerTradeBuilder buyManyForOne(Item buy, int min, int max) {
		priceA = itemRandomAmount(buy, min, max);
		sale = itemRandomAmount(Items.EMERALD, 1, 1);
		return this;
	}
	
	public VillagerTradeBuilder buyOneForMany(Item buy, int min, int max) {
		priceA = itemRandomAmount(buy, 1, 1);
		sale = itemRandomAmount(Items.EMERALD, min, max);
		return this;
	}

	public VillagerTradeBuilder sell(Item sell, int min, int max) {
		sale = itemRandomAmount(sell, min, max);
		return this;
	}

	public VillagerTradeBuilder emeraldPrice(int min, int max) {
		priceA = itemRandomAmount(Items.EMERALD, min, max);
		return this;
	}
	
	//So that I don't have to write .get()
	public VillagerTradeBuilder buyManyForOne(RegistryObject<? extends Item> buy, int min, int max) {
		buyManyForOne(buy.get(), min, max);
		return this;
	}
	public VillagerTradeBuilder buyOneForMany(RegistryObject<? extends Item> buy, int min, int max) {
		buyOneForMany(buy.get(), min, max);
		return this;
	}
	public VillagerTradeBuilder sell(RegistryObject<? extends Item> sell, int min, int max) {
		sell(sell.get(), min, max);
		return this;
	}
	
	public static Function<RandomSource, ItemStack> itemRandomAmount(Item item, int min, int max) {
		return (random) -> new ItemStack(item, min + random.nextInt(max - min + 1));
	}
	
	public VillagerTrades.ItemListing build() {
		return (entity, random) -> new MerchantOffer(priceA.apply(random), priceB.apply(random), sale.apply(random), maxTrades, xp, priceMult);
	}
}
