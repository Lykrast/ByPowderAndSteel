package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ShrubhulkEntity;
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

public class ShrubhulkModel extends EntityModel<ShrubhulkEntity> {
	// Made with Blockbench 4.1.5 then adjusted later for animations and shit
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("shrubhulk"), "main");
	private final ModelPart body, head, rightArm, leftArm, rightLeg, leftLeg;
	private float animProgress;

	public ShrubhulkModel(ModelPart root) {
		body = root.getChild("MainBody");
		rightLeg = root.getChild("RightLeg");
		leftLeg = root.getChild("LeftLeg");
		head = body.getChild("Head");
		rightArm = body.getChild("RightArm");
		leftArm = body.getChild("LeftArm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition MainBody = partdefinition.addOrReplaceChild("MainBody", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 4.0F));
		MainBody.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(24, 16).addBox(-6.0F, -3.0F, -3.0F, 12.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -9.0F, -7.0F, 0.6981F, 0.0F, 0.0F));
		MainBody.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(0, 36).addBox(-5.0F, -2.0F, -3.0F, 6.0F, 18.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-7.0F, -10.0F, -7.0F));
		MainBody.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 36).mirror().addBox(-1.0F, -2.0F, -3.0F, 6.0F, 18.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(7.0F, -10.0F, -7.0F));
		MainBody.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, -9.0F));
		partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-2.9F, 12.0F, 4.0F));
		partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-3.0F, -2.0F, -3.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(2.9F, 12.0F, 4.0F));
		
		return LayerDefinition.create(meshdefinition, 64, 64);
	}
	
	@Override
	public void prepareMobModel(ShrubhulkEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
		//the super is empty
		animProgress = entity.getAnimProgress(partialTick);
		if (entity.clientAnim == ShrubhulkEntity.ANIM_SLAM) animProgress = BPASUtils.easeOutQuad(animProgress);
		else animProgress = BPASUtils.easeInQuad(animProgress);
	}

	@Override
	public void setupAnim(ShrubhulkEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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
		
		body.xRot = 0;
		rightArm.yRot = 0;
		leftArm.yRot = 0;
		
		//animate now that we have the swung arm to return to neutral
		//because a slam is not supposed to be interrupted by another anim, just ease neutral -> windup -> slam -> neutral
		if (entity.clientAnim == ShrubhulkEntity.ANIM_WINDUP) {
			//reminder: blockbench angles for X and Y are negated for some reason
			body.xRot = -35*animProgress * Mth.DEG_TO_RAD;
			head.xRot = Mth.lerp(animProgress, head.xRot, 45*Mth.DEG_TO_RAD);
			head.yRot = head.yRot*(1-animProgress);
			rightArm.xRot = Mth.lerp(animProgress, rightArm.xRot, -95*Mth.DEG_TO_RAD);
			rightArm.yRot = -25*animProgress * Mth.DEG_TO_RAD;
			rightArm.zRot = rightArm.zRot*(1-animProgress);
			leftArm.xRot = Mth.lerp(animProgress, leftArm.xRot, -95*Mth.DEG_TO_RAD);
			leftArm.yRot = 25*animProgress * Mth.DEG_TO_RAD;
			leftArm.zRot = leftArm.zRot*(1-animProgress);
		}
		else if (entity.clientAnim == ShrubhulkEntity.ANIM_SLAM) {
			body.xRot = Mth.lerp(animProgress, -35*Mth.DEG_TO_RAD, 55*Mth.DEG_TO_RAD);
			head.xRot = 45*Mth.DEG_TO_RAD;
			head.yRot = 0;
			rightArm.xRot = -95*Mth.DEG_TO_RAD;
			rightArm.yRot = -25*Mth.DEG_TO_RAD;
			rightArm.zRot = 0;
			leftArm.xRot = -95*Mth.DEG_TO_RAD;
			leftArm.yRot = 25*Mth.DEG_TO_RAD;
			leftArm.zRot = 0;
		}
		else if (entity.clientAnim == ShrubhulkEntity.ANIM_NEUTRAL && animProgress < 0.99) {
			body.xRot = 55*(1-animProgress) * Mth.DEG_TO_RAD;
			head.xRot = Mth.lerp(animProgress, 45*Mth.DEG_TO_RAD, head.xRot);
			head.yRot = head.yRot*animProgress;
			rightArm.xRot = Mth.lerp(animProgress, -95*Mth.DEG_TO_RAD, rightArm.xRot);
			rightArm.yRot = -25*(1-animProgress) * Mth.DEG_TO_RAD;
			rightArm.zRot = rightArm.zRot*animProgress;
			leftArm.xRot = Mth.lerp(animProgress, -95*Mth.DEG_TO_RAD, leftArm.xRot);
			leftArm.yRot = 25*(1-animProgress) * Mth.DEG_TO_RAD;
			leftArm.zRot = leftArm.zRot*animProgress;
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, buffer, packedLight, packedOverlay);
		rightLeg.render(poseStack, buffer, packedLight, packedOverlay);
		leftLeg.render(poseStack, buffer, packedLight, packedOverlay);
	}

}
