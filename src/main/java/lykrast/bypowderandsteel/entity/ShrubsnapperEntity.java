package lykrast.bypowderandsteel.entity;

import java.util.EnumSet;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
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

	public ShrubsnapperEntity(EntityType<? extends ShrubsnapperEntity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(3, new SlamAttack(this, 5));
		//this one makes it approach target without actually doing a melee attack
		goalSelector.addGoal(4, new MeleeAttackGoal(this, 1, false) {
			@Override
			protected void checkAndPerformAttack(LivingEntity target, double distance) {
			}
		});
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30).add(Attributes.ATTACK_DAMAGE, 8).add(Attributes.MOVEMENT_SPEED, 0.22).add(Attributes.KNOCKBACK_RESISTANCE, 1);
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

	private static class SlamAttack extends Goal {
		private ShrubsnapperEntity snapper;
		private double startRangeSqr;
		private int timer;
		private boolean windup;

		public SlamAttack(ShrubsnapperEntity hulk, double startRange) {
			this.snapper = hulk;
			startRangeSqr = startRange * startRange;
			setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return snapper.getTarget() != null && snapper.distanceToSqr(snapper.getTarget()) <= startRangeSqr;
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
				timer = 20;
				windup = false;
				//summon the fangs
				//TODO one on target, one near target
				LivingEntity target = snapper.getTarget();
				if (target != null) {
					double yMin = Math.min(target.getY(), snapper.getY());
					double yMax = Math.max(target.getY(), snapper.getY()) + 1;
					float f = (float) Mth.atan2(target.getZ() - snapper.getZ(), target.getX() - snapper.getX());
					for (int l = 0; l < 16; ++l) {
						double d2 = 1.25D * (double) (l + 1);
						int j = 1 * l;
						createFang(snapper.getX() + (double) Mth.cos(f) * d2, snapper.getZ() + (double) Mth.sin(f) * d2, yMin, yMax, f, j);
					}
				}
			}
			else if (windup && timer == 3) snapper.setAnimation(ANIM_SLAM);
			else if (!windup && timer == 15) snapper.setAnimation(ANIM_WINDDOWN);
			else if (!windup && timer == 10) snapper.setAnimation(ANIM_NEUTRAL);
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}
		
		//Copied from evoker
		private void createFang(double x, double z, double yMin, double yMax, float rot, int delay) {
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

		}

	}

}
