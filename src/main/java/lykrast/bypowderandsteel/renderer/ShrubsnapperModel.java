package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ShrubsnapperEntity;
import lykrast.bypowderandsteel.misc.BPASUtils;
import net.minecraft.client.model.AnimationUtils;
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

public class ShrubsnapperModel extends EntityModel<ShrubsnapperEntity> {
	// Made with Blockbench 4.1.5 then adjusted later for animations and shit
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("shrubsnapper"), "main");
	private final ModelPart body, head, rightArm, rightForearm, rightTrunk, leftArm, leftForearm, leftTrunk, rightLeg, leftLeg;
	private float animProgress;

	//from blockbench
	private static final float BODY_BASE_X_ROT = 0.1745F, HEAD_BASE_X_ROT = 0.3491F, ARM_BASE_X_ROT = -0.1745F;

	public ShrubsnapperModel(ModelPart root) {
		body = root.getChild("body");
		rightLeg = root.getChild("legRight");
		leftLeg = root.getChild("legLeft");
		head = body.getChild("head");
		rightArm = body.getChild("armRight");
		leftArm = body.getChild("armLeft");
		rightForearm = rightArm.getChild("forearmRight");
		leftForearm = leftArm.getChild("forearmLeft");
		rightTrunk = rightForearm.getChild("trunkRight");
		leftTrunk = leftForearm.getChild("trunkLeft");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 9.0F, 1.0F, BODY_BASE_X_ROT, 0.0F, 0.0F));
		PartDefinition armRight = body.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(0, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)),
				PartPose.offsetAndRotation(-5.0F, -10.0F, 0.0F, ARM_BASE_X_ROT, 0.0F, 0.0F));
		PartDefinition forearmRight = armRight.addOrReplaceChild("forearmRight", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)),
				PartPose.offset(-1.0F, 9.0F, 0.0F));
		forearmRight.addOrReplaceChild("trunkRight", CubeListBuilder.create().texOffs(16, 52).addBox(-8.0F, 0.0F, -1.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(6.0F, 4.0F, -1.0F));
		PartDefinition armLeft = body.addOrReplaceChild("armLeft",
				CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)).mirror(false),
				PartPose.offsetAndRotation(5.0F, -10.0F, 0.0F, ARM_BASE_X_ROT, 0.0F, 0.0F));
		PartDefinition forearmLeft = armLeft.addOrReplaceChild("forearmLeft",
				CubeListBuilder.create().texOffs(0, 48).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(1.0F, 9.0F, 0.0F));
		forearmLeft.addOrReplaceChild("trunkLeft", CubeListBuilder.create().texOffs(16, 52).mirror().addBox(-8.0F, 0.0F, -1.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(6.0F, 4.0F, -1.0F));
		body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, HEAD_BASE_X_ROT, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("legRight", CubeListBuilder.create().texOffs(16, 32).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-2.0F, 8.0F, 1.0F));
		partdefinition.addOrReplaceChild("legLeft", CubeListBuilder.create().texOffs(16, 32).mirror().addBox(-2.1F, 0.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(2.0F, 8.0F, 1.0F));

		return LayerDefinition.create(meshdefinition, 32, 64);
	}

	@Override
	public void prepareMobModel(ShrubsnapperEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
		//the super is empty
		animProgress = entity.getAnimProgress(partialTick);
		if (entity.clientAnim == ShrubsnapperEntity.ANIM_SLAM || entity.clientAnim == ShrubsnapperEntity.ANIM_WINDDOWN) animProgress = BPASUtils.easeOutQuad(animProgress);
		else animProgress = BPASUtils.easeInQuad(animProgress);
	}

	@Override
	public void setupAnim(ShrubsnapperEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//Mostly adapted from HumanoidModel
		boolean falling = entity.getFallFlyingTicks() > 4;
		head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		if (falling) head.xRot = -Mth.PI / 4 + HEAD_BASE_X_ROT;
		else head.xRot = headPitch * Mth.DEG_TO_RAD + HEAD_BASE_X_ROT;

		float swingModifier = 1;
		if (falling) {
			swingModifier = (float) entity.getDeltaMovement().lengthSqr();
			swingModifier /= 0.2F;
			swingModifier *= swingModifier * swingModifier;
		}

		if (swingModifier < 1) swingModifier = 1;

		rightArm.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 2.0F * limbSwingAmount * 0.5F / swingModifier + ARM_BASE_X_ROT;
		leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / swingModifier + ARM_BASE_X_ROT;
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

		AnimationUtils.bobModelPart(rightArm, ageInTicks, 0.6f);
		AnimationUtils.bobModelPart(leftArm, ageInTicks, -0.6f);
		rightForearm.xRot = 0;
		rightForearm.zRot = 0;
		leftForearm.xRot = 0;
		leftForearm.zRot = 0;
		AnimationUtils.bobModelPart(rightForearm, ageInTicks + 20, 0.4f);
		AnimationUtils.bobModelPart(leftForearm, ageInTicks + 20, -0.4f);
		
		body.xRot = BODY_BASE_X_ROT;
		rightArm.yRot = 0;
		leftArm.yRot = 0;
		rightTrunk.y = 4;
		leftTrunk.y = 4;

		//animate now that we have the swung arm to return to neutral
		//because a slam is not supposed to be interrupted by another anim, just ease neutral -> windup -> slam -> winddown -> neutral
		if (entity.clientAnim == ShrubsnapperEntity.ANIM_WINDUP) {
			//reminder: blockbench angles for X and Y are negated for some reason, sometimes, seems inconsistent with the cube
			body.xRot = Mth.lerp(animProgress, body.xRot, 5*Mth.DEG_TO_RAD);
			head.xRot = Mth.lerp(animProgress, head.xRot, HEAD_BASE_X_ROT);
			head.yRot = head.yRot*(1-animProgress);
			rightArm.xRot = rightArm.xRot*(1-animProgress);
			rightArm.yRot = Mth.lerp(animProgress, rightArm.yRot, -45*Mth.DEG_TO_RAD);
			rightArm.zRot = Mth.lerp(animProgress, rightArm.zRot, 150*Mth.DEG_TO_RAD);
			leftArm.xRot = leftArm.xRot*(1-animProgress);
			leftArm.yRot = Mth.lerp(animProgress, leftArm.yRot, 45*Mth.DEG_TO_RAD);
			leftArm.zRot = Mth.lerp(animProgress, leftArm.zRot, -150*Mth.DEG_TO_RAD);
			rightForearm.xRot = Mth.lerp(animProgress, rightForearm.xRot, -115*Mth.DEG_TO_RAD);
			rightForearm.zRot = rightForearm.zRot*(1-animProgress);
			leftForearm.xRot = Mth.lerp(animProgress, leftForearm.xRot, -115*Mth.DEG_TO_RAD);
			leftForearm.zRot = leftForearm.zRot*(1-animProgress);
			rightTrunk.y = Mth.lerp(animProgress, rightTrunk.y, 5);
			leftTrunk.y = Mth.lerp(animProgress, leftTrunk.y, 5);
		}
		else if (entity.clientAnim == ShrubsnapperEntity.ANIM_SLAM) {
			//TODO one of the arm transitions wonkily
			body.xRot = Mth.lerp(animProgress, 5*Mth.DEG_TO_RAD, 60*Mth.DEG_TO_RAD);
			head.xRot = HEAD_BASE_X_ROT;
			head.yRot = 0;
			rightArm.xRot = -60*Mth.DEG_TO_RAD*animProgress;
			rightArm.yRot = Mth.lerp(animProgress, -45*Mth.DEG_TO_RAD, 10*Mth.DEG_TO_RAD);
			rightArm.zRot = 150*Mth.DEG_TO_RAD*(1-animProgress);
			leftArm.xRot = -60*Mth.DEG_TO_RAD*animProgress;
			leftArm.yRot = Mth.lerp(animProgress, 45*Mth.DEG_TO_RAD, -10*Mth.DEG_TO_RAD);
			leftArm.zRot = -150*Mth.DEG_TO_RAD*(1-animProgress);
			rightForearm.xRot = Mth.lerp(animProgress, -115*Mth.DEG_TO_RAD, -5*Mth.DEG_TO_RAD);
			rightForearm.zRot = -10*Mth.DEG_TO_RAD*animProgress;
			leftForearm.xRot = Mth.lerp(animProgress, -115*Mth.DEG_TO_RAD, -5*Mth.DEG_TO_RAD);
			leftForearm.zRot = 10*Mth.DEG_TO_RAD*animProgress;
			rightTrunk.y = Mth.lerp(animProgress, 5, 8);
			leftTrunk.y = Mth.lerp(animProgress, 5, 8);
		}
		else if (entity.clientAnim == ShrubsnapperEntity.ANIM_WINDDOWN) {
			body.xRot = Mth.lerp(animProgress, 60*Mth.DEG_TO_RAD, 40*Mth.DEG_TO_RAD);
			head.xRot = HEAD_BASE_X_ROT;
			head.yRot = 0;
			rightArm.xRot = -60*Mth.DEG_TO_RAD*(1-animProgress);
			rightArm.yRot = Mth.lerp(animProgress, 10*Mth.DEG_TO_RAD, -30*Mth.DEG_TO_RAD);
			rightArm.zRot = 70*Mth.DEG_TO_RAD*animProgress;
			leftArm.xRot = -60*Mth.DEG_TO_RAD*(1-animProgress);
			leftArm.yRot = Mth.lerp(animProgress, -10*Mth.DEG_TO_RAD, 30*Mth.DEG_TO_RAD);
			leftArm.zRot = -70*Mth.DEG_TO_RAD*animProgress;
			rightForearm.xRot = Mth.lerp(animProgress, -5*Mth.DEG_TO_RAD, -110*Mth.DEG_TO_RAD);
			rightForearm.zRot = -10*Mth.DEG_TO_RAD;
			leftForearm.xRot = Mth.lerp(animProgress, -5*Mth.DEG_TO_RAD, -110*Mth.DEG_TO_RAD);
			leftForearm.zRot = 10*Mth.DEG_TO_RAD;
			rightTrunk.y = 8;
			leftTrunk.y = 8;
		}
		else if (entity.clientAnim == ShrubsnapperEntity.ANIM_NEUTRAL && animProgress < 0.99) {
			body.xRot = Mth.lerp(animProgress, 40*Mth.DEG_TO_RAD, body.xRot);
			head.xRot = Mth.lerp(animProgress, HEAD_BASE_X_ROT, head.xRot);
			head.yRot = head.yRot*animProgress;
			rightArm.xRot = rightArm.xRot*animProgress;
			rightArm.yRot = Mth.lerp(animProgress, -30*Mth.DEG_TO_RAD, rightArm.yRot);
			rightArm.zRot = Mth.lerp(animProgress, 70*Mth.DEG_TO_RAD, rightArm.zRot);
			leftArm.xRot = leftArm.xRot*animProgress;
			leftArm.yRot = Mth.lerp(animProgress, 30*Mth.DEG_TO_RAD, leftArm.yRot);
			leftArm.zRot = Mth.lerp(animProgress, -70*Mth.DEG_TO_RAD, leftArm.zRot);
			rightForearm.xRot = Mth.lerp(animProgress, -110*Mth.DEG_TO_RAD, rightForearm.xRot);
			rightForearm.zRot = Mth.lerp(animProgress, -10*Mth.DEG_TO_RAD, rightForearm.zRot);
			leftForearm.xRot = Mth.lerp(animProgress, -110*Mth.DEG_TO_RAD, leftForearm.xRot);
			leftForearm.zRot = Mth.lerp(animProgress, 10*Mth.DEG_TO_RAD, leftForearm.zRot);
			rightTrunk.y = Mth.lerp(animProgress, 8, rightTrunk.y);
			leftTrunk.y = Mth.lerp(animProgress, 8, leftTrunk.y);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, buffer, packedLight, packedOverlay);
		rightLeg.render(poseStack, buffer, packedLight, packedOverlay);
		leftLeg.render(poseStack, buffer, packedLight, packedOverlay);
	}

}
