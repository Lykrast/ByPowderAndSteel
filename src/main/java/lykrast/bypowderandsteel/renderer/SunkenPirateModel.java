package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.entity.SunkenPirateEntity;
import lykrast.bypowderandsteel.misc.BPASUtils;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class SunkenPirateModel extends HumanoidModel<SunkenPirateEntity> {
	private float animProgress;
	
	public SunkenPirateModel(ModelPart modelpart) {
		super(modelpart);
	}
	
	@Override
	public void prepareMobModel(SunkenPirateEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
		super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
		animProgress = entity.getAnimProgress(partialTick);
		if (entity.clientAnim == SunkenPirateEntity.ANIM_FIRE) animProgress = BPASUtils.easeOutQuad(animProgress);
		else animProgress = BPASUtils.easeInQuad(animProgress);
	}

	@Override
	public void setupAnim(SunkenPirateEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		//Swimming from Drowned
		if (swimAmount > 0.0F) {
			AnimationUtils.animateZombieArms(leftArm, rightArm, entity.isAggressive(), attackTime, ageInTicks);
			rightArm.xRot = rotlerpRad(swimAmount, rightArm.xRot, -2.5132742F) + swimAmount * 0.35F * Mth.sin(0.1F * ageInTicks);
			leftArm.xRot = rotlerpRad(swimAmount, leftArm.xRot, -2.5132742F) - swimAmount * 0.35F * Mth.sin(0.1F * ageInTicks);
			rightArm.zRot = rotlerpRad(swimAmount, rightArm.zRot, -0.15F);
			leftArm.zRot = rotlerpRad(swimAmount, leftArm.zRot, 0.15F);
			leftLeg.xRot -= swimAmount * 0.55F * Mth.sin(0.1F * ageInTicks);
			rightLeg.xRot += swimAmount * 0.55F * Mth.sin(0.1F * ageInTicks);
			head.xRot = 0;
		}
		
		//sword is main hand, gun is offhand
		ModelPart swordArm = entity.isLeftHanded() ? leftArm : rightArm;
		ModelPart gunArm = entity.isLeftHanded() ? rightArm : leftArm;
		
		//only animation that uses the sword arm
		if (entity.clientAnim == SunkenPirateEntity.ANIM_CHARGE) {
			if (swimAmount > 0) swordArm.xRot += Mth.HALF_PI*animProgress;
			else if (animProgress > 0.99) swordArm.xRot = -Mth.HALF_PI;
			else swordArm.xRot = BPASUtils.rotlerpRad(animProgress, swordArm.xRot, -Mth.HALF_PI);
		}
		else if (entity.prevAnim == SunkenPirateEntity.ANIM_CHARGE) {
			if (swimAmount > 0) swordArm.xRot += Mth.HALF_PI*(1-animProgress);
			else if (animProgress <= 0.99) swordArm.xRot = BPASUtils.rotlerpRad(animProgress, -Mth.HALF_PI, swordArm.xRot);
		}
		
		if (animProgress > 0.99) {
			gunArm.xRot = getGunTargetXRot(entity.clientAnim, gunArm.xRot, headPitch);
			gunArm.yRot = getGunTargetYRot(entity.clientAnim, gunArm.yRot, netHeadYaw);
		}
		else {
			gunArm.xRot = BPASUtils.rotlerpRad(animProgress, getGunTargetXRot(entity.prevAnim, gunArm.xRot, headPitch), getGunTargetXRot(entity.clientAnim, gunArm.xRot, headPitch));
			gunArm.yRot = BPASUtils.rotlerpRad(animProgress, getGunTargetYRot(entity.prevAnim, gunArm.yRot, netHeadYaw), getGunTargetYRot(entity.clientAnim, gunArm.yRot, netHeadYaw));
		}
	}
	
	private float getGunTargetXRot(int anim, float neutral, float headPitch) {
		//take the neutral because idle is what the humanoid tries to set
		switch (anim) {
			default:
				return neutral;
			case SunkenPirateEntity.ANIM_AIM:
				return -Mth.HALF_PI + headPitch*Mth.DEG_TO_RAD;
			case SunkenPirateEntity.ANIM_FIRE:
				return -150*Mth.DEG_TO_RAD;
		}
	}
	
	private float getGunTargetYRot(int anim, float neutral, float headYaw) {
		switch (anim) {
			default:
				return neutral;
			case SunkenPirateEntity.ANIM_AIM:
				return headYaw*Mth.DEG_TO_RAD;
		}
	}

}
