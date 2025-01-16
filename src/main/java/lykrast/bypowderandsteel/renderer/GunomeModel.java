package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.GunomeEntity;
import net.minecraft.client.model.AnimationUtils;
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

public class GunomeModel extends EntityModel<GunomeEntity> implements ArmedModel {
	// Made with Blockbench 4.1.5 then adjusted later for animations and shit
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("gunome"), "main");
	private final ModelPart head, beard, body, rightArm, leftArm, rightLeg, leftLeg;

	public GunomeModel(ModelPart root) {
		head = root.getChild("Head");
		beard = head.getChild("beard");
		body = root.getChild("Body");
		rightArm = root.getChild("RightArm");
		leftArm = root.getChild("LeftArm");
		rightLeg = root.getChild("RightLeg");
		leftLeg = root.getChild("LeftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(40, 0)
						.addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.25F)).texOffs(24, 8).addBox(-1.0F, -4.0F, -4.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 10.0F, 0.0F));
		Head.addOrReplaceChild("beard", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.0F));
		partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(12, 12).addBox(-3.0F, 0.0F, -1.5F, 6.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(12, 23).addBox(-3.0F, 1.0F,
				1.0F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));
		partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(30, 12).addBox(-2.0F, -2.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-4.0F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(30, 12).mirror().addBox(-1.0F, -2.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(4.0F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 12).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-1.4F, 18.0F, 0.0F));
		partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 12).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(1.4F, 18.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(GunomeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//Mostly adapted from HumanoidModel
		boolean falling = entity.getFallFlyingTicks() > 4;
		head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		if (falling) head.xRot = -Mth.PI / 4;
		else head.xRot = headPitch * Mth.DEG_TO_RAD;
		beard.xRot = -head.xRot;

		float swingModifier = 1;
		if (falling) {
			swingModifier = (float) entity.getDeltaMovement().lengthSqr();
			swingModifier /= 0.2F;
			swingModifier *= swingModifier * swingModifier;
		}

		if (swingModifier < 1) swingModifier = 1;

		rightArm.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 2.0F * limbSwingAmount * 0.5F / swingModifier;
		leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / swingModifier;
		rightArm.zRot = 0.0F;
		leftArm.zRot = 0.0F;
		rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / swingModifier;
		leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount / swingModifier;
		rightLeg.yRot = 0.005F;
		leftLeg.yRot = -0.005F;
		rightLeg.zRot = 0.005F;
		leftLeg.zRot = -0.005F;
		if (riding) {
			rightArm.xRot += (-Mth.PI / 5F);
			leftArm.xRot += (-Mth.PI / 5F);
			rightLeg.xRot = -1.4137167F;
			rightLeg.yRot = (Mth.PI / 10F);
			rightLeg.zRot = 0.07853982F;
			leftLeg.xRot = -1.4137167F;
			leftLeg.yRot = (-Mth.PI / 10F);
			leftLeg.zRot = -0.07853982F;
		}
		
		AnimationUtils.bobModelPart(rightArm, ageInTicks, 1);
		AnimationUtils.bobModelPart(leftArm, ageInTicks, -1);

		//This part from skeleton
		//TODO better pose
		if (entity.isAggressive()) {
			float f = Mth.sin(this.attackTime * (float) Math.PI);
			float f1 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float) Math.PI);
			this.rightArm.zRot = 0.0F;
			this.leftArm.zRot = 0.0F;
			this.rightArm.yRot = -(0.1F - f * 0.6F);
			this.leftArm.yRot = 0.1F - f * 0.6F;
			this.rightArm.xRot = (-(float) Math.PI / 2F);
			this.leftArm.xRot = (-(float) Math.PI / 2F);
			this.rightArm.xRot -= f * 1.2F - f1 * 0.4F;
			this.leftArm.xRot -= f * 1.2F - f1 * 0.4F;
			AnimationUtils.bobArms(this.rightArm, this.leftArm, ageInTicks);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, buffer, packedLight, packedOverlay);
		body.render(poseStack, buffer, packedLight, packedOverlay);
		rightArm.render(poseStack, buffer, packedLight, packedOverlay);
		leftArm.render(poseStack, buffer, packedLight, packedOverlay);
		rightLeg.render(poseStack, buffer, packedLight, packedOverlay);
		leftLeg.render(poseStack, buffer, packedLight, packedOverlay);
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
		if (arm == HumanoidArm.LEFT) leftArm.translateAndRotate(poseStack);
		else rightArm.translateAndRotate(poseStack);
		poseStack.scale(0.75f, 0.75f, 0.75f);
	}

}
