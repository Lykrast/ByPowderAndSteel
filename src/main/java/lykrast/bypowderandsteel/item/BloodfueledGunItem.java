package lykrast.bypowderandsteel.item;

import java.util.List;

import lykrast.bypowderandsteel.registry.BPASDamage;
import lykrast.gunswithoutroses.item.GunItem;
import lykrast.gunswithoutroses.item.IBullet;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BloodfueledGunItem extends GunItem {
	protected int selfDamage;

	public BloodfueledGunItem(Properties properties, int bonusDamage, double damageMultiplier, int fireDelay, double inaccuracy, int enchantability, int selfDamage) {
		super(properties, bonusDamage, damageMultiplier, fireDelay, inaccuracy, enchantability);
		this.selfDamage = selfDamage;
	}
	
	@Override
	protected void shoot(Level world, Player player, ItemStack gun, ItemStack ammo, IBullet bulletItem, boolean bulletFree) {
		selfDamage(player);
		super.shoot(world, player, gun, ammo, bulletItem, bulletFree);
	}
	
	@Override
	public void shootAt(LivingEntity shooter, double x, double y, double z, ItemStack gun, ItemStack ammo, IBullet bulletItem, double mobSpread, boolean bulletFree) {
		selfDamage(shooter);
		super.shootAt(shooter, x, y, z, gun, ammo, bulletItem, mobSpread, bulletFree);
	}
	
	protected void selfDamage(LivingEntity shooter) {
		int lastInvulnerable = shooter.invulnerableTime;
		shooter.invulnerableTime = 0;
		shooter.hurt(BPASDamage.bloodfueledDamage(shooter.level().registryAccess()), selfDamage);
		shooter.invulnerableTime = lastInvulnerable;
	}
	
	@Override
	protected void addExtraStatsTooltip(ItemStack stack, Level world, List<Component> tooltip) {
		tooltip.add(Component.translatable("tooltip.bypowderandsteel.bloodfueled", selfDamage).withStyle(ChatFormatting.GRAY));
	}

}
