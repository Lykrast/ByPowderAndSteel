package lykrast.bypowderandsteel.entity;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.misc.BPASUtils;
import lykrast.bypowderandsteel.registry.BPASEffects;
import lykrast.bypowderandsteel.registry.BPASItems;
import lykrast.bypowderandsteel.registry.BPASSounds;
import lykrast.gunswithoutroses.item.BulletItem;
import lykrast.gunswithoutroses.item.GunItem;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ZombunnyInquisitorEntity extends AnimatedMonster implements GunMob {
	//animations
	public static final int ANIM_NEUTRAL = 0, ANIM_WINDUP = 1, ANIM_JUMP = 2, ANIM_LAND = 3, ANIM_AIM = 4, ANIM_SHOOTED = 5;
	
	public ZombunnyInquisitorEntity(EntityType<? extends ZombunnyInquisitorEntity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(4, new ChaseLeapShoot(this, 1, true));
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
		return BPASUtils.baseGunMobAttributes().add(Attributes.MAX_HEALTH, 80).add(Attributes.ARMOR, 10).add(Attributes.MOVEMENT_SPEED, 0.31).add(Attributes.KNOCKBACK_RESISTANCE, 1)
				.add(GWRAttributes.dmgTotal.get(), 0.5);
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
		//no super to not have armor
		//sword in main hand
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(BPASItems.inquisitorialMarksblade.get()));
		//gun in off hand
		setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(BPASItems.inquisitorialGun.get()));
	}

	@Override
	protected void populateDefaultEquipmentEnchantments(RandomSource random, DifficultyInstance difficulty) {
		super.populateDefaultEquipmentEnchantments(random, difficulty);
		float f = difficulty.getSpecialMultiplier();
		BPASUtils.enchantOffhand(this, random, f);
	}

	@Override
	protected void setupNewAnimation() {
		if (clientAnim == ANIM_WINDUP || clientAnim == ANIM_NEUTRAL || clientAnim == ANIM_AIM) animDur = 5;
		else animDur = 2;
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/zombunny_inquisitor");
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
	public boolean doHurtTarget(Entity target) {
		if (super.doHurtTarget(target)) {
			//TODO change if the marksblade changes
			//mob attacks don't call sword hurtEnemy aaaaaa
			//so cheating by putting the status effect here like cave spiders
			if (target instanceof LivingEntity ltarget) {
				ltarget.addEffect(new MobEffectInstance(BPASEffects.marked.get(), 5 * 20, 2), this);
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

	private static class ChaseLeapShoot extends MeleeAttackGoal {
		private ZombunnyInquisitorEntity buny;
		//0 approach, 1 windup, 2 leap, 3 recover, 4 feint leap, 5 shooting
		private int phase;
		//ticksUntilNextAttack is private so I'm making my own countdown
		private int time, feints, extraShots;

		public ChaseLeapShoot(ZombunnyInquisitorEntity buny, double speed, boolean seeThroughWalls) {
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
					if (target.hasEffect(BPASEffects.marked.get())) {
						//target is marked, try to shoot if los
						if (buny.getSensing().hasLineOfSight(target)) {
							phase = 5;
							time = 20;
							buny.setAnimation(ANIM_AIM);
							buny.getLookControl().setLookAt(target, 50, 0);
							extraShots = buny.random.nextInt(2) + buny.random.nextInt(2);
						}
					}
					else if (distanceSqr < 20) {
						//ready for the jump
						phase = 1;
						time = 10;
						buny.setAnimation(ANIM_WINDUP);
						buny.getLookControl().setLookAt(target, 50, 0);
						feints = buny.random.nextInt(2) + buny.random.nextInt(2); //0-2 feints, weighted towards 1
						//TODO windup sound
					}
					break;
				case 1:
					//bracing for jump
					if (distanceSqr > 49) {
						//target too far, disengage jump
						//but longer than slashers because larger jump
						phase = 0;
						buny.setAnimation(ANIM_NEUTRAL);
					}
					else if (time <= 0) {
						//leap!
						time = 10;
						//from LeapAtTargetGoal
						Vec3 vec3 = buny.getDeltaMovement();
						Vec3 vec31 = new Vec3(target.getX() - buny.getX(), 0, target.getZ() - buny.getZ());
						if (feints > 0) {
							feints--;
							phase = 4;
							if (vec31.lengthSqr() > 1.0E-7D) {
								vec31 = vec31.normalize().scale(1).add(vec3.scale(0.2)).yRot((buny.random.nextBoolean() ? -1 : 1)* Mth.HALF_PI * 0.5f);
							}
						}
						else {
							phase = 2;
							buny.setAnimation(ANIM_JUMP);
							if (vec31.lengthSqr() > 1.0E-7D) {
								//bigger leap than slashers because of lower base speed
								vec31 = vec31.normalize().scale(1.6).add(vec3.scale(0.2));
							}
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
					//recovery
					if (time <= 0) {
						phase = 0;
						//check if we'll chain another jump or shooting
						if (target.hasEffect(BPASEffects.marked.get()) || extraShots > 0) {
							if (buny.getSensing().hasLineOfSight(target)) {
								phase = 5;
								time = 15;
								buny.setAnimation(ANIM_AIM);
								buny.getLookControl().setLookAt(target, 50, 0);
								if (target.hasEffect(BPASEffects.marked.get())) {
									if (extraShots <= 0) extraShots = buny.random.nextInt(2) + buny.random.nextInt(2);
								}
								else extraShots--;
							}
							else {
								buny.setAnimation(ANIM_NEUTRAL);
								extraShots = 0;
							}
						}
						else if (distanceSqr < 20) {
							phase = 1;
							time = 10;
							buny.setAnimation(ANIM_WINDUP);
							buny.getLookControl().setLookAt(target, 50, 0);
							feints = buny.random.nextInt(2) + buny.random.nextInt(2); //0-2 feints, weighted towards 1
							extraShots = 0;
						}
						else {
							buny.setAnimation(ANIM_NEUTRAL);
							extraShots = 0;
						}
					}
					else buny.getNavigation().stop();
					break;
				case 4:
					//in the air but feinting
					if (time <= 0 || buny.onGround()) {
						//landed, switch to recovery
						phase = 1;
						time = 5;
						buny.getNavigation().stop();
						buny.getLookControl().setLookAt(target, 30, 30);
					}
					break;
				case 5:
					//aiming for a shot
					if (time <= 0) {
						//shoot! but only if los
						if (!buny.getSensing().hasLineOfSight(target)) {
							phase = 0;
							buny.setAnimation(ANIM_NEUTRAL);
						}
						else {
							ItemStack gun = buny.getOffhandItem();
							if (!gun.isEmpty() && gun.getItem() instanceof GunItem gunItem) {
								ItemStack bullet = buny.getBulletStack();
								gunItem.shootAt(buny, target, gun, bullet, buny.getBullet(), buny.getAddedSpread(), true);
								buny.playSound(gunItem.getFireSound(), 1, 1.0F / (buny.getRandom().nextFloat() * 0.4F + 0.8F));
							}
							//switch to recovery
							phase = 3;
							time = 5;
							buny.setAnimation(ANIM_SHOOTED);
							buny.getNavigation().stop();
							//cheating a bit on the gun fire delay
							//TODO don't do that?
						}
					}
					else {
						buny.getNavigation().stop();
						buny.getLookControl().setLookAt(target, 50, 0);
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
