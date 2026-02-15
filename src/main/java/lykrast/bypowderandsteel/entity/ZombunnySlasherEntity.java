package lykrast.bypowderandsteel.entity;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.registry.BPASEffects;
import lykrast.bypowderandsteel.registry.BPASItems;
import lykrast.bypowderandsteel.registry.BPASSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ZombunnySlasherEntity extends AnimatedMonster {
	//animations
	public static final int ANIM_NEUTRAL = 0, ANIM_WINDUP = 1, ANIM_JUMP = 2, ANIM_LAND = 3;

	public ZombunnySlasherEntity(EntityType<? extends ZombunnySlasherEntity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(4, new ChaseAndLeap(this, 1, true));
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.MOVEMENT_SPEED, 0.32);
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
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(BPASItems.ironMarksblade.get()));
	}

	@Override
	protected void setupNewAnimation() {
		if (clientAnim == ANIM_WINDUP || clientAnim == ANIM_NEUTRAL) animDur = 5;
		else animDur = 2;
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/zombunny_slasher");
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

	//TODO can't pitch down the vanilla sounds enough :(
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

	private static class ChaseAndLeap extends MeleeAttackGoal {
		private ZombunnySlasherEntity buny;
		//0 approach, 1 windup, 2 leap, 3 recover
		private int phase;
		//ticksUntilNextAttack is private so I'm making my own countdown
		private int time;

		public ChaseAndLeap(ZombunnySlasherEntity buny, double speed, boolean seeThroughWalls) {
			super(buny, speed, seeThroughWalls);
			this.buny = buny;
		}

		@Override
		public void start() {
			super.start();
			phase = 0;
			time = 0;
		}

		@Override
		public void stop() {
			super.stop();
			buny.setAnimation(ANIM_NEUTRAL);
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
					if (distanceSqr < 20) {
						//ready for the jump
						phase = 1;
						time = 10;
						buny.setAnimation(ANIM_WINDUP);
						buny.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ(), 50, 0);
						//TODO windup sound
					}
					break;
				case 1:
					//bracing for jump
					if (distanceSqr > 36) {
						//target too far, disengage jump
						phase = 0;
						buny.setAnimation(ANIM_NEUTRAL);
					}
					else if (time <= 0) {
						//leap!
						time = 10;
						phase = 2;
						buny.setAnimation(ANIM_JUMP);
						//from LeapAtTargetGoal
						Vec3 vec3 = buny.getDeltaMovement();
						Vec3 vec31 = new Vec3(target.getX() - buny.getX(), 0, target.getZ() - buny.getZ());
						if (vec31.lengthSqr() > 1.0E-7D) {
							vec31 = vec31.normalize().scale(1.3).add(vec3.scale(0.2));
						}

						buny.setDeltaMovement(vec31.x, Math.max(0, target.getY() - buny.getY()) * 0.2 + 0.4, vec31.z);
						//TODO dedicated jump sound
						mob.playSound(SoundEvents.RABBIT_JUMP, 1, 1);
					}
					else {
						buny.getNavigation().stop();
						//buny.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
					}
					break;
				case 2:
					//in the air
					if (time <= 0 || buny.onGround()) {
						//landed, switch to recovery
						phase = 3;
						time = 10;
						buny.setAnimation(ANIM_LAND);
						buny.getNavigation().stop();
					}
					else {
						super.checkAndPerformAttack(target, distanceSqr);
					}
					break;
				case 3:
					//jump recovery
					if (time <= 0) {
						phase = 0;
						//check if we'll chain another jump
						if (distanceSqr < 20) {
							phase = 1;
							time = 10;
							buny.setAnimation(ANIM_WINDUP);
							buny.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ(), 50, 0);
						}
						else buny.setAnimation(ANIM_NEUTRAL);
					}
					else buny.getNavigation().stop();
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
