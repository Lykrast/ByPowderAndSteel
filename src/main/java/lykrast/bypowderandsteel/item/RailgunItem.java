package lykrast.bypowderandsteel.item;

import lykrast.bypowderandsteel.registry.BPASSounds;
import lykrast.gunswithoutroses.item.ChargeGunItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RailgunItem extends ChargeGunItem {

	public RailgunItem(Properties properties, int bonusDamage, double damageMultiplier, int fireDelay, double inaccuracy, int enchantability, int chargeTime) {
		super(properties, bonusDamage, damageMultiplier, fireDelay, inaccuracy, enchantability, chargeTime);
	}

	@Override
	public void onUseTick(Level world, LivingEntity user, ItemStack gun, int ticks) {
		super.onUseTick(world, user, gun, ticks);
		int dur = getUseDuration(gun);
		int used = dur - ticks;
		if (used % 5 == 0) {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), BPASSounds.railgunCharge.get(), user.getSoundSource(), 1, 0.5f + (used / (float) dur) * 1f);
		}
	}

}
