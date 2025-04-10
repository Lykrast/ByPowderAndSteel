package lykrast.bypowderandsteel.entity;

import java.util.EnumSet;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ai.GunGoal;
import lykrast.bypowderandsteel.misc.BPASUtils;
import lykrast.bypowderandsteel.registry.BPASItems;
import lykrast.gunswithoutroses.item.BulletItem;
import lykrast.gunswithoutroses.registry.GWRAttributes;
import lykrast.gunswithoutroses.registry.GWRItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class SunkenPirateEntity extends Monster implements GunMob {
	//drowned logic
	protected boolean searchingForLand;
	protected final WaterBoundPathNavigation waterNavigation;
	protected final GroundPathNavigation groundNavigation;

	public SunkenPirateEntity(EntityType<? extends SunkenPirateEntity> type, Level world) {
		super(type, world);
		setMaxUpStep(1.0F);
		moveControl = new DrownedMoveControl(this);
		setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
		waterNavigation = new WaterBoundPathNavigation(this, world);
		groundNavigation = new GroundPathNavigation(this, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new DrownedGoToWaterGoal(this, 1));
		goalSelector.addGoal(2, new PirateGunGoal(this, 0.8, 4, -1, 16, 5));
		goalSelector.addGoal(3, new DrownedAttackGoal(this, 1.2, false));
		goalSelector.addGoal(5, new DrownedGoToBeachGoal(this, 1));
		goalSelector.addGoal(6, new DrownedSwimUpGoal(this, 1, level().getSeaLevel()));
		goalSelector.addGoal(7, new RandomStrollGoal(this, 1));
		goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	@SuppressWarnings("unchecked")
	@Override
	public BulletItem getBullet() {
		return GWRItems.prismarineBullet.get();
	}

	@Override
	public double getAddedSpread() {
		//same as skeletons, 10/6/2 for easy/normal/hard
		return 14 - level().getDifficulty().getId() * 4;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return BPASUtils.baseGunMobAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.ARMOR, 2).add(Attributes.ATTACK_DAMAGE, 4).add(Attributes.MOVEMENT_SPEED, 0.23)
				.add(GWRAttributes.dmgTotal.get(), 0.5).add(GWRAttributes.fireDelay.get(), 2);
	}

	//Similar spawn rule as drowned
	@SuppressWarnings("deprecation")
	public static boolean spawnRules(EntityType<? extends Monster> entity, ServerLevelAccessor world, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
		if (!world.getFluidState(pos.below()).is(FluidTags.WATER)) return false;
		else {
			return pos.getY() < world.getSeaLevel() - 3 && world.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(world, pos, random)
					&& (spawnType == MobSpawnType.SPAWNER || world.getFluidState(pos).is(FluidTags.WATER));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag nbt) {
		//super is fine for an override
		groupData = super.finalizeSpawn(world, difficulty, spawnType, groupData, nbt);
		RandomSource random = world.getRandom();
		populateDefaultEquipmentSlots(random, difficulty);
		populateDefaultEquipmentEnchantments(random, difficulty);

		return groupData;
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
		//no super to not have armor
		Item gun = BPASItems.buccaneerFlintlock.get();
		//1/3 of the time have the flintlock with different stats, otherwise have a default gun
		if (random.nextInt(3) < 2) gun = BPASUtils.randomDefaultGun(random);
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(gun));
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/sunken_pirate");
	}

	@Override
	public void aiStep() {
		//Zombie burn in sunlight
		if (isAlive()) {
			boolean shouldBurn = isSunBurnTick();
			if (shouldBurn) {
				ItemStack helmet = getItemBySlot(EquipmentSlot.HEAD);
				if (!helmet.isEmpty()) {
					if (helmet.isDamageableItem()) {
						helmet.setDamageValue(helmet.getDamageValue() + random.nextInt(2));
						if (helmet.getDamageValue() >= helmet.getMaxDamage()) {
							broadcastBreakEvent(EquipmentSlot.HEAD);
							setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
						}
					}

					shouldBurn = false;
				}

				if (shouldBurn) setSecondsOnFire(8);
			}
		}

		super.aiStep();
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	//TODO sounds

	@Override
	protected SoundEvent getAmbientSound() {
		return isInWater() ? SoundEvents.DROWNED_AMBIENT_WATER : SoundEvents.DROWNED_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return isInWater() ? SoundEvents.DROWNED_HURT_WATER : SoundEvents.DROWNED_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return isInWater() ? SoundEvents.DROWNED_DEATH_WATER : SoundEvents.DROWNED_DEATH;
	}

	protected SoundEvent getStepSound() {
		return SoundEvents.DROWNED_STEP;
	}

	@Override
	protected SoundEvent getSwimSound() {
		return SoundEvents.DROWNED_SWIM;
	}

	//Below that is mostly Drowned copy pasted logic
	public boolean okTarget(@Nullable LivingEntity target) {
		if (target != null) {
			return !level().isDay() || target.isInWater();
		}
		else return false;
	}

	@Override
	public boolean isPushedByFluid() {
		return !this.isSwimming();
	}

	protected boolean wantsToSwim() {
		if (searchingForLand) return true;
		else {
			LivingEntity target = getTarget();
			return target != null && target.isInWater();
		}
	}

	@Override
	public void travel(Vec3 p_32394_) {
		if (isControlledByLocalInstance() && isInWater() && wantsToSwim()) {
			moveRelative(0.01F, p_32394_);
			move(MoverType.SELF, getDeltaMovement());
			//for drowneds this is 0.9
			//I'm making the gals faster
			setDeltaMovement(getDeltaMovement().scale(1.25));
		}
		else {
			super.travel(p_32394_);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public void updateSwimming() {
		if (!level().isClientSide) {
			if (isEffectiveAi() && isInWater() && wantsToSwim()) {
				navigation = waterNavigation;
				setSwimming(true);
			}
			else {
				navigation = groundNavigation;
				setSwimming(false);
			}
		}

	}

	@Override
	public boolean isVisuallySwimming() {
		return isSwimming();
	}

	protected boolean closeToNextPos() {
		Path path = getNavigation().getPath();
		if (path != null) {
			BlockPos target = path.getTarget();
			if (target != null) {
				double distSqr = distanceToSqr(target.getX(), target.getY(), target.getZ());
				if (distSqr < 4) return true;
			}
		}
		return false;
	}

	public void setSearchingForLand(boolean searching) {
		searchingForLand = searching;
	}

	//Most of those are Drowned, copy pasted cause private classes
	//Will move them out if I do variants
	private static class DrownedMoveControl extends MoveControl {
		private final SunkenPirateEntity mob;

		public DrownedMoveControl(SunkenPirateEntity mob) {
			super(mob);
			this.mob = mob;
		}

		@Override
		public void tick() {
			LivingEntity target = mob.getTarget();
			if (mob.wantsToSwim() && mob.isInWater()) {
				if (target != null && target.getY() > mob.getY() || mob.searchingForLand) {
					mob.setDeltaMovement(mob.getDeltaMovement().add(0, 0.002, 0));
				}

				if (operation != MoveControl.Operation.MOVE_TO || mob.getNavigation().isDone()) {
					mob.setSpeed(0);
					return;
				}

				double dx = this.wantedX - this.mob.getX();
				double dy = this.wantedY - this.mob.getY();
				double dz = this.wantedZ - this.mob.getZ();
				double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
				dy /= dist;
				float targetAngle = (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90;
				mob.setYRot(rotlerp(mob.getYRot(), targetAngle, 90));
				mob.yBodyRot = mob.getYRot();
				float targetSpeed = (float) (speedModifier * mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
				float speed = Mth.lerp(0.125F, mob.getSpeed(), targetSpeed);
				mob.setSpeed(speed);
				mob.setDeltaMovement(mob.getDeltaMovement().add(speed * dx * 0.005, speed * dy * 0.1, speed * dz * 0.005));
			}
			else {
				if (!mob.onGround()) {
					mob.setDeltaMovement(mob.getDeltaMovement().add(0, -0.008, 0));
				}
				super.tick();
			}

		}
	}

	//Go for melee attacks
	private static class DrownedAttackGoal extends MeleeAttackGoal {
		//TODO speed up when near to dash?
		//and prevent gun when near
		private final SunkenPirateEntity mob;

		public DrownedAttackGoal(SunkenPirateEntity mob, double speed, boolean seeThroughWalls) {
			super(mob, speed, seeThroughWalls);
			this.mob = mob;
		}

		@Override
		public boolean canUse() {
			return super.canUse() && mob.okTarget(mob.getTarget());
		}

		@Override
		public boolean canContinueToUse() {
			return super.canContinueToUse() && mob.okTarget(mob.getTarget());
		}
	}

	//Gun attacks
	private static class PirateGunGoal extends GunGoal<SunkenPirateEntity> {
		//Does the drowned target check (night or water) and also stops when target is too close
		private double minRangeSqr;

		public PirateGunGoal(SunkenPirateEntity mob, double speed, int minAttackInterval, double strafeRange, double maxRange, double minRange) {
			super(mob, speed, minAttackInterval, strafeRange, maxRange);
			minRangeSqr = minRange * minRange;
		}

		@Override
		public boolean canUse() {
			//super will do the target != null check
			LivingEntity target = mob.getTarget();
			return super.canUse() && mob.okTarget(target) && mob.distanceToSqr(target) > minRangeSqr;
		}

		@Override
		public boolean canContinueToUse() {
			//super might not do a target != null, so have to do one more on the distance check
			LivingEntity target = mob.getTarget();
			return super.canContinueToUse() && mob.okTarget(target) && (target == null || mob.distanceToSqr(target) > minRangeSqr);
		}
	}

	//Go to water during the day
	private static class DrownedGoToWaterGoal extends Goal {
		private final PathfinderMob mob;
		private double wantedX;
		private double wantedY;
		private double wantedZ;
		private final double speedModifier;
		private final Level level;

		public DrownedGoToWaterGoal(PathfinderMob mob, double speed) {
			this.mob = mob;
			speedModifier = speed;
			level = mob.level();
			setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			if (!level.isDay() || mob.isInWater()) return false;
			else {
				Vec3 water = getWaterPos();
				if (water == null) return false;
				else {
					wantedX = water.x;
					wantedY = water.y;
					wantedZ = water.z;
					return true;
				}
			}
		}

		@Override
		public boolean canContinueToUse() {
			return !mob.getNavigation().isDone();
		}

		@Override
		public void start() {
			mob.getNavigation().moveTo(wantedX, wantedY, wantedZ, speedModifier);
		}

		@Nullable
		private Vec3 getWaterPos() {
			RandomSource random = mob.getRandom();
			BlockPos mobPos = mob.blockPosition();

			for (int i = 0; i < 10; ++i) {
				BlockPos target = mobPos.offset(random.nextInt(20) - 10, 2 - random.nextInt(8), random.nextInt(20) - 10);
				if (level.getBlockState(target).is(Blocks.WATER)) return Vec3.atBottomCenterOf(target);
			}

			return null;
		}
	}

	//Go to the beach at night
	private static class DrownedGoToBeachGoal extends MoveToBlockGoal {
		private final SunkenPirateEntity mob;

		public DrownedGoToBeachGoal(SunkenPirateEntity mob, double speed) {
			super(mob, speed, 8, 2);
			this.mob = mob;
		}

		@Override
		public boolean canUse() {
			return super.canUse() && !mob.level().isDay() && mob.isInWater() && mob.getY() >= mob.level().getSeaLevel() - 3;
		}

		@Override
		protected boolean isValidTarget(LevelReader world, BlockPos target) {
			BlockPos above = target.above();
			return world.isEmptyBlock(above) && world.isEmptyBlock(above.above()) ? world.getBlockState(target).entityCanStandOn(world, target, mob) : false;
		}

		@Override
		public void start() {
			mob.setSearchingForLand(false);
			mob.navigation = mob.groundNavigation;
			super.start();
		}
	}

	//Swim to the surface during the night
	private static class DrownedSwimUpGoal extends Goal {
		private final SunkenPirateEntity mob;
		private final double speedModifier;
		private final int seaLevel;
		private boolean stuck;

		public DrownedSwimUpGoal(SunkenPirateEntity mob, double speed, int seaLevel) {
			this.mob = mob;
			this.speedModifier = speed;
			this.seaLevel = seaLevel;
		}

		@Override
		public boolean canUse() {
			return !mob.level().isDay() && mob.isInWater() && mob.getY() < seaLevel - 2;
		}

		@Override
		public boolean canContinueToUse() {
			return canUse() && !stuck;
		}

		@Override
		public void tick() {
			if (mob.getY() < seaLevel - 1 && (mob.getNavigation().isDone() || mob.closeToNextPos())) {
				Vec3 vec3 = DefaultRandomPos.getPosTowards(mob, 4, 8, new Vec3(mob.getX(), seaLevel - 1, mob.getZ()), Mth.HALF_PI);
				if (vec3 == null) {
					stuck = true;
					return;
				}

				mob.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, speedModifier);
			}

		}

		@Override
		public void start() {
			mob.setSearchingForLand(true);
			stuck = false;
		}

		@Override
		public void stop() {
			mob.setSearchingForLand(false);
		}
	}

}
