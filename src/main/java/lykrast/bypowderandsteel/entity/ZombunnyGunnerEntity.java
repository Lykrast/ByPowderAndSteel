package lykrast.bypowderandsteel.entity;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.config.BPASConfigValues;
import lykrast.bypowderandsteel.entity.ai.GunGoal;
import lykrast.bypowderandsteel.misc.BPASUtils;
import lykrast.bypowderandsteel.registry.BPASEffects;
import lykrast.bypowderandsteel.registry.BPASEntities;
import lykrast.bypowderandsteel.registry.BPASSounds;
import lykrast.gunswithoutroses.item.BulletItem;
import lykrast.gunswithoutroses.registry.GWRAttributes;
import lykrast.gunswithoutroses.registry.GWRItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.SpawnPlacements;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class ZombunnyGunnerEntity extends Monster implements GunMob {
	public ZombunnyGunnerEntity(EntityType<? extends ZombunnyGunnerEntity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(4, new ZombunnyGunGoal(this, 1, 4, 6, 7));
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
		return GWRItems.ironBullet.get();
	}

	@Override
	public double getAddedSpread() {
		//same as skeletons, 10/6/2 for easy/normal/hard
		return 14 - level().getDifficulty().getId() * 4;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return BPASUtils.baseGunMobAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.MOVEMENT_SPEED, 0.28).add(GWRAttributes.dmgTotal.get(), 1.0 / 3).add(GWRAttributes.fireDelay.get(), 3);
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
		//spawn slashers as well
		if (BPASConfigValues.ZOMBUNNY_SQUAD && (spawnType == MobSpawnType.NATURAL || spawnType == MobSpawnType.CHUNK_GENERATION) && random.nextInt(4) > 0) {
			//based on zombie reinforcement code
			ZombunnySlasherEntity slasher = BPASEntities.zombunnySlasher.get().create(level());
			EntityType<?> entitytype = slasher.getType();
			int sx = Mth.floor(getX());
			int sy = Mth.floor(getY());
			int sz = Mth.floor(getZ());

			for (int attempt = 0; attempt < 20; ++attempt) {
				//the highest I seen that suceeded was like 17, but many like 0-3, maybe 12
				int tx = sx + Mth.nextInt(random, 1, 4) * Mth.nextInt(random, -1, 1);
				int ty = sy + Mth.nextInt(random, 1, 4) * Mth.nextInt(random, -1, 1);
				int tz = sz + Mth.nextInt(random, 1, 4) * Mth.nextInt(random, -1, 1);
				BlockPos target = new BlockPos(tx, ty, tz);
				SpawnPlacements.Type spawnplacements$type = SpawnPlacements.getPlacementType(entitytype);
				if (NaturalSpawner.isSpawnPositionOk(spawnplacements$type, level(), target, entitytype) && SpawnPlacements.checkSpawnRules(entitytype, world, spawnType, target, random)) {
					slasher.setPos(tx, ty, tz);
					ForgeEventFactory.onFinalizeSpawn(slasher, world, difficulty, spawnType, groupData, null);
					world.addFreshEntityWithPassengers(slasher);
					//slasher.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60 * 20));
					break;
				}
			}
		}

		return groupData;
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(BPASUtils.randomDefaultGun(random)));
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/zombunny_gunner");
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	@Override
	public int getMaxFallDistance() {
		//they're immune to fall damage
		return 32;
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
	public boolean doHurtTarget(Entity target) {
		if (super.doHurtTarget(target)) {
			//mob attacks don't call sword hurtEnemy aaaaaa
			//so cheating by putting the status effect here like cave spiders
			if (target instanceof LivingEntity ltarget) {
				ltarget.addEffect(new MobEffectInstance(BPASEffects.marked.get(), 5 * 20, 0), this);
			}
			return true;
		}
		else return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return BPASSounds.zombunnyIdle.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BPASSounds.zombunnyHurt.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BPASSounds.zombunnyDeath.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1);
	}

	private static class ZombunnyGunGoal extends GunGoal<ZombunnyGunnerEntity> {
		public ZombunnyGunGoal(ZombunnyGunnerEntity mob, double speed, int minAttackInterval, double strafeRange, double attackRange) {
			super(mob, speed, minAttackInterval, strafeRange, attackRange);
		}

		@Override
		public void stop() {
			super.stop();
			mob.setPose(Pose.STANDING);
		}

		@Override
		protected void doStrafing(LivingEntity target, double targetDistance) {
			if (strafeRadiusSqr > 0 && targetDistance <= strafeRadiusSqr && seeTime >= 20) {
				mob.getNavigation().stop();
				//don't increment strafe while in the air so the jumps are more spaced
				if (mob.onGround()) ++strafingTime;
			}
			else {
				mob.getNavigation().moveTo(target, speedModifier);
				strafingTime = -1;
				mob.setPose(Pose.STANDING);
			}

			//mixing strafing replaced with the leaping
			if (strafingTime >= 20) {
				if (mob.getRandom().nextFloat() < 0.3) strafingClockwise = !strafingClockwise;
				Vec3 vec3 = mob.getDeltaMovement();
				Vec3 vec31 = new Vec3(target.getX() - mob.getX(), 0, target.getZ() - mob.getZ());
				if (vec31.lengthSqr() > 1.0E-7D) {
					vec31 = vec31.normalize().scale(0.6);
					if (strafingBackwards) vec31 = vec31.yRot(Mth.PI);
					else if (strafingClockwise) vec31 = vec31.yRot(Mth.HALF_PI * 0.75f); //I'm actually not sure the clockwise/counterclockwise aren't inverted but eeeeeh
					else vec31 = vec31.yRot(-Mth.HALF_PI * 0.75f);
					vec31 = vec31.add(vec3.scale(0.2));
				}
				mob.setDeltaMovement(vec31.x, 0.5, vec31.z);
				mob.setPose(Pose.STANDING);
				//TODO dedicated jump sound
				mob.playSound(SoundEvents.RABBIT_JUMP, 1, 1);
				strafingTime = 0;
				//only mix up forward/backwards after leaping
				if (mob.getRandom().nextFloat() < 0.3) strafingBackwards = !strafingBackwards;
			}
			else if (strafingTime == 10) {
				//crouch before jumping
				mob.setPose(Pose.CROUCHING);
			}

			if (strafingTime > -1) {
				if (targetDistance > strafeRadiusSqr * 0.75) strafingBackwards = false;
				else if (targetDistance < strafeRadiusSqr * 0.25) strafingBackwards = true;

				Entity vehicle = mob.getControlledVehicle();
				if (vehicle instanceof Mob) {
					Mob mob = (Mob) vehicle;
					mob.lookAt(target, 30, 30);
				}

				mob.lookAt(target, 30, 30);
			}
			else {
				mob.getLookControl().setLookAt(target, 30, 30);
			}
		}
	}
}
