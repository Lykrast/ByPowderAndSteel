package lykrast.bypowderandsteel.entity;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.registry.BPASEffects;
import lykrast.bypowderandsteel.registry.BPASItems;
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

public class ZombunnySlasherEntity extends Monster {
	//TODO make the jump, animate the jump

	public ZombunnySlasherEntity(EntityType<? extends ZombunnySlasherEntity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(4, new MeleeAttackGoal(this, 1, true));
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.ARMOR, 2).add(Attributes.MOVEMENT_SPEED, 0.32);
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
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/zombunny_slasher");
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
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

	//TODO Sounds
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
}
