package lykrast.bypowderandsteel.entity;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ai.HoverWanderGoal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AbeillonGPEmperorEntity extends AbeillonAbstractEntity {
	public AbeillonGPEmperorEntity(EntityType<? extends AbeillonGPEmperorEntity> type, Level world) {
		super(type, world);
		xpReward = 20;
	}

	@Override
	protected void registerGoals() {
		//stays aggressive during the day
		goalSelector.addGoal(1, new ChargeAt(this, 1, true));
		goalSelector.addGoal(2, new HoverWanderGoal(this));
		goalSelector.addGoal(3, new FloatGoal(this));
		goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		//I want them to be faster than player walking backwards but can't get them to go too fast :(
		return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 32).add(Attributes.MAX_HEALTH, 100).add(Attributes.ATTACK_DAMAGE, 8).add(Attributes.MOVEMENT_SPEED, 0.35)
				.add(Attributes.FLYING_SPEED, 0.8).add(Attributes.KNOCKBACK_RESISTANCE, 1);
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/abeillon_great_purple_emperor");
	}

	private static class ChargeAt extends MeleeAttackGoal {
		private AbeillonGPEmperorEntity bee;
		//0 approach, 1 windup, 2 leap, 3 recover
		private int phase;
		//ticksUntilNextAttack is private so I'm making my own countdown
		private int time;
		//lag behind the target since it's a bigger leap than zombunnies
		private double lockX, lockY, lockZ;

		public ChargeAt(AbeillonGPEmperorEntity buny, double speed, boolean seeThroughWalls) {
			super(buny, speed, seeThroughWalls);
			this.bee = buny;
		}
		
		@Override
		protected double getAttackReachSqr(LivingEntity target) {
			return 4 + target.getBbWidth();
		}

		@Override
		public void start() {
			super.start();
			phase = 0;
			time = 0;
		}

		@Override
		public void tick() {
			if (time > 0) time--;
			super.tick();
		}

		@Override
		protected void checkAndPerformAttack(LivingEntity target, double distanceSqr) {
			//this is only called when target is alive, so we do the transition animations above
			switch (phase) {
				case 0:
					//running at target
					if (time <= 0 && distanceSqr < 36 && bee.getSensing().hasLineOfSight(target)) {
						//ready for the jump
						phase = 1;
						time = 10;
						bee.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ(), 50, 0);
						lockX = target.getX();
						lockY = target.getEyeY();
						lockZ = target.getZ();
						//TODO windup sound
					}
					break;
				case 1:
					//bracing for jump
					if (distanceSqr > 64 || !bee.getSensing().hasLineOfSight(target)) {
						//target too far, disengage jump
						phase = 0;
					}
					else if (time <= 0) {
						//leap!
						time = 5;
						phase = 2;	
						Vec3 vec31 = new Vec3(lockX - bee.getX(), lockY - bee.getY(), lockZ - bee.getZ());
						if (vec31.lengthSqr() > 1.0E-7D) {
							vec31 = vec31.normalize().scale(1.4);
						}

						bee.setDeltaMovement(vec31.x, vec31.y, vec31.z);
						//TODO sound
					}
					else {
						bee.getNavigation().stop();
						//buny.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
					}
					break;
				case 2:
					//in the air
					if (time <= 0) {
						//end of charge
						phase = 3;
						time = 8;
						bee.getNavigation().stop();
						//deccelerate
						bee.setDeltaMovement(bee.getDeltaMovement().scale(0.3));
					}
					else {
						super.checkAndPerformAttack(target, distanceSqr);
					}
					break;
				case 3:
					//jump recovery
					if (time <= 0) {
						phase = 0;
						time = 2;
						//2 ticks of forced movement to realign
					}
					else bee.getNavigation().stop();
					break;
			}
		}

		@Override
		public boolean canContinueToUse() {
			if (phase > 0 && time > 0) return true;
			return super.canContinueToUse();
		}
	}
}
