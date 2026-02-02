package lykrast.bypowderandsteel.item;

import java.util.List;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.registry.BPASEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class MarksbladeItem extends SwordItem {
	//level 0 is 4 damage, then every added level is +1, should be good enough for my uses
	protected int markedLevel;
	public static final float MARKED_BASE_DMG = 4;

	public MarksbladeItem(Tier tier, int damage, float speed, int markedLevel, Properties props) {
		super(tier, damage, speed, props);
		this.markedLevel = markedLevel;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		//same check as crits
		if (!(attacker instanceof Player player) || player.getAttackStrengthScale(0.5f) >= 0.9) {
			target.addEffect(new MobEffectInstance(BPASEffects.marked.get(), 5 * 20, markedLevel));
			//TODO a hook for subclasses to directly put effects here for convenience
		}
		return super.hurtEnemy(stack, target, attacker);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(Component.translatable("tooltip.bypowderandsteel.marksblade.1").withStyle(ChatFormatting.DARK_GREEN));
		tooltip.add(Component.translatable("tooltip.bypowderandsteel.marksblade.2", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(MARKED_BASE_DMG+markedLevel)).withStyle(ChatFormatting.DARK_GREEN));
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		//DEFAULT_SWORD_ACTIONS is SWORD_DIG and SWORD_SWEEP, don't want the sweep without the enchantment
		return toolAction == ToolActions.SWORD_DIG || (toolAction == ToolActions.SWORD_SWEEP && stack.getEnchantmentLevel(Enchantments.SWEEPING_EDGE) > 0);
	}

}
