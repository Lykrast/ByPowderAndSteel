package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.entity.ZombunnySlasherEntity;
import lykrast.bypowderandsteel.misc.BPASUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class ZombunnySlasherModel extends HumanoidModel<ZombunnySlasherEntity> {	
	private float animProgress;
	
	public ZombunnySlasherModel(ModelPart modelpart) {
		super(modelpart);
	}
	
	@Override
	public void prepareMobModel(ZombunnySlasherEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
		//the super is empty
		animProgress = entity.getAnimProgress(partialTick);
		animProgress = BPASUtils.easeOutQuad(animProgress);
	}
	
	private float getTargetXRot(int anim, float neutral) {
		//take the neutral because idle is what the humanoid tries to set
		switch (anim) {
			default:
			case ZombunnySlasherEntity.ANIM_NEUTRAL:
			case ZombunnySlasherEntity.ANIM_LAND:
				return neutral;
			case ZombunnySlasherEntity.ANIM_WINDUP:
				return 70*Mth.DEG_TO_RAD;
			case ZombunnySlasherEntity.ANIM_JUMP:
				return -90*Mth.DEG_TO_RAD;
		}
	}
	
	private float getTargetZRot(int anim, float neutral, boolean leftArm) {
		//take the neutral because idle is what the humanoid tries to set
		switch (anim) {
			default:
			case ZombunnySlasherEntity.ANIM_NEUTRAL:
			case ZombunnySlasherEntity.ANIM_LAND:
				return neutral;
			case ZombunnySlasherEntity.ANIM_WINDUP:
				return (leftArm ? -40 : 40)*Mth.DEG_TO_RAD;
			case ZombunnySlasherEntity.ANIM_JUMP:
				return (leftArm ? -75 : 75)*Mth.DEG_TO_RAD;
		}
	}

	@Override
	public void setupAnim(ZombunnySlasherEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		int anim = entity.clientAnim;
		crouching = (anim != ZombunnySlasherEntity.ANIM_NEUTRAL);
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		if (animProgress > 0.999) {
			leftArm.xRot = getTargetXRot(anim, leftArm.xRot);
			leftArm.zRot = getTargetZRot(anim, leftArm.zRot, true);
			rightArm.xRot = getTargetXRot(anim, rightArm.xRot);
			rightArm.zRot = getTargetZRot(anim, rightArm.zRot, false);
		}
		else {
			int prev = entity.prevAnim;
			leftArm.xRot = BPASUtils.rotlerpRad(animProgress, getTargetXRot(prev, leftArm.xRot), getTargetXRot(anim, leftArm.xRot));
			leftArm.zRot = BPASUtils.rotlerpRad(animProgress, getTargetZRot(prev, leftArm.zRot, true), getTargetZRot(anim, leftArm.zRot, true));
			rightArm.xRot = BPASUtils.rotlerpRad(animProgress, getTargetXRot(prev, rightArm.xRot), getTargetXRot(anim, rightArm.xRot));
			rightArm.zRot = BPASUtils.rotlerpRad(animProgress, getTargetZRot(prev, rightArm.zRot, false), getTargetZRot(anim, rightArm.zRot, false));
		}
	}

}
