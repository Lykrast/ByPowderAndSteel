package lykrast.bypowderandsteel.entity;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.registry.BPASSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public abstract class GunnubusAbstractEntity extends Monster implements GunMob {

	public GunnubusAbstractEntity(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16);
		setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1);
	}

	@Override
	public double getAddedSpread() {
		//same as skeletons, 10/6/2 for easy/normal/hard
		return 14 - level().getDifficulty().getId() * 4;
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
	protected SoundEvent getAmbientSound() {
		return BPASSounds.gunnubusIdle.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BPASSounds.gunnubusHurt.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BPASSounds.gunnubusDeath.get();
	}

}