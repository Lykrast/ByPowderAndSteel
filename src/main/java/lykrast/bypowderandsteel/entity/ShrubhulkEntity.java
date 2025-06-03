package lykrast.bypowderandsteel.entity;

import java.util.EnumSet;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ai.ApproachTargetGoal;
import lykrast.bypowderandsteel.registry.BPASSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ShrubhulkEntity extends AnimatedMonster {
	//animations
	public static final int ANIM_NEUTRAL = 0, ANIM_WINDUP = 1, ANIM_SLAM = 2;

	public ShrubhulkEntity(EntityType<? extends ShrubhulkEntity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(3, new SlamAttack(this, 2));
		//this one makes it approach target without actually doing a melee attack
		goalSelector.addGoal(4, new ApproachTargetGoal(this, 1, false));
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 50).add(Attributes.ATTACK_DAMAGE, 10).add(Attributes.MOVEMENT_SPEED, 0.22).add(Attributes.KNOCKBACK_RESISTANCE, 1);
	}

	@Override
	protected void setupNewAnimation() {
		if (clientAnim == ANIM_SLAM) animDur = 3;
		else if (clientAnim == ANIM_WINDUP) animDur = 9;
		else animDur = 10;
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/shrubhulk");
	}

	@Override
	public void tick() {
		super.tick();

		if (level().isClientSide() && clientAnim == ANIM_SLAM && animProg == 2) {
			level().addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ(), 0, 0, 0);
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return BPASSounds.shrubIdle.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BPASSounds.shrubHurt.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BPASSounds.shrubDeath.get();
	}

	private static class SlamAttack extends Goal {
		private ShrubhulkEntity hulk;
		private double startRangeSqr;
		private int timer;
		private boolean windup;

		public SlamAttack(ShrubhulkEntity hulk, double startRange) {
			this.hulk = hulk;
			startRangeSqr = startRange * startRange;
			setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return hulk.getTarget() != null && hulk.distanceToSqr(hulk.getTarget()) <= startRangeSqr;
		}

		@Override
		public boolean canContinueToUse() {
			return windup || timer > 0;
		}

		@Override
		public void start() {
			windup = true;
			timer = 15;
			hulk.setAnimation(ANIM_WINDUP);
		}

		@Override
		public void stop() {
			hulk.setAnimation(ANIM_NEUTRAL);
		}

		@Override
		public void tick() {
			timer--;
			if (windup && timer < 0) {
				//timer for the winddown
				timer = 15;
				windup = false;
				//perform the slam
				//to match the explosion visuals it needs to be 6x6x6 (so 3 on each side from center)
				//shrubhulk's base is 0.99x2.4x0.99 so +2.5 on each side for x/z and 1.8 for y
				for (LivingEntity target : hulk.level().getEntitiesOfClass(LivingEntity.class, hulk.getBoundingBox().inflate(2.5, 1.8, 2.5))) {
					//TODO reduce friendly fire between forest monsters
					if (target.isAlive() && !target.isInvulnerable() && target != hulk && !hulk.isAlliedTo(target)) {
						if (hulk.doHurtTarget(target)) {
							double mult = Math.max(0, 1 - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
							target.setDeltaMovement(target.getDeltaMovement().add(0, 0.3 * mult, 0));
						}
					}
				}
				hulk.playSound(SoundEvents.GENERIC_EXPLODE, 1, (1 + (hulk.random.nextFloat() - hulk.random.nextFloat()) * 0.2F) * 0.7F);
				//particles are for client so will be on the animation itself
			}
			else if (windup && timer == 3) hulk.setAnimation(ANIM_SLAM);
			else if (!windup && timer == 10) hulk.setAnimation(ANIM_NEUTRAL);
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

	}

}
