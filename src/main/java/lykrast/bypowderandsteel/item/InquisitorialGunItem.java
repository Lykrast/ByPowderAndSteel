package lykrast.bypowderandsteel.item;

import java.util.List;

import javax.annotation.Nullable;

import lykrast.gunswithoutroses.item.GunItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class InquisitorialGunItem extends GunItem {
	//TODO figure out what I want to do with it gameplay-wise

	public InquisitorialGunItem(Properties properties, int bonusDamage, double damageMultiplier, int fireDelay, double inaccuracy, int enchantability) {
		super(properties, bonusDamage, damageMultiplier, fireDelay, inaccuracy, enchantability);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		//Plain text and not localized because it's not meant to be readable (at least not without decoding the cipher)
		//though the translation is right there I guess so you just got spoiled
		//and there's also many other ways to get spoiled (creative search, looking at the font file, finding font in commands...)
		//I just hope at least one person has fun deciphering it the intended way
		tooltip.add(Component.literal("By my gun me carry # Powder").withStyle(ChatFormatting.GRAY).withStyle(FlavoredItem.STYLE));
	}

}
