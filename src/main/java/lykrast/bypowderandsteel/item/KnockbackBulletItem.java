package lykrast.bypowderandsteel.item;

import java.util.List;

import javax.annotation.Nullable;

import lykrast.gunswithoutroses.entity.BulletEntity;
import lykrast.gunswithoutroses.item.BulletItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class KnockbackBulletItem extends BulletItem {
	private double knockback;

	public KnockbackBulletItem(Properties properties, int damage, double knockback) {
		super(properties, damage);
		this.knockback = knockback;
	}
	
	@Override
	public BulletEntity createProjectile(Level world, ItemStack stack, LivingEntity shooter) {
		BulletEntity bullet = super.createProjectile(world, stack, shooter);
		bullet.setKnockbackStrength(knockback);
		return bullet;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(Component.translatable("tooltip.bypowderandsteel.bullet.knockback", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(knockback)).withStyle(ChatFormatting.DARK_GREEN));
	}
}
