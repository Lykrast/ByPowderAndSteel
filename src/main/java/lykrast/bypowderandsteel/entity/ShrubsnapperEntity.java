package lykrast.bypowderandsteel.entity;

import java.util.EnumSet;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ai.CircleTargetGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
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
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShrubsnapperEntity extends AnimatedMonster {
	//animations
	public static final int ANIM_NEUTRAL = 0, ANIM_WINDUP = 1, ANIM_SLAM = 2, ANIM_WINDDOWN = 3;
	private int attackCooldown;

	public ShrubsnapperEntity(EntityType<? extends ShrubsnapperEntity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(3, new FangAttack(this, 8));
		goalSelector.addGoal(4, new CircleTargetGoal(this, 1, 8));
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30).add(Attributes.ATTACK_DAMAGE, 6).add(Attributes.MOVEMENT_SPEED, 0.22).add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
	}

	@Override
	protected void setupNewAnimation() {
		if (clientAnim == ANIM_SLAM || clientAnim == ANIM_WINDDOWN) animDur = 3;
		else if (clientAnim == ANIM_WINDUP) animDur = 9;
		else animDur = 10;
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/shrubsnapper");
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

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
		//eyes are lower because it's tilting forward
		return dimensions.height * 0.75F;
	}

	//TODO sounds
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ZOMBIE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ZOMBIE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ZOMBIE_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1);
	}

	private static class FangAttack extends Goal {
		private ShrubsnapperEntity snapper;
		private double startRangeSqr;
		private int timer;
		private boolean windup;

		public FangAttack(ShrubsnapperEntity hulk, double startRange) {
			this.snapper = hulk;
			startRangeSqr = startRange * startRange;
			setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return snapper.attackCooldown <= 0 && snapper.getTarget() != null && snapper.distanceToSqr(snapper.getTarget()) <= startRangeSqr;
		}

		@Override
		public boolean canContinueToUse() {
			return windup || timer > 0;
		}

		@Override
		public void start() {
			windup = true;
			timer = 15;
			snapper.setAnimation(ANIM_WINDUP);
			snapper.getNavigation().stop();
			//I'm trying to stop the strafing but no it still sliding aaaaa
			snapper.getMoveControl().setWantedPosition(snapper.getX(), snapper.getY(), snapper.getZ(), 0);
			snapper.getLookControl().setLookAt(snapper.getTarget(), 30, 30);
		}

		@Override
		public void stop() {
			snapper.setAnimation(ANIM_NEUTRAL);
		}

		@Override
		public void tick() {
			timer--;
			if (windup && timer < 0) {
				//timer for the winddown
				timer = 35;
				windup = false;
				snapper.attackCooldown = 50 + snapper.random.nextInt(11);
				//summon the fangs
				LivingEntity target = snapper.getTarget();
				if (target != null) {
					double yMin = Math.min(target.getY(), snapper.getY());
					double yMax = Math.max(target.getY(), snapper.getY()) + 1;
					float angleToTarget = (float) Mth.atan2(target.getZ() - snapper.getZ(), target.getX() - snapper.getX());
					//one on target
					createFang(target.getX(), target.getZ(), yMin, yMax, angleToTarget, 0);
					//one near target with a delay
					for (int i = 0; i < 3; i++) {
						float ang = snapper.random.nextFloat() * Mth.TWO_PI;
						if (createFang(target.getX() + Mth.cos(ang) * 2.5, target.getZ() + Mth.sin(ang) * 2.5, yMin, yMax, ang, 6)) break;
					}
				}
			}
			else if (windup && timer == 3) snapper.setAnimation(ANIM_SLAM);
			else if (!windup && timer == 13) snapper.setAnimation(ANIM_WINDDOWN);
			else if (!windup && timer == 10) snapper.setAnimation(ANIM_NEUTRAL);
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}
		
		//Copied from evoker but with a return if the claw could be spawned
		//TODO custom fangs
		private boolean createFang(double x, double z, double yMin, double yMax, float rot, int delay) {
			BlockPos pos = BlockPos.containing(x, yMax, z);
			boolean foundGround = false;
			double yOffset = 0;

			do {
				BlockPos blockpos1 = pos.below();
				BlockState blockstate = snapper.level().getBlockState(blockpos1);
				if (blockstate.isFaceSturdy(snapper.level(), blockpos1, Direction.UP)) {
					if (!snapper.level().isEmptyBlock(pos)) {
						BlockState blockstate1 = snapper.level().getBlockState(pos);
						VoxelShape voxelshape = blockstate1.getCollisionShape(snapper.level(), pos);
						if (!voxelshape.isEmpty()) {
							yOffset = voxelshape.max(Direction.Axis.Y);
						}
					}

					foundGround = true;
					break;
				}

				pos = pos.below();
			} while (pos.getY() >= Mth.floor(yMin) - 1);

			if (foundGround) snapper.level().addFreshEntity(new EvokerFangs(snapper.level(), x, pos.getY() + yOffset, z, rot, delay, snapper));
			return foundGround;
		}

	}

}
