package lykrast.bypowderandsteel.entity;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ai.AvoidTargetGoal;
import lykrast.bypowderandsteel.entity.ai.GunGoal;
import lykrast.bypowderandsteel.misc.BPASUtils;
import lykrast.bypowderandsteel.registry.BPASEntities;
import lykrast.bypowderandsteel.registry.BPASItems;
import lykrast.bypowderandsteel.registry.BPASSounds;
import lykrast.gunswithoutroses.item.BulletItem;
import lykrast.gunswithoutroses.registry.GWRAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class GunomeEntity extends Monster implements GunMob {

	public GunomeEntity(EntityType<? extends GunomeEntity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(3, new AvoidTargetGoal(this, 4, 10, 1.2));
		goalSelector.addGoal(4, new GunGoal<>(this, 1, 4, 10));
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	@SuppressWarnings("unchecked")
	@Override
	public BulletItem getBullet() {
		return BPASItems.caliberry.get();
	}

	@Override
	public double getAddedSpread() {
		//same as skeletons, 10/6/2 for easy/normal/hard
		return 14 - level().getDifficulty().getId() * 4;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return BPASUtils.baseGunMobAttributes().add(Attributes.MAX_HEALTH, 12).add(Attributes.MOVEMENT_SPEED, 0.27).add(GWRAttributes.dmgTotal.get(), 0.5).add(GWRAttributes.fireDelay.get(), 2.5);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
		Item gun = BPASItems.peashooter.get();
		//2/3 of the time have the peashooter, otherwise have a default gun
		if (random.nextInt(3) < 1) gun = BPASUtils.randomDefaultGun(random);
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(gun));
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
		//max gunome tower of 3
		Entity vehicle = getVehicle();
		if (random.nextInt(10) == 0 && (vehicle == null || (vehicle.getVehicle() == null))) {
			GunomeEntity rider = BPASEntities.gunome.get().create(level());
			if (rider != null) {
				rider.moveTo(getX(), getY(), getZ(), getYRot(), 0);
				rider.finalizeSpawn(world, difficulty, spawnType, null, null);
				rider.startRiding(this);
			}
		}

		return groupData;
	}
	
	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/gunome");
	}

	@Override
	public LivingEntity getControllingPassenger() {
		//to make gunome towers work
		LivingEntity controlling = super.getControllingPassenger();
		return controlling instanceof GunomeEntity ? null : controlling;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return BPASSounds.gunomeIdle.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BPASSounds.gunomeHurt.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BPASSounds.gunomeDeath.get();
	}

}
