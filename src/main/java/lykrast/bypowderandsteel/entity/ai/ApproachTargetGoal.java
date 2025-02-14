package lykrast.bypowderandsteel.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class ApproachTargetGoal extends MeleeAttackGoal {
	private boolean shouldAggressive;

	public ApproachTargetGoal(PathfinderMob mob, double speed, boolean seeThroughWalls) {
		this(mob, speed, seeThroughWalls, true);
	}

	public ApproachTargetGoal(PathfinderMob mob, double speed, boolean seeThroughWalls, boolean shouldAggressive) {
		super(mob, speed, seeThroughWalls);
		this.shouldAggressive = shouldAggressive;
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity target, double distance) {
	}
	
	//a bit hacky but should probably work

	@Override
	public void start() {
		boolean oldAggressive = mob.isAggressive();
		super.start();
		if (!shouldAggressive) mob.setAggressive(oldAggressive);
	}

	@Override
	public void stop() {
		boolean oldAggressive = mob.isAggressive();
		super.stop();
		if (!shouldAggressive) mob.setAggressive(oldAggressive);
	}

}
