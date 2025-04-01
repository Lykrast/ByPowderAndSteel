package lykrast.bypowderandsteel.entity.ai;

import java.util.EnumSet;

import lykrast.bypowderandsteel.entity.GunMob;
import lykrast.gunswithoutroses.item.GunItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;

public class GunGoal<T extends Mob & GunMob> extends Goal {
	//Based on the RangedBowAttackGoal
	private final T mob;
	private final double speedModifier;
	private int attackIntervalMin;
	private final double strafeRadiusSqr, attackRadiusSqr;
	private int attackTime = -1;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public GunGoal(T mob, double speed, int minAttackInterval, double strafeRange, double attackRange) {
		//-1 strafe to disable strafing, -1 range to disable the max range
		this.mob = mob;
		this.speedModifier = speed;
		this.attackIntervalMin = minAttackInterval;
		this.strafeRadiusSqr = strafeRange > 0 ? strafeRange * strafeRange : -1;
		this.attackRadiusSqr = attackRange > 0 ? attackRange * attackRange : -1;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	public GunGoal(T mob, double speed, int minAttackInterval, double strafeRange) {
		this(mob, speed, minAttackInterval, strafeRange, -1);
	}

	@Override
	public boolean canUse() {
		return mob.getTarget() == null ? false : isHoldingGun();
	}

	protected boolean isHoldingGun() {
		return mob.isHolding(is -> is.getItem() instanceof GunItem);
	}

	@Override
	public boolean canContinueToUse() {
		return (canUse() || !mob.getNavigation().isDone()) && isHoldingGun();
	}

	@Override
	public void start() {
		super.start();
		mob.setAggressive(true);
	}

	@Override
	public void stop() {
		super.stop();
		mob.setAggressive(false);
		seeTime = 0;
		attackTime = -1;
		//mob.stopUsingItem();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		LivingEntity target = mob.getTarget();
		if (target != null) {
			double targetDistance = mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
			boolean los = mob.getSensing().hasLineOfSight(target);
			boolean hasSeen = seeTime > 0;
			
			//adjust seeing time
			if (los != hasSeen) seeTime = 0;
			if (los) ++seeTime;
			else --seeTime;

			//strafing
			if (strafeRadiusSqr > 0 && targetDistance <= strafeRadiusSqr && seeTime >= 20) {
				mob.getNavigation().stop();
				++strafingTime;
			}
			else {
				mob.getNavigation().moveTo(target, speedModifier);
				strafingTime = -1;
			}

			//mixing strafing
			if (strafingTime >= 20) {
				if (mob.getRandom().nextFloat() < 0.3) strafingClockwise = !strafingClockwise;
				if (mob.getRandom().nextFloat() < 0.3) strafingBackwards = !strafingBackwards;
				strafingTime = 0;
			}

			if (strafingTime > -1) {
				if (targetDistance > strafeRadiusSqr * 0.75) strafingBackwards = false;
				else if (targetDistance < strafeRadiusSqr * 0.25) strafingBackwards = true;

				mob.getMoveControl().strafe(strafingBackwards ? -0.5F : 0.5F, strafingClockwise ? 0.5F : -0.5F);
				Entity vehicle = mob.getControlledVehicle();
				if (vehicle instanceof Mob) {
					Mob mob = (Mob) vehicle;
					mob.lookAt(target, 30, 30);
				}

				mob.lookAt(target, 30, 30);
			}
			else {
				mob.getLookControl().setLookAt(target, 30, 30);
			}
			
			//shoot
			if (--attackTime <= 0 && seeTime >= 20 && los && (attackRadiusSqr < 0 || targetDistance <= attackRadiusSqr)) {
				ItemStack gun =  mob.getMainHandItem();
				GunItem gunItem = (GunItem) gun.getItem();
				ItemStack bullet = mob.getBulletStack();
				attackTime = Math.max(gunItem.getFireDelay(gun, mob), attackIntervalMin);
				gunItem.shootAt(mob, target, gun, bullet, mob.getBullet(), mob.getAddedSpread(), true);
				mob.playSound(gunItem.getFireSound(), 1, 1.0F / (mob.getRandom().nextFloat() * 0.4F + 0.8F));
			}

//			if (mob.isUsingItem()) {
//				if (!los && seeTime < -60) {
//					mob.stopUsingItem();
//				}
//				else if (los) {
//					int i = mob.getTicksUsingItem();
//					if (i >= 20) {
//						mob.stopUsingItem();
//						mob.performRangedAttack(target, BowItem.getPowerForTime(i));
//						attackTime = this.attackIntervalMin;
//					}
//				}
//			}
//			else if (--attackTime <= 0 && seeTime >= -60) {
//				mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(mob, item -> item instanceof BowItem));
//			}

		}
	}

}
