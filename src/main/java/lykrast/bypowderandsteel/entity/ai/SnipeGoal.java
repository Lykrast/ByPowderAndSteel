package lykrast.bypowderandsteel.entity.ai;

import java.util.EnumSet;

import lykrast.bypowderandsteel.entity.SniperMob;
import lykrast.bypowderandsteel.registry.BPASSounds;
import lykrast.gunswithoutroses.item.GunItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;

public class SnipeGoal<T extends Mob & SniperMob> extends Goal {
	//This was originally just for Zombie Seals but extending it for pirates
	//Based on the guardian one a bit
	protected T sniper;
	protected LivingEntity target;
	private int seeTime;
	private boolean sealSounds;

	public SnipeGoal(T sniper, boolean sealSounds) {
		this.sniper = sniper;
		this.sealSounds = sealSounds;
		setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	protected boolean isHoldingGun() {
		return sniper.isHolding(is -> is.getItem() instanceof GunItem);
	}

	@Override
	public boolean canUse() {
		return sniper.getAttackCooldown() <= 0 && sniper.getTarget() != null && isHoldingGun() && sniper.hasLineOfSight(sniper.getTarget());
	}

	@Override
	public boolean canContinueToUse() {
		return sniper.getAttackCooldown() <= 0 && target != null && target.isAlive() && seeTime > -20 && isHoldingGun();
	}

	@Override
	public void start() {
		sniper.getNavigation().stop();
		seeTime = 0;
		target = sniper.getTarget();
		if (target != null) {
			sniper.getLookControl().setLookAt(target, 90, 90);
			sniper.setActiveAttackTarget(target.getId());
			sniper.setAggressive(true);
			//volume 4 should be heard 64 blocks away (headshot sound is 5)
			if (sealSounds) sniper.playSound(BPASSounds.sealSpot.get(), 4, 1);
		}
	}

	@Override
	public void stop() {
		sniper.setActiveAttackTarget(0);
		sniper.setAggressive(false);
		if (sniper.getAttackCooldown() <= 0) sniper.setAttackCooldown(5);
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
	
	//hooks for the sunken pirate so that I don't have to tear down the tick()
	protected void onFire() {}
	protected void onDisengage() {}

	@Override
	public void tick() {
		if (target != null) {
			sniper.getNavigation().stop();
			sniper.getLookControl().setLookAt(target, 90, 90);
			sniper.setActiveAttackTarget(target.getId());
			if (!sniper.hasLineOfSight(target)) {
				seeTime -= 2;
				//volume 4 should be heard 64 blocks away (headshot sound is 5)
				if (seeTime <= -20 && sealSounds) {
					sniper.playSound(BPASSounds.sealUnspot.get(), 4, 1);
					onDisengage();
				}
			}
			else {
				if (seeTime < 0) seeTime = 0;
				seeTime++;
				if (seeTime >= 50) {
					ItemStack gun = sniper.getMainHandItem();
					if (!(gun.getItem() instanceof GunItem)) gun = sniper.getOffhandItem();
					GunItem gunItem = (GunItem) gun.getItem();
					ItemStack bullet = sniper.getBulletStack();
					sniper.setAttackCooldown(gunItem.getFireDelay(gun, sniper));
					gunItem.shootAt(sniper, target, gun, bullet, sniper.getBullet(), sniper.getAddedSpread(), true);
					//volume 4 should be heard 64 blocks away (headshot sound is 5)
					sniper.playSound(gunItem.getFireSound(), sealSounds ? 4 : 1, 1.0F / (sniper.getRandom().nextFloat() * 0.4F + 0.8F));
					onFire();
				}
			}
		}
	}
}
