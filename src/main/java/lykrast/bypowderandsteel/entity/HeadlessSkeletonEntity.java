package lykrast.bypowderandsteel.entity;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ai.WaterAvoidingRandomStrollEvenWhenVehicleGoal;
import lykrast.bypowderandsteel.registry.BPASEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.ForgeEventFactory;

public class HeadlessSkeletonEntity extends AbstractSkeleton {
	public HeadlessSkeletonEntity(EntityType<? extends HeadlessSkeletonEntity> type, Level world) {
		super(type, world);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return AbstractSkeleton.createAttributes().add(Attributes.MAX_HEALTH, 16);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(2, new RestrictSunGoal(this));
		goalSelector.addGoal(3, new FleeSunGoal(this, 1));
		goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 6, 1, 1.2));
		//copying the whole thing just to replace this one
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollEvenWhenVehicleGoal(this, 1));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
		super.populateDefaultEquipmentSlots(random, difficulty);
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_AXE));
		setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
	}

	@Override
	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag nbt) {
		groupData = super.finalizeSpawn(level, difficulty, spawnType, groupData, nbt);
		RandomSource randomsource = level.getRandom();
		if (randomsource.nextInt(10) > 0) {
			EnsouledSkullEntity skull = BPASEntities.ensouledSkull.get().create(level());
			if (skull != null) {
				skull.moveTo(getX(), getY(), getZ(), getYRot(), 0);
				ForgeEventFactory.onFinalizeSpawn(skull, level, difficulty, MobSpawnType.JOCKEY, groupData, null);
				skull.startRiding(this);
			}
		}

		return groupData;
	}

	@Override
	public LivingEntity getControllingPassenger() {
		LivingEntity controlling = super.getControllingPassenger();
		return controlling instanceof EnsouledSkullEntity ? null : controlling;
	}

	@Override
	public double getPassengersRidingOffset() {
		return getBbHeight();
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		boolean ret = super.hurt(source, amount);
		//eject skull if we end up under half health
		if (!getPassengers().isEmpty() && getHealth() <= getMaxHealth() / 2.0) {
			if (getFirstPassenger()instanceof EnsouledSkullEntity skull) {
				Entity damager = source.getEntity();
				if (damager != null) skull.eject(getX() - damager.getX(), getZ() - damager.getZ());
				else skull.eject();
			}
		}
		return ret;
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/headless_skeleton");
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
		return dimensions.height;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.SKELETON_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.SKELETON_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.SKELETON_DEATH;
	}

	@Override
	protected SoundEvent getStepSound() {
		return SoundEvents.SKELETON_STEP;
	}
}
