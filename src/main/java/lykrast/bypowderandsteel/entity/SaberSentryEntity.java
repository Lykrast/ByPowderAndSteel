package lykrast.bypowderandsteel.entity;

import java.time.LocalDateTime;
import java.time.Month;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.config.BPASConfigValues;
import lykrast.bypowderandsteel.registry.BPASSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SaberSentryEntity extends AnimatedMonster {
	//127 or something should be enough
	private static final EntityDataAccessor<Byte> DATA_COSMETIC = SynchedEntityData.defineId(SaberSentryEntity.class, EntityDataSerializers.BYTE);
	public static final int COSMETICS = 19; //well 20 with 0 being the default, but that's the cap for array index
	//animations
	public static final int ANIM_NEUTRAL = 0, ANIM_RUN = 1, ANIM_WINDUP = 2, ANIM_SLASH = 3, ANIM_SPIN_START = 4, ANIM_SPINNING = 5, ANIM_SPIN_STOP = 6;
	public int spinTime;
	public float spinAngle, spinPrev;

	public SaberSentryEntity(EntityType<? extends SaberSentryEntity> type, Level world) {
		super(type, world);
		spinTime = 0;
		spinAngle = 0;
		spinPrev = 0;
		setMaxUpStep(1.25F);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(3, new ChaseAndSwing(this, 1.2, false));
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30).add(Attributes.ARMOR, 12).add(Attributes.ATTACK_DAMAGE, 8).add(Attributes.MOVEMENT_SPEED, 0.26)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1);
	}

	public static boolean spawnRules(EntityType<? extends Monster> entity, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
		return pos.getY() <= BPASConfigValues.SABER_SENTRY_MAX_HEIGHT && Monster.checkMonsterSpawnRules(entity, level, spawnType, pos, random);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_COSMETIC, (byte) 0);
	}

	private void setCosmetic(int value) {
		entityData.set(DATA_COSMETIC, (byte) Math.min(value, COSMETICS));
	}

	public int getCosmetic() {
		return entityData.get(DATA_COSMETIC);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Cosmetic", getCosmetic());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		setCosmetic(tag.getInt("Cosmetic"));
	}

	@Override
	protected void setupNewAnimation() {
		if (clientAnim == ANIM_SLASH) animDur = 3;
		else if (clientAnim == ANIM_WINDUP || clientAnim == ANIM_SPIN_START) animDur = 5;
		else animDur = 10;
	}

	@Override
	public void tick() {
		super.tick();

		if (level().isClientSide()) {
			//same spin logic as dame fortuna
			spinPrev = spinAngle;
			if (clientAnim == ANIM_SPINNING) {
				//spinning up
				if (spinTime < 20) {
					spinTime++;
					//keep spin angle at 0 to know our offset when we spin down
				}
				//stable spinning
				else {
					spinAngle += 36;
					if (spinAngle >= 360) spinAngle = 0;
				}
			}
			else {
				if (spinAngle > 0) {
					//finish our full turn 
					spinAngle += 36;
					if (spinAngle >= 360) spinAngle = 0;
				}
				else if (spinTime > 0) {
					spinTime--;
				}
			}
		}
	}

	//	@SuppressWarnings("resource")
	//	@Override
	//	public void aiStep() {
	//		//Blaze particles for debug
	//		if (level().isClientSide) {
	//			AABB hitbox = getHitbox();
	//			level().addParticle(ParticleTypes.CRIT, hitbox.minX, hitbox.minY, hitbox.minZ, 0, 0, 0);
	//			level().addParticle(ParticleTypes.CRIT, hitbox.minX, hitbox.minY, hitbox.maxZ, 0, 0, 0);
	//			level().addParticle(ParticleTypes.CRIT, hitbox.minX, hitbox.maxY, hitbox.minZ, 0, 0, 0);
	//			level().addParticle(ParticleTypes.CRIT, hitbox.minX, hitbox.maxY, hitbox.maxZ, 0, 0, 0);
	//			level().addParticle(ParticleTypes.CRIT, hitbox.maxX, hitbox.minY, hitbox.minZ, 0, 0, 0);
	//			level().addParticle(ParticleTypes.CRIT, hitbox.maxX, hitbox.minY, hitbox.maxZ, 0, 0, 0);
	//			level().addParticle(ParticleTypes.CRIT, hitbox.maxX, hitbox.maxY, hitbox.minZ, 0, 0, 0);
	//			level().addParticle(ParticleTypes.CRIT, hitbox.maxX, hitbox.maxY, hitbox.maxZ, 0, 0, 0);
	//		}
	//
	//		super.aiStep();
	//	}

	private float getEasedSpin(float progress) {
		//0.9x^2 means we spin a full 360° after 10 ticks
		//and derivative at x = 10 is 36, matching the constant speed rate when stable
		return 0.9f * progress * progress;
	}

	public float getSpinAngle(float partial) {
		if (clientAnim == ANIM_SPINNING) {
			//spinning up
			if (spinTime < 20) return getEasedSpin(spinTime + partial);
			//stable spin
			else return Mth.rotLerp(partial, spinPrev, spinAngle);
		}
		//spinning down
		else {
			//finishing the turn
			if (spinAngle > 0) return Mth.rotLerp(partial, spinPrev, spinAngle);
			//actual spin down
			else if (spinTime > 0) return 360 - getEasedSpin(spinTime - partial);
			else return 0;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag nbt) {
		RandomSource random = world.getRandom();
		//1% chance for a flag blade
		//25% during pride month
		if (random.nextDouble() < (LocalDateTime.now().getMonth() == Month.JUNE ? 0.25 : 0.01)) {
			setCosmetic(random.nextIntBetweenInclusive(1, COSMETICS));
		}
		//super is fine for an override
		return super.finalizeSpawn(world, difficulty, spawnType, groupData, nbt);
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/sabersentry");
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
		//eye at 32px, model is 36px, upscaled by 1.5, but bounding box only 3.3 blocks
		return dimensions.height * 0.9F;
	}

	//TODO proper sound, want an ena voice distorted like the glub slender fortress buzz
	@Override
	protected SoundEvent getAmbientSound() {
		return BPASSounds.sentryIdle.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BPASSounds.sentryHurt.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BPASSounds.sentryDeath.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.IRON_GOLEM_STEP, 0.15F, 1);
	}

	public void swing() {
		//on the model it's like reaching 1 block in front and to either side, so move 1 block in front and extend by 0.5
		//and since the model is scaled by 1.5, 0.75 on either side
		//but it's mostly eyeballed here
		AABB hitbox = getBoundingBox().inflate(0.75, 0, 0.75).move(Vec3.directionFromRotation(0, getYRot()));
		for (LivingEntity entity : level().getEntitiesOfClass(LivingEntity.class, hitbox)) {
			if (entity != this && (entity == getTarget() || !(entity instanceof SaberSentryEntity)) && entity.isAlive()) doHurtTarget(entity);
		}
		playSound(BPASSounds.saberSwing.get(), 1, 1);
	}

	private static class ChaseAndSwing extends MeleeAttackGoal {
		private SaberSentryEntity sentry;
		//0 approach, 1 windup, 2 recover, 3 start spin, 4 spinning, 5 recovering spin
		private int phase;
		//ticksUntilNextAttack is private so I'm making my own countdown
		private int time;
		//TODO THE SPEED MODIFIER IS PRIVAAAATE have to actually copy the logic to redo it

		public ChaseAndSwing(SaberSentryEntity sentry, double speed, boolean seeThroughWalls) {
			super(sentry, speed, seeThroughWalls);
			this.sentry = sentry;
		}

		@Override
		public void start() {
			super.start();
			sentry.setAnimation(ANIM_RUN);
			phase = 0;
			time = 0;
		}

		@Override
		public void stop() {
			super.stop();
			sentry.setAnimation(ANIM_NEUTRAL);
		}

		@Override
		public void tick() {
			if (time > 0) time--;
			//stop spinning if target is dead
			if (phase == 4 && (sentry.getTarget() == null || !sentry.getTarget().isAlive())) {
				sentry.setAnimation(ANIM_SPIN_STOP);
				phase = 5;
				time = 20;
			}
			super.tick();
		}

		@Override
		protected void checkAndPerformAttack(LivingEntity target, double distanceSqr) {
			//this is only called when target is alive, so we do the transition animations above
			switch (phase) {
				case 0:
					//running at target
					if (distanceSqr < 9) {
						//raise arms to prepare attack, with a minimum time
						sentry.setAnimation(ANIM_WINDUP);
						sentry.playSound(BPASSounds.saberSwing.get(), 1, 1);
						phase = 1;
						time = 8;
					}
					break;
				case 1:
					//arms raised
					if (distanceSqr > 16) {
						//target too far, return to run animation
						sentry.setAnimation(ANIM_RUN);
						phase = 0;
					}
					else if (time <= 0 && distanceSqr <= 5) {
						//swing!
						//sentry.setAnimation(ANIM_SLASH);
						time = 20;
						phase = 2;
					}
					break;
				case 2:
					//swinging
					if (time <= 0) {
						//recovery animation done, spin if near, run if far
						if (distanceSqr < 16) {
							sentry.setAnimation(ANIM_SPIN_START);
							sentry.playSound(BPASSounds.saberSwing.get(), 1, 1);
							phase = 3;
							time = 7;
							sentry.getNavigation().stop();
						}
						else {
							sentry.setAnimation(ANIM_RUN);
							phase = 0;
						}
					}
					else {
						//recovery animation
						sentry.getNavigation().stop();
						//and strike when animation is done
						if (time == 15) sentry.setAnimation(ANIM_SLASH);
						else if (time == 13) sentry.swing();
					}
					break;
				case 3:
					//starting to spin
					if (time <= 0) {
						sentry.setAnimation(ANIM_SPINNING);
						phase = 4;
						time = 5;
					}
					else sentry.getNavigation().stop();
					break;
				case 4:
					//spinning
					if (time <= 0) {
						//strike every 5 ticks (180°)
						sentry.swing();
						//if target is too far, stop spinning
						if (distanceSqr > 25) {
							sentry.setAnimation(ANIM_SPIN_STOP);
							phase = 5;
							time = 20;
						}
						else time = 5;
					}
					break;
				case 5:
					//stopping spin, resume chase when done
					if (time <= 0) {
						sentry.setAnimation(ANIM_RUN);
						phase = 0;
					}
					else sentry.getNavigation().stop();
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
