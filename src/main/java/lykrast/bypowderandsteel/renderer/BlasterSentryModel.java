package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.BlasterSentryEntity;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class BlasterSentryModel extends EntityModel<BlasterSentryEntity> implements ArmedModel {
	// Made with Blockbench 4.1.5 then adjusted later for animations and shit
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("blastersentry"), "main");
	private final ModelPart base, body, head, eye, arm;

	public BlasterSentryModel(ModelPart root) {
		base = root.getChild("base");
		body = root.getChild("body");
		head = root.getChild("head");
		arm = root.getChild("arm");
		eye = head.getChild("eye");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 46).addBox(-7.0F, -7.0F, -2.0F, 14.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, -1.5708F, 3.1416F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 14).addBox(-6.0F, -23.0F, -6.0F, 12.0F, 20.0F, 12.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 23.0F, 0.0F));

		body.addOrReplaceChild("headplates",
				CubeListBuilder.create().texOffs(40, 0).addBox(-5.0F, -5.0F, 2.0F, 10.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 0)
						.addBox(-5.0F, -5.0F, -1.0F, 10.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 12).addBox(-4.5F, -4.5F, -3.0F, 9.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -26.0F, 0.0F, -1.5708F, 3.1416F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		head.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-0.5F, -0.5F, -3.0F, 1.0F,
				1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, -5.0F));

		partdefinition.addOrReplaceChild("arm", CubeListBuilder.create().texOffs(4, 18).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.0F, 4.0F, -6.0F, -1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(BlasterSentryEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//Mostly adapted from HumanoidModel
		boolean falling = entity.getFallFlyingTicks() > 4;
		head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		if (falling) eye.xRot = -Mth.PI / 4;
		else eye.xRot = headPitch * Mth.DEG_TO_RAD;

		arm.xRot = eye.xRot - Mth.HALF_PI;
		arm.yRot = head.yRot;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		base.render(poseStack, buffer, packedLight, packedOverlay);
		body.render(poseStack, buffer, packedLight, packedOverlay);
		head.render(poseStack, buffer, packedLight, packedOverlay);
		arm.render(poseStack, buffer, packedLight, packedOverlay);
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
		this.arm.translateAndRotate(poseStack);
	}

}
