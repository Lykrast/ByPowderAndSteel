package lykrast.bypowderandsteel.entity.ai;

import java.util.EnumSet;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.phys.Vec3;

public class HoverWanderGoal extends Goal {
	//based on beewander
	protected PathfinderMob mob;

	public HoverWanderGoal(PathfinderMob skibidi) {
		setFlags(EnumSet.of(Goal.Flag.MOVE));
		this.mob = skibidi;
	}

	@Override
	public boolean canUse() {
		return mob.getNavigation().isDone() && mob.getRandom().nextInt(10) == 0;
	}

	@Override
	public boolean canContinueToUse() {
		return mob.getNavigation().isInProgress();
	}

	@Override
	public void start() {
		Vec3 vec3 = this.findPos();
		if (vec3 != null) mob.getNavigation().moveTo(mob.getNavigation().createPath(BlockPos.containing(vec3), 1), 1);
	}

	@Nullable
	private Vec3 findPos() {
		Vec3 vec3 = mob.getViewVector(0.0F);
		Vec3 destination = HoverRandomPos.getPos(mob, 8, 7, vec3.x, vec3.z, Mth.HALF_PI, 3, 1);
		return destination != null ? destination : AirAndWaterRandomPos.getPos(mob, 8, 4, -2, vec3.x, vec3.z, Mth.HALF_PI);
	}
}