package lykrast.bypowderandsteel.entity.ai;

import java.util.EnumSet;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class CircleTargetGoal extends Goal {
	//Based on the RangedBowAttackGoal (or like what I copied for the GunGoal)
	//but doesn't attack, and then mostly adjusted for the Shrubsnapper cause it keeps interrupting it
	private final Mob mob;
	private final double speedModifier;
	private final double attackRadiusSqr;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public CircleTargetGoal(Mob mob, double speed, double range) {
		this.mob = mob;
		this.speedModifier = speed;
		this.attackRadiusSqr = range * range;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return mob.getTarget() != null;
	}

	@Override
	public boolean canContinueToUse() {
		return canUse() || !mob.getNavigation().isDone();
	}

	@Override
	public void stop() {
		super.stop();
		mob.setAggressive(false);
		seeTime = 0;
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
			if (!(targetDistance > attackRadiusSqr) && seeTime >= 5) {
				mob.getNavigation().stop();
				++strafingTime;
				if (strafingTime == 0) {
					if (mob.getRandom().nextFloat() < 0.3) strafingClockwise = !strafingClockwise;
					if (mob.getRandom().nextFloat() < 0.3) strafingBackwards = !strafingBackwards;
				}
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
				if (targetDistance > attackRadiusSqr * 0.75) strafingBackwards = false;
				else if (targetDistance < attackRadiusSqr * 0.25) strafingBackwards = true;

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

		}
	}

}
