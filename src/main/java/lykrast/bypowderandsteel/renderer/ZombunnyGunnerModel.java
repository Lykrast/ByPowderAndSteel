package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.entity.ZombunnyGunnerEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;

public class ZombunnyGunnerModel extends HumanoidModel<ZombunnyGunnerEntity> {		
	public ZombunnyGunnerModel(ModelPart modelpart) {
		super(modelpart);
	}

	@Override
	public void setupAnim(ZombunnyGunnerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		crouching = entity.isCrouching();
		rightArmPose = HumanoidModel.ArmPose.EMPTY;
		leftArmPose = HumanoidModel.ArmPose.EMPTY;
		if (!entity.getMainHandItem().isEmpty() && entity.isAggressive()) {
			if (entity.getMainArm() == HumanoidArm.RIGHT) rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
			else leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
		}
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

}
