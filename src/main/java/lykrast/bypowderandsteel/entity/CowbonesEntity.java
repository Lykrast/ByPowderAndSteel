package lykrast.bypowderandsteel.entity;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ai.GunGoal;
import lykrast.bypowderandsteel.misc.BPASUtils;
import lykrast.bypowderandsteel.registry.BPASItems;
import lykrast.gunswithoutroses.item.BulletItem;
import lykrast.gunswithoutroses.registry.GWRAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CowbonesEntity extends AbstractSkeleton implements GunMob {
	public CowbonesEntity(EntityType<? extends CowbonesEntity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(2, new RestrictSunGoal(this));
		goalSelector.addGoal(3, new FleeSunGoal(this, 1));
		goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 6, 1, 1.2));
		goalSelector.addGoal(4, new GunGoal<>(this, 1, 4, 15));
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
		Item gun = BPASItems.desertRevolver.get();
		//1/3 of the time have the revolver with different stats, otherwise have a default gun
		if (random.nextInt(3) < 2) gun = BPASUtils.randomDefaultGun(random);
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(gun));
	}

	public static AttributeSupplier.Builder createAttributes() {
		//armor mimics having a leather chest and boots
		return BPASUtils.baseGunMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ARMOR, 4).add(GWRAttributes.dmgBase.get(), -3).add(GWRAttributes.fireDelay.get(), 2);
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
		return ByPowderAndSteel.rl("entities/cowbones");
	}

}
