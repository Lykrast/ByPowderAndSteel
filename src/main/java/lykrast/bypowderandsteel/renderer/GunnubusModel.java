package lykrast.bypowderandsteel.renderer;

import net.minecraft.client.model.AnimationUtils;
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
import net.minecraft.world.entity.Mob;

public class GunnubusModel<T extends Mob> extends HumanoidModel<T> {
	public GunnubusModel(ModelPart modelpart) {
		super(modelpart);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition body = partdefinition.getChild("body");

		//Blockbenched breasts
		body.addOrReplaceChild("breast", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 5.0F, 3.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, -0.5236F, 0.0F, 0.0F));
		
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		if (entity.isAggressive()) {
			//really wish there was a "get item in left hand" function
			//I mean they're not even supposed to dual wield but y'knoooow just in case
			if (!entity.getMainHandItem().isEmpty()) {
				if (entity.getMainArm() == HumanoidArm.LEFT) {
					leftArm.xRot = headPitch * Mth.DEG_TO_RAD - Mth.HALF_PI;
					leftArm.yRot = netHeadYaw * Mth.DEG_TO_RAD;		
					AnimationUtils.bobModelPart(leftArm, ageInTicks, -1);
				}
				else {
					rightArm.xRot = headPitch * Mth.DEG_TO_RAD - Mth.HALF_PI;
					rightArm.yRot = netHeadYaw * Mth.DEG_TO_RAD;
					AnimationUtils.bobModelPart(rightArm, ageInTicks, 1);
				}
			}
			if (!entity.getOffhandItem().isEmpty()) {
				if (entity.getMainArm() == HumanoidArm.RIGHT) {
					leftArm.xRot = headPitch * Mth.DEG_TO_RAD - Mth.HALF_PI;
					leftArm.yRot = netHeadYaw * Mth.DEG_TO_RAD;
					AnimationUtils.bobModelPart(leftArm, ageInTicks, -1);
				}
				else {
					rightArm.xRot = headPitch * Mth.DEG_TO_RAD - Mth.HALF_PI;
					rightArm.yRot = netHeadYaw * Mth.DEG_TO_RAD;
					AnimationUtils.bobModelPart(rightArm, ageInTicks, 1);
				}
			}
		}
	}

}
