package lykrast.bypowderandsteel.entity;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ai.GunGoal;
import lykrast.bypowderandsteel.misc.BPASUtils;
import lykrast.bypowderandsteel.registry.BPASItems;
import lykrast.gunswithoutroses.item.BulletItem;
import lykrast.gunswithoutroses.registry.GWRAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GunnubusPaleEntity extends GunnubusAbstractEntity {
	//TODO probably need a better behavior
	
	public GunnubusPaleEntity(EntityType<? extends GunnubusPaleEntity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(4, new GunGoal<>(this, 1, 4, -1, 16));
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
		return BPASItems.soulsBreathBullet.get();
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(BPASUtils.randomDefaultGun(random)));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return BPASUtils.baseGunMobAttributes().add(Attributes.MAX_HEALTH, 50).add(Attributes.ARMOR, 6).add(Attributes.MOVEMENT_SPEED, 0.18).add(GWRAttributes.fireDelay.get(), 3.5);
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/gunnubus_pale");
	}

}
