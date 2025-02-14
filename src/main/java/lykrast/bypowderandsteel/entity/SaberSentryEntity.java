package lykrast.bypowderandsteel.entity;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
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
import net.minecraft.world.level.block.state.BlockState;

public class SaberSentryEntity extends AnimatedMonster {
	//animations
	public static final int ANIM_NEUTRAL = 0, ANIM_RUN = 1, ANIM_WINDUP = 2, ANIM_SLASH = 3;

	public SaberSentryEntity(EntityType<? extends SaberSentryEntity> type, Level world) {
		super(type, world);
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
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30).add(Attributes.ARMOR, 12).add(Attributes.ATTACK_DAMAGE, 10).add(Attributes.MOVEMENT_SPEED, 0.26);
	}

	@Override
	protected void setupNewAnimation() {
		if (clientAnim == ANIM_SLASH) animDur = 3;
		else animDur = 10;
	}

	@Override
	public float maxUpStep() {
		return 1.25f;
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

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.IRON_GOLEM_STEP, 0.15F, 1);
	}

	//kinda all janky for now but should be a good placeholder until I do proper chase and spin attack
	//TODO do my own so I have proper control
	private static class ChaseAndSwing extends MeleeAttackGoal {
		private SaberSentryEntity sentry;
		//0 approach, 1 windup, 2 recover
		private int phase;

		public ChaseAndSwing(SaberSentryEntity sentry, double speed, boolean seeThroughWalls) {
			super(sentry, speed, seeThroughWalls);
			this.sentry = sentry;
		}

		@Override
		public void start() {
			super.start();
			sentry.setAnimation(ANIM_RUN);
			phase = 0;
		}

		@Override
		public void stop() {
			super.stop();
			sentry.setAnimation(ANIM_NEUTRAL);
		}

		@Override
		protected void checkAndPerformAttack(LivingEntity target, double distanceSqr) {
			if (phase == 0) {
				if (distanceSqr < 9) {
					sentry.setAnimation(ANIM_WINDUP);
					resetAttackCooldown();
					phase = 1;
				}
			}
			else if (phase == 1) {
				if (distanceSqr > 16) {
					sentry.setAnimation(ANIM_RUN);
					phase = 0;
				}
				else if (getTicksUntilNextAttack() <= 0 && distanceSqr <= getAttackReachSqr(target)) {
					sentry.setAnimation(ANIM_SLASH);
					sentry.doHurtTarget(target);
					resetAttackCooldown();
					phase = 2;
				}
			}
			else if (phase == 2) {
				if (getTicksUntilNextAttack() <= 0) {
					sentry.setAnimation(ANIM_RUN);
					phase = 0;
				}
				else {
					sentry.getNavigation().stop();
				}
			}
		}

		@Override
		public boolean canContinueToUse() {
			if ((phase == 2 || phase == 1) && getTicksUntilNextAttack() > 0) return true;
			return super.canContinueToUse();
		}
	}

}
