package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.entity.ZombunnyInquisitorEntity;
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

	//TODO real animations, for now just copying the gunner
	@Override
	public void setupAnim(ZombunnyInquisitorEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		crouching = entity.isCrouching();
		rightArmPose = HumanoidModel.ArmPose.EMPTY;
		leftArmPose = HumanoidModel.ArmPose.EMPTY;
		if (!entity.getMainHandItem().isEmpty() && entity.isAggressive()) {
			//swapped cause gun is in off hand
			if (entity.getMainArm() == HumanoidArm.LEFT) rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
			else leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
		}
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

}
