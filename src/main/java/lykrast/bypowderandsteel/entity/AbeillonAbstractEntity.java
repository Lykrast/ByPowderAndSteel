package lykrast.bypowderandsteel.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.fluids.FluidType;

public abstract class AbeillonAbstractEntity extends Monster {
	public AbeillonAbstractEntity(EntityType<? extends AbeillonAbstractEntity> type, Level world) {
		super(type, world);
		//like bees but less turn
		moveControl = new FlyingMoveControl(this, 10, true);
		setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1);
		setPathfindingMalus(BlockPathTypes.WATER, -1);
		setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16);
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
		//model is 11px high, eye at 7px
		return dimensions.height * 0.636F;
	}

	@Override
	protected PathNavigation createNavigation(Level level) {
		//Bee logic
		FlyingPathNavigation nav = new FlyingPathNavigation(this, level) {
			@Override
			public boolean isStableDestination(BlockPos p_27947_) {
				return !this.level.getBlockState(p_27947_.below()).isAir();
			}
		};
		nav.setCanOpenDoors(false);
		nav.setCanFloat(false);
		nav.setCanPassDoors(true);
		return nav;
	}

	@Override
	public void jumpInFluid(FluidType type) {
		setDeltaMovement(getDeltaMovement().add(0, 0.01, 0));
	}

	@Override
	public float getWalkTargetValue(BlockPos pos, LevelReader level) {
		return level.getBlockState(pos).isAir() ? 10 : 0;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
	}

	@Override
	public MobType getMobType() {
		return MobType.ARTHROPOD;
	}

	//TODO sounds
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.SPIDER_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.BEE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.BEE_DEATH;
	}

	//spider goals to be neutral in sunlight
	protected static class SpiderAttackGoal extends MeleeAttackGoal {
		public SpiderAttackGoal(PathfinderMob mob, double speed, boolean followingTargetEvenIfNotSeen) {
			super(mob, speed, followingTargetEvenIfNotSeen);
		}

		@Override
		public boolean canContinueToUse() {
			@SuppressWarnings("deprecation")
			float f = this.mob.getLightLevelDependentMagicValue();
			if (f >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
				this.mob.setTarget((LivingEntity) null);
				return false;
			}
			else {
				return super.canContinueToUse();
			}
		}

		@Override
		protected double getAttackReachSqr(LivingEntity target) {
			return 4 + target.getBbWidth();
		}
	}

	protected static class SpiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
		public SpiderTargetGoal(PathfinderMob mob, Class<T> target, boolean mustSee) {
			super(mob, target, mustSee);
		}

		@Override
		public boolean canUse() {
			@SuppressWarnings("deprecation")
			float f = this.mob.getLightLevelDependentMagicValue();
			return f >= 0.5F ? false : super.canUse();
		}
	}
}
