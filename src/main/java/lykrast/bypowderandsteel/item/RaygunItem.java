package lykrast.bypowderandsteel.item;

import java.util.List;

import lykrast.gunswithoutroses.item.GunItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RaygunItem extends GunItem {

	public RaygunItem(Properties properties, int bonusDamage, double damageMultiplier, int fireDelay, double inaccuracy, int enchantability) {
		super(properties, bonusDamage, damageMultiplier, fireDelay, inaccuracy, enchantability);
		projectileSpeed(1);
	}
	
	@Override
	protected void addExtraStatsTooltip(ItemStack stack, Level world, List<Component> tooltip) {
		tooltip.add(Component.translatable("tooltip.bypowderandsteel.raygun").withStyle(ChatFormatting.GRAY));
	}

}
