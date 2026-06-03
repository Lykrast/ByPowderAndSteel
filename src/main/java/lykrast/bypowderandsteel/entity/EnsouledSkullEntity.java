package lykrast.bypowderandsteel.entity;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ai.HoverWanderGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

public class EnsouledSkullEntity extends AnimatedMonster {
	//animations
	public static final int ANIM_NEUTRAL = 0, ANIM_OPEN = 1, ANIM_STUN = 2, ANIM_RECOVERING = 3;
	//TODO stun/reanimate behavior
	//TODO animate as it is approaching

	public EnsouledSkullEntity(EntityType<? extends EnsouledSkullEntity> type, Level world) {
		super(type, world);
		//like bees
		moveControl = new FlyingMoveControl(this, 20, true);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, false));
		goalSelector.addGoal(2, new HoverWanderGoal(this));
		goalSelector.addGoal(3, new FloatGoal(this));
		goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	@Override
	protected void setupNewAnimation() {
		if (clientAnim == ANIM_OPEN || clientAnim == ANIM_STUN) animDur = 3;
		else animDur = 10;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 12).add(Attributes.ATTACK_DAMAGE, 8).add(Attributes.MOVEMENT_SPEED, 0.4).add(Attributes.FLYING_SPEED, 0.8)
				.add(Attributes.FOLLOW_RANGE, 20);
	}
	
	public void eject() {
		stopRiding();
		push(0, 0.4, 0);
	}
	
	public void eject(double dx, double dz) {
		stopRiding();
		Vec3 norm = new Vec3(dx,0,dz).normalize();
		push(norm.x*0.2, 0.4, norm.z*0.2);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		boolean ret = super.hurt(source, amount);
		//eject skull if we end up under half health
		Entity vehicle = getVehicle();
		if (vehicle != null && getHealth() <= getMaxHealth()/2.0 && vehicle instanceof HeadlessSkeletonEntity) {
			Entity damager = source.getEntity();
			if (damager != null) eject(getX() - damager.getX(), getZ() - damager.getZ());
			else eject();
		}
		return ret;
	}
	
//	@Override
//	public void die(DamageSource source) {
//		//TODO stun behavior
//		if (!isRemoved() && !dead && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
//			setHealth(getMaxHealth());
//			playSound(SoundEvents.LAVA_EXTINGUISH, getSoundVolume(), getVoicePitch());
//		}
//		else super.die(source);
//	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/ensouled_skull");
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
		//model is 8px, eye at 4px
		return dimensions.height * 0.5F;
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
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
		nav.setCanFloat(true);
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
	public void aiStep() {
		//particles like blazes
		if (level().isClientSide() && !isDeadOrDying()) {
			for (int i = 0; i < 2; ++i) {
				level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
			}
		}

		super.aiStep();
	}
}
