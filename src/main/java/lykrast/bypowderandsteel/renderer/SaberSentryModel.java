package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.SaberSentryEntity;
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

public class SaberSentryModel extends EntityModel<SaberSentryEntity> {
	// Made with Blockbench 4.1.5 then adjusted later for animations and shit
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("sabersentry"), "main");
	private final ModelPart lowerBody, upperBody, rightLeg, leftLeg, head, leftArm, leftForearm, rightArm, rightForearm, leftLowerLeg, rightLowerLeg;
	private float animProgress;

	public SaberSentryModel(ModelPart root) {
		this.lowerBody = root.getChild("Body");
		this.rightLeg = root.getChild("RightLeg");
		this.leftLeg = root.getChild("LeftLeg");
		upperBody = lowerBody.getChild("UpperBody");
		head = upperBody.getChild("Head");
		leftArm = upperBody.getChild("LeftArm");
		leftForearm = leftArm.getChild("LeftForearm");
		rightArm = upperBody.getChild("RightArm");
		rightForearm = rightArm.getChild("RightForearm");
		leftLowerLeg = leftLeg.getChild("LeftLowerLeg");
		rightLowerLeg = rightLeg.getChild("RightLowerLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 26).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition UpperBody = Body.addOrReplaceChild("UpperBody", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		UpperBody.addOrReplaceChild("breast_r1", CubeListBuilder.create().texOffs(16, 36).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.0F, -2.0F, -0.5236F, 0.0F, 0.0F));

		UpperBody.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition RightArm = UpperBody.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -10.0F, 0.0F));

		RightArm.addOrReplaceChild("RightForearm", CubeListBuilder.create().texOffs(40, 30).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(-0.1F))
		.texOffs(50, 42).addBox(-0.5F, 3.0F, -3.0F, 1.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 6.0F, 0.0F));

		PartDefinition LeftArm = UpperBody.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, -10.0F, 0.0F));

		LeftArm.addOrReplaceChild("LeftForearm", CubeListBuilder.create().texOffs(40, 30).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(-0.1F)).mirror(false)
		.texOffs(50, 42).mirror().addBox(-0.5F, 3.0F, -3.0F, 1.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 6.0F, 0.0F));

		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 8.0F, 0.0F));

		RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create().texOffs(0, 30).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.9F, 8.0F, 0.0F));

		LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create().texOffs(0, 30).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offset(0.0F, 8.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
	
	@Override
	public void prepareMobModel(SaberSentryEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
		//the super is empty
		animProgress = entity.getAnimProgress(partialTick);
		//if (entity.clientAnim == SaberSentryEntity.ANIM_SLAM) animProgress = BPASUtils.easeOutQuad(animProgress);
		//else animProgress = BPASUtils.easeInQuad(animProgress);
	}

	@Override
	public void setupAnim(SaberSentryEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//Mostly adapted from HumanoidModel
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

		rightArm.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 2.0F * limbSwingAmount * 0.5F / swingModifier;
		leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / swingModifier;
		rightArm.zRot = 0.0F;
		leftArm.zRot = 0.0F;
		rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.8F * limbSwingAmount / swingModifier; //original was 1.4f
		leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.8F * limbSwingAmount / swingModifier;
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
		
		//knees
		rightLowerLeg.xRot = Math.max(0, rightLeg.xRot*-1.5f);
		leftLowerLeg.xRot = Math.max(0, leftLeg.xRot*-1.5f);
		//curve legs inwards
		rightLeg.zRot = Math.max(Mth.abs(rightLeg.xRot)*-1/8f, -10*Mth.DEG_TO_RAD);
		leftLeg.zRot = Math.min(Mth.abs(rightLeg.xRot)*1/8f, 10*Mth.DEG_TO_RAD);
		
		
		//TODO animations
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		lowerBody.render(poseStack, buffer, packedLight, packedOverlay);
		rightLeg.render(poseStack, buffer, packedLight, packedOverlay);
		leftLeg.render(poseStack, buffer, packedLight, packedOverlay);
	}

}
