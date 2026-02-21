package lykrast.bypowderandsteel.entity;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ai.HoverWanderGoal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class AbeillonMonarchEntity extends AbeillonAbstractEntity {
	public AbeillonMonarchEntity(EntityType<? extends AbeillonMonarchEntity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		//TODO more sophisticated attack
		goalSelector.addGoal(1, new SpiderAttackGoal(this, 1, true, 2));
		goalSelector.addGoal(2, new HoverWanderGoal(this));
		goalSelector.addGoal(3, new FloatGoal(this));
		goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new SpiderTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new SpiderTargetGoal<>(this, IronGolem.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 32).add(Attributes.MAX_HEALTH, 20).add(Attributes.ATTACK_DAMAGE, 4).add(Attributes.MOVEMENT_SPEED, 0.35)
				.add(Attributes.FLYING_SPEED, 0.8);
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ByPowderAndSteel.rl("entities/abeillon_monarch");
	}
}
