package lykrast.bypowderandsteel.entity;

import java.util.EnumSet;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ai.GunGoal;
import lykrast.bypowderandsteel.misc.BPASUtils;
import lykrast.bypowderandsteel.registry.BPASItems;
import lykrast.gunswithoutroses.item.BulletItem;
import lykrast.gunswithoutroses.registry.GWRAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

public class SkybenderEntity extends Monster implements GunMob {
	//I'm so mad the vanilla endermen have a spawn weight of 1 in the end so I can't be granular aaaaaaaa
	public SkybenderEntity(EntityType<? extends SkybenderEntity> type, Level world) {
		super(type, world);
		//like bees
		moveControl = new FlyingMoveControl(this, 20, true);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new GunGoal<>(this, 1, 4, -1, 8));
		goalSelector.addGoal(2, new WanderGoal(this));
		goalSelector.addGoal(3, new FloatGoal(this));
		goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	@SuppressWarnings("unchecked")
	@Override
	public BulletItem getBullet() {
		return BPASItems.graviticBullet.get();
	}

	@Override
	public double getAddedSpread() {
		//same as skeletons, 10/6/2 for easy/normal/hard
		return 14 - level().getDifficulty().getId() * 4;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return BPASUtils.baseGunMobAttributes().add(Attributes.MAX_HEALTH, 30).add(Attributes.ARMOR, 15).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.FLYING_SPEED, 0.6)
				.add(GWRAttributes.dmgTotal.get(), 0.75).add(GWRAttributes.fireDelay.get(), 3);
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
		Item gun = BPASItems.raygun.get();
		//1/3 of the time have the raygun, otherwise have a default gun
		if (random.nextInt(3) < 2) gun = BPASUtils.randomDefaultGun(random);
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(gun));
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/skybender");
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
		//model is 18px, eye at 13px
		return dimensions.height * 0.722F;
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

	//TODO sounds
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.BLAZE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.BLAZE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.BLAZE_DEATH;
	}

	private static class WanderGoal extends Goal {
		//based on beewander
		protected SkybenderEntity skibidi;

		private WanderGoal(SkybenderEntity skibidi) {
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

}
