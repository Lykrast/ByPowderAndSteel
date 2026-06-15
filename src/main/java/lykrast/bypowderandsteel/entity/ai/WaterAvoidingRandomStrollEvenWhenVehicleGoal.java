package lykrast.bypowderandsteel.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.phys.Vec3;

public class WaterAvoidingRandomStrollEvenWhenVehicleGoal extends WaterAvoidingRandomStrollGoal {

	//just removing the vehicle checks so they can wander around
	public WaterAvoidingRandomStrollEvenWhenVehicleGoal(PathfinderMob mob, double speed) {
		super(mob, speed);
	}

	@Override
	public boolean canUse() {
		if (!this.forceTrigger) {
			//this.checkNoActionTime is true for the constructors we use (and private otherwise)
			if (this.mob.getNoActionTime() >= 100) { return false; }

			if (this.mob.getRandom().nextInt(reducedTickDelay(this.interval)) != 0) { return false; }
		}

		Vec3 vec3 = this.getPosition();
		if (vec3 == null) {
			return false;
		}
		else {
			this.wantedX = vec3.x;
			this.wantedY = vec3.y;
			this.wantedZ = vec3.z;
			this.forceTrigger = false;
			return true;
		}
	}

	@Override
	public boolean canContinueToUse() {
		return !this.mob.getNavigation().isDone();
	}

}
