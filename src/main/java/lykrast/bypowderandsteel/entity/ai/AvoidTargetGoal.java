package lykrast.bypowderandsteel.entity.ai;

import java.util.EnumSet;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

/**
 * Runs away when attack target is too close.
 */
public class AvoidTargetGoal extends Goal {
	//based on AvoidEntityGoal
	protected final PathfinderMob mob;
	protected final PathNavigation pathNav;
	protected Path path;
	private final double speed, detectRangeSqr;
	private final int runTo;

	public AvoidTargetGoal(PathfinderMob mob, double runRange, int runTo, double speed) {
		this.mob = mob;
		this.detectRangeSqr = runRange*runRange;
		this.runTo = runTo;
		this.speed = speed;
		pathNav = mob.getNavigation();
		setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (mob.getTarget() == null || mob.getTarget().distanceToSqr(mob) > detectRangeSqr) return false;
		//I think the 16/7 are xz distance and y range, will leave like that for now
		Vec3 runPos = DefaultRandomPos.getPosAway(mob, runTo, 7, mob.getTarget().position());
		if (runPos == null) return false;
		path = pathNav.createPath(runPos.x, runPos.y, runPos.z, 0);
		return path != null;
	}

	@Override
	public boolean canContinueToUse() {
		return !pathNav.isDone();
	}

	@Override
	public void start() {
		pathNav.moveTo(path, speed);
	}
	//didn't copy the tick that increases speed when near
}
