package lykrast.bypowderandsteel.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class GraviticSwordItem extends SwordItem {

	public GraviticSwordItem(Tier tier, int damage, float speed, Properties props) {
		super(tier, damage, speed, props);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		double mult = Math.max(0, 1 - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
		target.push(0, 0.5 * mult, 0);
		target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 0));
		return super.hurtEnemy(stack, target, attacker);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(Component.translatable("tooltip.bypowderandsteel.gravitic_bullet").withStyle(ChatFormatting.GRAY));
	}

}
