package lykrast.bypowderandsteel.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class AnimatedMonster extends Monster {
	private static final EntityDataAccessor<Byte> ANIMATION = SynchedEntityData.defineId(AnimatedMonster.class, EntityDataSerializers.BYTE);
	public int clientAnim, prevAnim, animProg, animDur;

	protected AnimatedMonster(EntityType<? extends Monster> type, Level world) {
		super(type, world);
		clientAnim = 0;
		prevAnim = 0;
		animProg = 1;
		animDur = 1;
	}

	@Override
	public void tick() {
		super.tick();

		if (level().isClientSide()) {
			int newanim = getAnimation();
			if (clientAnim != newanim) {
				prevAnim = clientAnim;
				clientAnim = newanim;
				animProg = 0;
				setupNewAnimation();
			}
			else if (animProg < animDur) animProg++;
		}
	}

	/**
	 * Set animDur to whatever is needed for the newly changed clientAnim
	 */
	protected abstract void setupNewAnimation();

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(ANIMATION, (byte) 0);
	}

	public int getAnimation() {
		return entityData.get(ANIMATION);
	}

	public void setAnimation(int anim) {
		entityData.set(ANIMATION, (byte) anim);
	}

	public float getAnimProgress(float partial) {
		return Mth.clamp((animProg + partial) / animDur, 0, 1);
	}

}
