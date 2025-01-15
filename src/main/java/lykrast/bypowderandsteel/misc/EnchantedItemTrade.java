package lykrast.bypowderandsteel.misc;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.registries.RegistryObject;

public class EnchantedItemTrade implements ItemListing {
	//Based on the vanilla class
	//Separate from the normal builder because the random level affects both price and sold item
	private Item item;
	private int baseEmeraldCost;
	private int maxUses;
	private int villagerXp;
	private float priceMultiplier;
	private int minLevel, maxLevel;

	public EnchantedItemTrade(RegistryObject<? extends Item> item, int baseCost, int minLevel, int maxLevel, int maxUses, int xp, double priceMultiplier) {
		this(item.get(), baseCost, minLevel, maxLevel, maxUses, xp, priceMultiplier);
	}

	public EnchantedItemTrade(Item item, int baseCost, int minLevel, int maxLevel, int maxUses, int xp, double priceMultiplier) {
		this.item = item;
		this.baseEmeraldCost = baseCost;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.maxUses = maxUses;
		this.villagerXp = xp;
		this.priceMultiplier = (float) priceMultiplier;
	}

	@Override
	public MerchantOffer getOffer(Entity entity, RandomSource random) {
		int level = minLevel + random.nextInt(maxLevel - minLevel + 1);
		ItemStack sell = EnchantmentHelper.enchantItem(random, new ItemStack(item), level, false);
		int price = Math.min(this.baseEmeraldCost + level, 64);
		ItemStack emeralds = new ItemStack(Items.EMERALD, price);
		return new MerchantOffer(emeralds, sell, maxUses, villagerXp, priceMultiplier);
	}

}
