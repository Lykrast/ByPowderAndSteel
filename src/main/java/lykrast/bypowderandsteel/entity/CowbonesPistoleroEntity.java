package lykrast.bypowderandsteel.entity;

import java.util.EnumSet;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.misc.BPASUtils;
import lykrast.bypowderandsteel.registry.BPASItems;
import lykrast.gunswithoutroses.item.BulletItem;
import lykrast.gunswithoutroses.item.GunItem;
import lykrast.gunswithoutroses.registry.GWRAttributes;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CowbonesPistoleroEntity extends AbstractSkeleton implements GunMob {
	private static final EntityDataAccessor<Byte> ANIMATION = SynchedEntityData.defineId(CowbonesPistoleroEntity.class, EntityDataSerializers.BYTE);
	//I only need 4 states but that leaves me room for 8 juust in case
	private static final int MASK_LEFT = 0b111, MASK_RIGHT = 0b111000, MASK_FULL = 0b111111;
	public static final int ANIM_IDLE = 0, ANIM_AIMING = 1, ANIM_FIRED = 2, ANIM_TWIRLING = 3;
	public int clientAnimL, prevAnimL, animProgL, animDurL;
	public int clientAnimR, prevAnimR, animProgR, animDurR;
	public float spinAngleL, spinPrevL;
	public float spinAngleR, spinPrevR;

	public CowbonesPistoleroEntity(EntityType<? extends CowbonesPistoleroEntity> type, Level world) {
		super(type, world);
		clientAnimL = 0;
		prevAnimL = 0;
		animProgL = 1;
		animDurL = 1;
		clientAnimR = 0;
		prevAnimR = 0;
		animProgR = 1;
		animDurR = 1;
		spinAngleL = 0;
		spinPrevL = 0;
		spinAngleR = 0;
		spinPrevR = 0;
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(2, new RestrictSunGoal(this));
		goalSelector.addGoal(3, new FleeSunGoal(this, 1));
		goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 6, 1, 1.2));
		goalSelector.addGoal(4, new GunslingGoal(this, 1, 4, 15));
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
		return BPASItems.gunsteelBullet.get();
	}

	@Override
	public double getAddedSpread() {
		//same as skeletons, 10/6/2 for easy/normal/hard
		return 14 - level().getDifficulty().getId() * 4;
	}

	@Override
	public void reassessWeaponGoal() {
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
		//no super to not have armor
		//1/3 of the time have the revolver with different stats, otherwise have a default gun
		if (random.nextInt(3) == 0) {
			setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(BPASItems.desertRevolver.get()));
			setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(BPASItems.desertRevolver.get()));
		}
		else {
			setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(BPASUtils.randomDefaultGun(random)));
			setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(BPASUtils.randomDefaultGun(random)));
		}
	}

	@Override
	protected void populateDefaultEquipmentEnchantments(RandomSource random, DifficultyInstance difficulty) {
		super.populateDefaultEquipmentEnchantments(random, difficulty);
		float f = difficulty.getSpecialMultiplier();
		BPASUtils.enchantOffhand(this, random, f);
	}

	public static AttributeSupplier.Builder createAttributes() {
		//armor mimics having a leather chest and boots
		return BPASUtils.baseGunMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ARMOR, 4).add(GWRAttributes.dmgTotal.get(), 1/3f).add(GWRAttributes.fireDelay.get(), 5);
	}

	@Override
	public void tick() {
		super.tick();

		if (level().isClientSide()) {
			//arm animations
			int newanim = getAnimation();
			int newanimL = newanim & MASK_LEFT;
			int newanimR = (newanim & MASK_RIGHT) >> 3;
			if (clientAnimL != newanimL) {
				prevAnimL = clientAnimL;
				clientAnimL = newanimL;
				animProgL = 0;
				if (clientAnimL == ANIM_AIMING || clientAnimL == ANIM_FIRED) animDurL = 2;
				else animDurL = 5;
			}
			else if (animProgL < animDurL) animProgL++;
			if (clientAnimR != newanimR) {
				prevAnimR = clientAnimR;
				clientAnimR = newanimR;
				animProgR = 0;
				animDurR = 3;
				if (clientAnimR == ANIM_AIMING || clientAnimR == ANIM_FIRED) animDurR = 2;
				else animDurR = 5;
			}
			else if (animProgR < animDurR) animProgR++;
			//gun spinning
			spinPrevL = spinAngleL;
			if (clientAnimL == ANIM_TWIRLING || spinAngleL > 0) {
				if (spinAngleL <= 0) spinAngleL = 360;
				spinAngleL -= 72;
			}
			spinPrevR = spinAngleR;
			if (clientAnimL == ANIM_TWIRLING || spinAngleR > 0) {
				if (spinAngleR <= 0) spinAngleR = 360;
				spinAngleR -= 72;
			}
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(ANIMATION, (byte) 0);
	}

	protected int getAnimation() {
		return entityData.get(ANIMATION);
	}

	protected void setAnimation(int anim) {
		entityData.set(ANIMATION, (byte) anim);
	}

	public void setAnimationLeft(int left) {
		int right = getAnimation() & MASK_RIGHT;
		entityData.set(ANIMATION, (byte) ((left | right) & MASK_FULL));
	}

	public void setAnimationRight(int right) {
		int left = getAnimation() & MASK_LEFT;
		entityData.set(ANIMATION, (byte) ((left | (right << 3)) & MASK_FULL));
	}

	public float getAnimProgressLeft(float partial) {
		return Mth.clamp((animProgL + partial) / animDurL, 0, 1);
	}

	public float getAnimProgressRight(float partial) {
		return Mth.clamp((animProgR + partial) / animDurR, 0, 1);
	}

	public float getSpinAngleLeft(float partial) {
		return Mth.rotLerp(partial, spinPrevL, spinAngleL);
	}

	public float getSpinAngleRight(float partial) {
		return Mth.rotLerp(partial, spinPrevR, spinAngleR);
	}

	//TODO sounds
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.SKELETON_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.SKELETON_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.SKELETON_DEATH;
	}

	@Override
	protected SoundEvent getStepSound() {
		return SoundEvents.SKELETON_STEP;
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/cowbones_pistolero");
	}

	private static class GunslingGoal extends Goal {
		//Based on the GunGoal
		//TODO still essentially undodgeable and I don't like that
		protected final CowbonesPistoleroEntity mob;
		private final double speedModifier;
		private final double strafeRadiusSqr, attackRadiusSqr;
		private int attackTime = -1, reloadL = 0, reloadR = 0, twirlTimeL = 0, twirlTimeR = 0;
		private int seeTime;
		private boolean strafingClockwise;
		private boolean strafingBackwards;
		private int strafingTime = -1;

		public GunslingGoal(CowbonesPistoleroEntity mob, double speed, double strafeRange, double attackRange) {
			//-1 strafe to disable strafing, -1 range to disable the max range
			this.mob = mob;
			this.speedModifier = speed;
			this.strafeRadiusSqr = strafeRange * strafeRange;
			this.attackRadiusSqr = attackRange * attackRange;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return mob.getTarget() != null && isHoldingGun();
		}

		protected boolean isHoldingGun() {
			return mob.isHolding(is -> is.getItem() instanceof GunItem);
		}

		@Override
		public boolean canContinueToUse() {
			return (canUse() || !mob.getNavigation().isDone()) && isHoldingGun();
		}

		@Override
		public void start() {
			super.start();
			mob.setAggressive(true);
			mob.setAnimationLeft(ANIM_AIMING);
			mob.setAnimationRight(ANIM_AIMING);
		}

		@Override
		public void stop() {
			super.stop();
			mob.setAggressive(false);
			seeTime = 0;
			attackTime = -1;
			reloadL = 0;
			reloadR = 0;
			mob.setAnimationLeft(ANIM_IDLE);
			mob.setAnimationRight(ANIM_IDLE);
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			LivingEntity target = mob.getTarget();
			if (target != null) {
				double targetDistance = mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
				boolean los = mob.getSensing().hasLineOfSight(target);
				boolean hasSeen = seeTime > 0;

				//adjust seeing time
				if (los != hasSeen) seeTime = 0;
				if (los) ++seeTime;
				else--seeTime;

				//strafing
				if (targetDistance <= strafeRadiusSqr && seeTime >= 20) {
					mob.getNavigation().stop();
					++strafingTime;
				}
				else {
					mob.getNavigation().moveTo(target, speedModifier);
					strafingTime = -1;
				}

				//mixing strafing
				if (strafingTime >= 20) {
					if (mob.getRandom().nextFloat() < 0.3) strafingClockwise = !strafingClockwise;
					if (mob.getRandom().nextFloat() < 0.3) strafingBackwards = !strafingBackwards;
					strafingTime = 0;
				}
				
				//gun animations (will stop strafing)
				reloadL--;
				//TODO sound when aiming anim
				if (reloadL == 20) {
					mob.setAnimationLeft(ANIM_AIMING);
					mob.getMoveControl().strafe(0, 0);
				}
				else if (reloadL == twirlTimeL) mob.setAnimationLeft(ANIM_TWIRLING);
				reloadR--;
				if (reloadR == 20) {
					mob.setAnimationRight(ANIM_AIMING);
					mob.getMoveControl().strafe(0, 0);
				}
				else if (reloadR == twirlTimeR) mob.setAnimationRight(ANIM_TWIRLING);

				if (strafingTime > -1 && reloadL > 20 && reloadR > 20) {
					if (targetDistance > strafeRadiusSqr * 0.75) strafingBackwards = false;
					else if (targetDistance < strafeRadiusSqr * 0.25) strafingBackwards = true;

					mob.getMoveControl().strafe(strafingBackwards ? -0.5F : 0.5F, strafingClockwise ? 0.75F : -0.75F);
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

				//shoot				
				if (--attackTime <= 0 && seeTime >= 20 && (reloadL <= 0 || reloadR <= 0) && los && targetDistance <= attackRadiusSqr) {
					ItemStack gun = mob.getMainHandItem();
					boolean left = false;
					if (reloadR > 0 || !(gun.getItem() instanceof GunItem)) {
						gun = mob.getOffhandItem();
						left = true;
						if (!(gun.getItem() instanceof GunItem)) return; //we shouldn't arrive in this case, but juuust in case that should prevent a crash
					}
					GunItem gunItem = (GunItem) gun.getItem();
					ItemStack bullet = mob.getBulletStack();
					attackTime = 4;
					if (left) {
						reloadL = Math.max(gunItem.getFireDelay(gun, mob), 6);
						mob.setAnimationLeft(ANIM_FIRED);
						twirlTimeL = Math.max(20, reloadL - 6);
					}
					else {
						reloadR = Math.max(gunItem.getFireDelay(gun, mob), 6);
						mob.setAnimationRight(ANIM_FIRED);
						twirlTimeR = Math.max(20, reloadR - 6);
					}
					gunItem.shootAt(mob, target, gun, bullet, mob.getBullet(), mob.getAddedSpread(), true);
					mob.playSound(gunItem.getFireSound(), 1, 1.0F / (mob.getRandom().nextFloat() * 0.4F + 0.8F));
				}

			}
		}

	}

}
