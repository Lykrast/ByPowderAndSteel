package lykrast.bypowderandsteel.item;

import java.util.List;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.entity.FlamethrowerBulletEntity;
import lykrast.gunswithoutroses.entity.BulletEntity;
import lykrast.gunswithoutroses.item.BulletItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class FlamethrowerBulletItem extends BulletItem {

	public FlamethrowerBulletItem(Properties properties, int damage) {
		super(properties, damage);
	}

	@Override
	public BulletEntity createProjectile(Level world, ItemStack stack, LivingEntity shooter) {
		FlamethrowerBulletEntity entity = new FlamethrowerBulletEntity(world, shooter);
		entity.setItem(stack);
		entity.setDamage(damage);
		return entity;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(Component.translatable("tooltip.bypowderandsteel.bullet.flamethrower.1").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("tooltip.bypowderandsteel.bullet.flamethrower.2").withStyle(ChatFormatting.GRAY));
	}

}
