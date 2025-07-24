package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import lykrast.bypowderandsteel.entity.CowbonesPistoleroEntity;
import lykrast.bypowderandsteel.misc.BPASUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class CowbonesPistoleroModel extends HumanoidModel<CowbonesPistoleroEntity> {
	public float animProgressL, animProgressR, gunSpinL, gunSpinR;

	public CowbonesPistoleroModel(ModelPart modelpart) {
		super(modelpart);
	}

	@Override
	public void prepareMobModel(CowbonesPistoleroEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
		super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
		
		animProgressL = entity.getAnimProgressLeft(partialTick);
		if (entity.clientAnimL == CowbonesPistoleroEntity.ANIM_AIMING || entity.clientAnimL == CowbonesPistoleroEntity.ANIM_FIRED) animProgressL = BPASUtils.easeOutQuad(animProgressL);
		else animProgressL = BPASUtils.easeInQuad(animProgressL);
		
		animProgressR = entity.getAnimProgressRight(partialTick);
		if (entity.clientAnimR == CowbonesPistoleroEntity.ANIM_AIMING || entity.clientAnimR == CowbonesPistoleroEntity.ANIM_FIRED) animProgressR = BPASUtils.easeOutQuad(animProgressR);
		else animProgressR = BPASUtils.easeInQuad(animProgressR);
		
		gunSpinL = entity.getSpinAngleLeft(partialTick);
		gunSpinR = entity.getSpinAngleRight(partialTick);
	}

	@Override
	public void setupAnim(CowbonesPistoleroEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		if (animProgressL > 0.99) {
			leftArm.xRot = getTargetXRot(entity.clientAnimL, leftArm.xRot, headPitch);
			leftArm.yRot = getTargetYRot(entity.clientAnimL, leftArm.yRot, netHeadYaw);
		}
		else {
			leftArm.xRot = BPASUtils.rotlerpRad(animProgressL, getTargetXRot(entity.prevAnimL, leftArm.xRot, headPitch), getTargetXRot(entity.clientAnimL, leftArm.xRot, headPitch));
			leftArm.yRot = BPASUtils.rotlerpRad(animProgressL, getTargetYRot(entity.prevAnimL, leftArm.yRot, netHeadYaw), getTargetYRot(entity.clientAnimL, leftArm.yRot, netHeadYaw));
		}
		if (animProgressR > 0.99) {
			rightArm.xRot = getTargetXRot(entity.clientAnimR, rightArm.xRot, headPitch);
			rightArm.yRot = getTargetYRot(entity.clientAnimR, rightArm.yRot, netHeadYaw);
		}
		else {
			rightArm.xRot = BPASUtils.rotlerpRad(animProgressR, getTargetXRot(entity.prevAnimR, rightArm.xRot, headPitch), getTargetXRot(entity.clientAnimR, rightArm.xRot, headPitch));
			rightArm.yRot = BPASUtils.rotlerpRad(animProgressR, getTargetYRot(entity.prevAnimR, rightArm.yRot, netHeadYaw), getTargetYRot(entity.clientAnimR, rightArm.yRot, netHeadYaw));
		}
	}
	
	private float getTargetXRot(int anim, float neutral, float headPitch) {
		//take the neutral because idle is what the humanoid tries to set
		switch (anim) {
			default:
			case CowbonesPistoleroEntity.ANIM_IDLE:
				return neutral;
			case CowbonesPistoleroEntity.ANIM_AIMING:
				return -Mth.HALF_PI + headPitch*Mth.DEG_TO_RAD;
			case CowbonesPistoleroEntity.ANIM_FIRED:
				return -150*Mth.DEG_TO_RAD;
			case CowbonesPistoleroEntity.ANIM_TWIRLING:
				return -40*Mth.DEG_TO_RAD;
		}
	}
	
	private float getTargetYRot(int anim, float neutral, float headYaw) {
		switch (anim) {
			default:
				return neutral;
			case CowbonesPistoleroEntity.ANIM_AIMING:
				return headYaw*Mth.DEG_TO_RAD;
		}
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack posestack) {
		//this is the only part of SkeletonModel I care about, I don't want the rest of the supers
		float offset = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
		ModelPart modelpart = getArm(arm);
		modelpart.x += offset;
		modelpart.translateAndRotate(posestack);
		modelpart.x -= offset;
	}

}
