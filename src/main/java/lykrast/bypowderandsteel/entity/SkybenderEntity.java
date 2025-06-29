package lykrast.bypowderandsteel.entity;

import java.util.EnumSet;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.registry.BPASSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

public class SkybenderEntity extends AnimatedMonster {
	//I'm so mad the vanilla endermen have a spawn weight of 1 in the end so I can't be granular aaaaaaaa
	//animations
	public static final int ANIM_NEUTRAL = 0, ANIM_SHIELD = 1, ANIM_WINDUP = 2, ANIM_SLASH = 3, ANIM_STUN = 4;

	public SkybenderEntity(EntityType<? extends SkybenderEntity> type, Level world) {
		super(type, world);
		//like bees
		moveControl = new FlyingMoveControl(this, 20, true);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new ShieldedChaseAndSwing(this, 1.2, false));
		goalSelector.addGoal(2, new WanderGoal(this));
		goalSelector.addGoal(3, new FloatGoal(this));
		goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	@Override
	protected void setupNewAnimation() {
		if (clientAnim == ANIM_SLASH || clientAnim == ANIM_STUN) animDur = 3;
		else if (clientAnim == ANIM_WINDUP || clientAnim == ANIM_SHIELD) animDur = 7;
		else animDur = 10;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30).add(Attributes.ARMOR, 15).add(Attributes.ATTACK_DAMAGE, 12).add(Attributes.MOVEMENT_SPEED, 0.3)
				.add(Attributes.FLYING_SPEED, 0.6);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		//shield blocking
		if (getAnimation() == ANIM_SHIELD) {
			if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !source.is(DamageTypeTags.BYPASSES_SHIELD)) {
				//same angle check as shields
				Vec3 vec32 = source.getSourcePosition();
				if (vec32 != null) {
					Vec3 vec3 = this.getViewVector(1.0F);
					Vec3 vec31 = vec32.vectorTo(this.position()).normalize();
					vec31 = new Vec3(vec31.x, 0.0D, vec31.z);
					if (vec31.dot(vec3) < 0.0D) {
						//damage is blocked
						playSound(BPASSounds.skybenderShield.get(), 1, 1);
						return false;
					}
				}
			}
			//was blocking but hit was not blocked
			//TODO stun
		}
		return super.hurt(source, amount);
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

	//TODO proper sounds, want a skibidi toilet
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

	public void swing() {
		//eyeballed by copying what the saber sentry does
		AABB hitbox = getBoundingBox().inflate(0.75, 0.25, 0.75).move(Vec3.directionFromRotation(0, getYRot()));
		for (LivingEntity entity : level().getEntitiesOfClass(LivingEntity.class, hitbox)) {
			if (entity != this && (entity == getTarget() || !(entity instanceof SkybenderEntity)) && entity.isAlive()) {
				if (doHurtTarget(entity)) {
					//gravitic saber effect
					double mult = Math.max(0, 1 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
					entity.push(0, 0.5 * mult, 0);
					entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 0));
				}
			}
		}
		playSound(BPASSounds.saberSwing.get(), 1, 1);
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

	private static class ShieldedChaseAndSwing extends MeleeAttackGoal {
		//based on the saber sentry behavior
		//TODO want to follow eye level instead of ground level (so it stays a bit in the air)
		//because the fucking stuff is private, will need to copy paste all of melee attack goal
		//so later
		private SkybenderEntity sentry;
		//0 approach (shielded), 1 windup, 2 recover
		private int phase;
		//ticksUntilNextAttack is private so I'm making my own countdown
		private int time;
		//TODO THE SPEED MODIFIER IS PRIVAAAATE have to actually copy the logic to redo it

		public ShieldedChaseAndSwing(SkybenderEntity sentry, double speed, boolean seeThroughWalls) {
			super(sentry, speed, seeThroughWalls);
			this.sentry = sentry;
		}

		@Override
		public void start() {
			super.start();
			sentry.setAnimation(ANIM_SHIELD);
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
			super.tick();
		}

		@Override
		protected void checkAndPerformAttack(LivingEntity target, double distanceSqr) {
			//this is only called when target is alive, so we do the transition animations above
			switch (phase) {
				case 0:
					//running at target
					if (distanceSqr < 6) {
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
						sentry.setAnimation(ANIM_SHIELD);
						phase = 0;
					}
					else if (time <= 0 && distanceSqr <= 3) {
						//swing!
						//sentry.setAnimation(ANIM_SLASH);
						time = 20;
						phase = 2;
					}
					break;
				case 2:
					//swinging
					if (time <= 0) {
						//recovery animation done, prepare another attack if close, or get shields back
						if (distanceSqr < 6) {
							sentry.setAnimation(ANIM_WINDUP);
							phase = 1;
							time = 8;
						}
						else {
							sentry.setAnimation(ANIM_SHIELD);
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
			}
		}

		@Override
		public boolean canContinueToUse() {
			if (phase > 0 && time > 0) return true;
			return super.canContinueToUse();
		}
	}
}
