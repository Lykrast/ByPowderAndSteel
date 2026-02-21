package lykrast.bypowderandsteel.entity;

import java.util.EnumSet;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

public class AbeillonSmallWhiteEntity extends Monster {
	public AbeillonSmallWhiteEntity(EntityType<? extends AbeillonSmallWhiteEntity> type, Level world) {
		super(type, world);
		//like bees but less turn
		moveControl = new FlyingMoveControl(this, 10, true);
		setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1);
		setPathfindingMalus(BlockPathTypes.WATER, -1);
		setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new SpiderAttackGoal(this, 1, true));
		goalSelector.addGoal(2, new WanderGoal(this));
		goalSelector.addGoal(3, new FloatGoal(this));
		goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new SpiderTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new SpiderTargetGoal<>(this, IronGolem.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 12).add(Attributes.ATTACK_DAMAGE, 2).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.FLYING_SPEED, 0.6);
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/abeillon_small_white");
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

	//TODO separate that since multiple mobs use it (skybenders and abeillons)
	private static class WanderGoal extends Goal {
		//based on beewander
		protected AbeillonSmallWhiteEntity skibidi;

		private WanderGoal(AbeillonSmallWhiteEntity skibidi) {
			setFlags(EnumSet.of(Goal.Flag.MOVE));
			this.skibidi = skibidi;
		}

		@Override
		public boolean canUse() {
			return skibidi.navigation.isDone() && skibidi.random.nextInt(10) == 0;
		}

		@Override
		public boolean canContinueToUse() {
			return skibidi.navigation.isInProgress();
		}

		@Override
		public void start() {
			Vec3 vec3 = this.findPos();
			if (vec3 != null) skibidi.navigation.moveTo(skibidi.navigation.createPath(BlockPos.containing(vec3), 1), 1);
		}

		@Nullable
		private Vec3 findPos() {
			Vec3 vec3 = skibidi.getViewVector(0.0F);
			Vec3 destination = HoverRandomPos.getPos(skibidi, 8, 7, vec3.x, vec3.z, Mth.HALF_PI, 3, 1);
			return destination != null ? destination : AirAndWaterRandomPos.getPos(skibidi, 8, 4, -2, vec3.x, vec3.z, Mth.HALF_PI);
		}
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
			return (double) (4.0F + target.getBbWidth());
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
