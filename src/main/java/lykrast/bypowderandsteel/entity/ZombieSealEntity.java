package lykrast.bypowderandsteel.entity;

import java.util.EnumSet;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ai.ApproachTargetGoal;
import lykrast.bypowderandsteel.entity.ai.AvoidTargetGoal;
import lykrast.bypowderandsteel.misc.BPASUtils;
import lykrast.bypowderandsteel.registry.BPASItems;
import lykrast.bypowderandsteel.registry.BPASSounds;
import lykrast.gunswithoutroses.item.BulletItem;
import lykrast.gunswithoutroses.item.GunItem;
import lykrast.gunswithoutroses.registry.GWRAttributes;
import lykrast.gunswithoutroses.registry.GWRItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class ZombieSealEntity extends Monster implements GunMob {
	//from guardian for the beam
	private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(ZombieSealEntity.class, EntityDataSerializers.INT);
	@Nullable
	private LivingEntity clientSideCachedAttackTarget;
	private int attackCooldown;

	public ZombieSealEntity(EntityType<? extends ZombieSealEntity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(2, new RestrictSunGoal(this));
		goalSelector.addGoal(3, new FleeSunGoal(this, 1));
		goalSelector.addGoal(4, new SnipeGoal(this));
		goalSelector.addGoal(5, new AvoidTargetGoal(this, 4, 8, 1.2));
		goalSelector.addGoal(6, new ApproachTargetGoal(this, 1, false, false));
		goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	@SuppressWarnings("unchecked")
	@Override
	public BulletItem getBullet() {
		return GWRItems.ironBullet.get();
	}

	@Override
	public double getAddedSpread() {
		return 0;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return BPASUtils.baseGunMobAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.ARMOR, 4).add(Attributes.MOVEMENT_SPEED, 0.23).add(Attributes.FOLLOW_RANGE, 32)
				.add(GWRAttributes.dmgBase.get(), -4).add(GWRAttributes.fireDelay.get(), 3);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_ID_ATTACK_TARGET, 0);
	}

	@Override
	public void customServerAiStep() {
		if (attackCooldown > 0) attackCooldown--;
		super.customServerAiStep();
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("AttackCooldown")) attackCooldown = compound.getInt("AttackCooldown");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("AttackCooldown", attackCooldown);
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
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(BPASItems.arcticSniper.get()));
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/zombie_seal");
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
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

	//target from guardian, for rendering the beeem
	protected void setActiveAttackTarget(int id) {
		entityData.set(DATA_ID_ATTACK_TARGET, id);
	}

	public boolean hasActiveAttackTarget() {
		return entityData.get(DATA_ID_ATTACK_TARGET) != 0;
	}

	@Nullable
	public LivingEntity getActiveAttackTarget() {
		if (!this.hasActiveAttackTarget()) return null;
		else if (this.level().isClientSide) {
			if (this.clientSideCachedAttackTarget != null) return clientSideCachedAttackTarget;
			else {
				Entity entity = level().getEntity(entityData.get(DATA_ID_ATTACK_TARGET));
				if (entity instanceof LivingEntity) {
					clientSideCachedAttackTarget = (LivingEntity) entity;
					return clientSideCachedAttackTarget;
				}
				else return null;
			}
		}
		else return getTarget();
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
		super.onSyncedDataUpdated(accessor);
		if (DATA_ID_ATTACK_TARGET.equals(accessor)) clientSideCachedAttackTarget = null;

	}

	@Override
	protected SoundEvent getAmbientSound() {
		return BPASSounds.sealIdle.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BPASSounds.sealHurt.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BPASSounds.sealDeath.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1);
	}

	private static class SnipeGoal extends Goal {
		//Based on the guardian one a bit
		private ZombieSealEntity seal;
		private LivingEntity target;
		private int seeTime;

		public SnipeGoal(ZombieSealEntity seal) {
			this.seal = seal;
			setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		protected boolean isHoldingGun() {
			return seal.isHolding(is -> is.getItem() instanceof GunItem);
		}

		@Override
		public boolean canUse() {
			return seal.attackCooldown <= 0 && seal.getTarget() != null && isHoldingGun() && seal.hasLineOfSight(seal.getTarget());
		}

		@Override
		public boolean canContinueToUse() {
			return seal.attackCooldown <= 0 && target != null && target.isAlive() && seeTime > -20 && isHoldingGun();
		}

		@Override
		public void start() {
			seal.getNavigation().stop();
			seeTime = 0;
			target = seal.getTarget();
			if (target != null) {
				seal.getLookControl().setLookAt(target, 90, 90);
				seal.setActiveAttackTarget(target.getId());
				seal.setAggressive(true);
				//TODO targeting sound
			}
		}

		@Override
		public void stop() {
			seal.setActiveAttackTarget(0);
			seal.setAggressive(false);
			if (seal.attackCooldown <= 0) seal.attackCooldown = 5;
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			if (target != null) {
				seal.getNavigation().stop();
				seal.getLookControl().setLookAt(target, 90, 90);
				seal.setActiveAttackTarget(target.getId());
				if (!seal.hasLineOfSight(target)) {
					seeTime -= 2;
				}
				else {
					if (seeTime < 0) seeTime = 0;
					seeTime++;
					if (seeTime >= 50) {
						ItemStack gun =  seal.getMainHandItem();
						GunItem gunItem = (GunItem) gun.getItem();
						ItemStack bullet = seal.getBulletStack();
						seal.attackCooldown = gunItem.getFireDelay(gun, seal);
						gunItem.shootAt(seal, target, gun, bullet, seal.getBullet(), seal.getAddedSpread(), true);
						//volume 4 should be heard 64 blocks away (headshot sound is 5)
						seal.playSound(gunItem.getFireSound(), 4, 1.0F / (seal.getRandom().nextFloat() * 0.4F + 0.8F));
					}
				}
			}
		}
	}
}
