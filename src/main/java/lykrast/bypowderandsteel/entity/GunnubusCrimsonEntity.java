package lykrast.bypowderandsteel.entity;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ai.GunGoal;
import lykrast.bypowderandsteel.misc.BPASUtils;
import lykrast.bypowderandsteel.registry.BPASItems;
import lykrast.gunswithoutroses.item.BulletItem;
import lykrast.gunswithoutroses.registry.GWRAttributes;
import lykrast.gunswithoutroses.registry.GWRItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class GunnubusCrimsonEntity extends Monster implements GunMob {
	//TODO give them an actual behavior

	public GunnubusCrimsonEntity(EntityType<? extends GunnubusCrimsonEntity> type, Level world) {
		super(type, world);
		setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16);
		setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(4, new GunGoal<>(this, 1, 4, 16));
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
		return BPASUtils.baseGunMobAttributes().add(Attributes.MAX_HEALTH, 50).add(Attributes.ARMOR, 6).add(Attributes.MOVEMENT_SPEED, 0.19).add(GWRAttributes.dmgTotal.get(), 0.75).add(GWRAttributes.fireDelay.get(), 2);
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
		//2/3 of the time it's iron gun, rest it's either bloodfueled or blazing
		//want to keep the high damage ones rarer
		Item gun = GWRItems.ironGun.get();
		if (random.nextInt(3) == 0) gun = (random.nextBoolean() ? GWRItems.blazeGun : BPASItems.bloodfueledRevolver).get();
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(gun));
	}
	
	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/gunnubus_crimson");
	}

	//TODO sounds
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.WITCH_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.WITCH_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.WITCH_DEATH;
	}

}
