package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.SkybenderEntity;
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

public class SkybenderModel extends EntityModel<SkybenderEntity> implements ArmedModel {
	// Made with Blockbench 4.1.5 then adjusted later for animations and shit
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("skybender"), "main");
	private final ModelPart seat, head, armRight, armLeft, forearmRight, forearmLeft;

	public SkybenderModel(ModelPart root) {
		seat = root.getChild("seat");
		head = root.getChild("head");
		armRight = root.getChild("armRight");
		armLeft = root.getChild("armLeft");
		forearmRight = armRight.getChild("forearmRight");
		forearmLeft = armLeft.getChild("forearmLeft");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(0, 16).addBox(-6.0F, -8.0F, -6.0F, 12.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-6.0F, -18.0F, 4.0F, 12.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 34).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition armRight = partdefinition.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(0, 34).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 16.0F, 0.0F));
		armRight.addOrReplaceChild("forearmRight", CubeListBuilder.create().texOffs(16, 34).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(-2.0F, 9.0F, 0.0F));

		PartDefinition armLeft = partdefinition.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(0.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.0F, 16.0F, 0.0F));
		armLeft.addOrReplaceChild("forearmLeft", CubeListBuilder.create().texOffs(16, 34).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(2.0F, 9.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(SkybenderEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//Mostly adapted from HumanoidModel
		//TODO animations
		boolean falling = entity.getFallFlyingTicks() > 4;
		head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		if (falling) head.xRot = -Mth.PI / 4;
		else head.xRot = headPitch * Mth.DEG_TO_RAD;

		float swingModifier = 1;
		if (falling) {
			swingModifier = (float) entity.getDeltaMovement().lengthSqr();
			swingModifier /= 0.2F;
			swingModifier *= swingModifier * swingModifier;
		}

		if (swingModifier < 1) swingModifier = 1;

		armRight.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.5F * limbSwingAmount * 0.5F / swingModifier;
		armLeft.xRot = Mth.cos(limbSwing * 0.6662F) * 1.5F * limbSwingAmount * 0.5F / swingModifier;
		armRight.zRot = 0.0F;
		armLeft.zRot = 0.0F;
		forearmRight.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI*1.1f) * 0.5F * limbSwingAmount * 0.5F / swingModifier;
		forearmLeft.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI*0.1f) * 0.5F * limbSwingAmount * 0.5F / swingModifier;
		forearmRight.zRot = 0.0F;
		forearmLeft.zRot = 0.0F;
		if (riding) {
			armRight.xRot += (-Mth.PI / 5F);
			armLeft.xRot += (-Mth.PI / 5F);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		seat.render(poseStack, buffer, packedLight, packedOverlay);
		head.render(poseStack, buffer, packedLight, packedOverlay);
		armRight.render(poseStack, buffer, packedLight, packedOverlay);
		armLeft.render(poseStack, buffer, packedLight, packedOverlay);
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
		if (arm == HumanoidArm.LEFT) {
			armLeft.translateAndRotate(poseStack);
			forearmLeft.translateAndRotate(poseStack);
		}
		else {
			armRight.translateAndRotate(poseStack);
			forearmRight.translateAndRotate(poseStack);
		}
	}

}
