package lykrast.bypowderandsteel.entity;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.registry.BPASSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
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

public class PatrollerEntity extends Monster {
	//TODO I'm not happy with their basic ass behavior but I don't have any ideas so it'll do for now

	public PatrollerEntity(EntityType<? extends PatrollerEntity> type, Level world) {
		super(type, world);
		setMaxUpStep(1.25F);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		//goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F)); //they leap sideways and it looks so bad
		goalSelector.addGoal(4, new MeleeAttackGoal(this, 1, true));
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8).add(Attributes.ARMOR, 20).add(Attributes.ATTACK_DAMAGE, 6).add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/patroller");
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
		//eye at 15px, model is 22px
		return dimensions.height * 0.68F;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return BPASSounds.patrollerIdle.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BPASSounds.sentryHurt.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BPASSounds.sentryDeath.get();
	}
}
