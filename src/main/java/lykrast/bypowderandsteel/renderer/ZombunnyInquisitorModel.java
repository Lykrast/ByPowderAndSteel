package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.entity.ZombunnyInquisitorEntity;
import lykrast.bypowderandsteel.misc.BPASUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class ZombunnyInquisitorModel extends HumanoidModel<ZombunnyInquisitorEntity> {
	private float animProgress;
	
	public ZombunnyInquisitorModel(ModelPart modelpart) {
		super(modelpart);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = ZombunnyModel.createMesh();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.getChild("head");
		//blockbenched
		head.addOrReplaceChild("brim", CubeListBuilder.create().texOffs(30, 47).addBox(-8, -8, -6.2F, 16, 16, 1, CubeDeformation.NONE), PartPose.offsetAndRotation(0, 0, 0, -Mth.HALF_PI, 0, 0));
		
		return LayerDefinition.create(meshdefinition, 64, 64);
	}
	
	//TODO shooting animations, for now it's just slasher animations
	
	@Override
	public void prepareMobModel(ZombunnyInquisitorEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
		//the super is empty
		animProgress = entity.getAnimProgress(partialTick);
		animProgress = BPASUtils.easeOutQuad(animProgress);
	}
	
	private float getTargetXRot(int anim, float neutral, boolean leftArm, boolean righthanded, float headPitch) {
		//take the neutral because idle is what the humanoid tries to set
		switch (anim) {
			default:
			case ZombunnyInquisitorEntity.ANIM_NEUTRAL:
			case ZombunnyInquisitorEntity.ANIM_LAND:
				return neutral;
			case ZombunnyInquisitorEntity.ANIM_WINDUP:
				return 70*Mth.DEG_TO_RAD;
			case ZombunnyInquisitorEntity.ANIM_JUMP:
				return -90*Mth.DEG_TO_RAD;
			case ZombunnyInquisitorEntity.ANIM_AIM:
				if (leftArm == righthanded) {
					//gun in offhand
					return headPitch * Mth.DEG_TO_RAD - Mth.HALF_PI;
				}
				else return neutral;
			case ZombunnyInquisitorEntity.ANIM_SHOOTED:
				if (leftArm == righthanded) {
					//gun in offhand
					return headPitch * Mth.DEG_TO_RAD - (Mth.HALF_PI*1.25f);
				}
				else return neutral;
		}
	}
	
	private float getTargetYRot(int anim, float neutral, boolean leftArm, boolean righthanded, float netHeadYaw) {
		//take the neutral because idle is what the humanoid tries to set
		switch (anim) {
			default:
			case ZombunnyInquisitorEntity.ANIM_NEUTRAL:
				return neutral;
			case ZombunnyInquisitorEntity.ANIM_AIM:
			case ZombunnyInquisitorEntity.ANIM_SHOOTED:
				if (leftArm == righthanded) {
					//gun in offhand
					return netHeadYaw * Mth.DEG_TO_RAD;
				}
				else return neutral;
		}
	}
	
	private float getTargetZRot(int anim, float neutral, boolean leftArm, boolean righthanded) {
		//take the neutral because idle is what the humanoid tries to set
		switch (anim) {
			default:
			case ZombunnyInquisitorEntity.ANIM_NEUTRAL:
			case ZombunnyInquisitorEntity.ANIM_LAND:
				return neutral;
			case ZombunnyInquisitorEntity.ANIM_WINDUP:
				return (leftArm ? -40 : 40)*Mth.DEG_TO_RAD;
			case ZombunnyInquisitorEntity.ANIM_JUMP:
				return (leftArm ? -75 : 75)*Mth.DEG_TO_RAD;
		}
	}

	@Override
	public void setupAnim(ZombunnyInquisitorEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		int anim = entity.clientAnim;
		crouching = (anim >= ZombunnyInquisitorEntity.ANIM_WINDUP && anim <= ZombunnyInquisitorEntity.ANIM_LAND);
		boolean righthanded = entity.getMainArm() == HumanoidArm.RIGHT;
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		if (animProgress > 0.999) {
			leftArm.xRot = getTargetXRot(anim, leftArm.xRot, true, righthanded, headPitch);
			leftArm.yRot = getTargetYRot(anim, leftArm.yRot, true, righthanded, netHeadYaw);
			leftArm.zRot = getTargetZRot(anim, leftArm.zRot, true, righthanded);
			rightArm.xRot = getTargetXRot(anim, rightArm.xRot, false, righthanded, headPitch);
			rightArm.yRot = getTargetYRot(anim, rightArm.yRot, false, righthanded, netHeadYaw);
			rightArm.zRot = getTargetZRot(anim, rightArm.zRot, false, righthanded);
		}
		else {
			int prev = entity.prevAnim;
			leftArm.xRot = BPASUtils.rotlerpRad(animProgress, getTargetXRot(prev, leftArm.xRot, true, righthanded, headPitch), getTargetXRot(anim, leftArm.xRot, true, righthanded, headPitch));
			leftArm.yRot = BPASUtils.rotlerpRad(animProgress, getTargetYRot(prev, leftArm.yRot, true, righthanded, netHeadYaw), getTargetYRot(anim, leftArm.yRot, true, righthanded, netHeadYaw));
			leftArm.zRot = BPASUtils.rotlerpRad(animProgress, getTargetZRot(prev, leftArm.zRot, true, righthanded), getTargetZRot(anim, leftArm.zRot, true, righthanded));
			rightArm.xRot = BPASUtils.rotlerpRad(animProgress, getTargetXRot(prev, rightArm.xRot, false, righthanded, headPitch), getTargetXRot(anim, rightArm.xRot, false, righthanded, headPitch));
			rightArm.yRot = BPASUtils.rotlerpRad(animProgress, getTargetYRot(prev, rightArm.yRot, false, righthanded, netHeadYaw), getTargetYRot(anim, rightArm.yRot, false, righthanded, netHeadYaw));
			rightArm.zRot = BPASUtils.rotlerpRad(animProgress, getTargetZRot(prev, rightArm.zRot, false, righthanded), getTargetZRot(anim, rightArm.zRot, false, righthanded));
		}
	}

}
