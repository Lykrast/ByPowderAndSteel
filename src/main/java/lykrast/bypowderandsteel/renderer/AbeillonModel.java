package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

public class AbeillonModel<T extends Mob> extends EntityModel<T> {
	// Made with Blockbench 4.1.5 then adjusted later for animations and shit
	private final ModelPart body, head, abdomen, legRF, legRM, legRB, legLF, legLM, legLB, wingRight, wingLeft;

	public AbeillonModel(ModelPart root) {
		body = root.getChild("body");
		head = root.getChild("head");
		abdomen = body.getChild("abdomen");
		legRF = body.getChild("legRF");
		legRM = body.getChild("legRM");
		legRB = body.getChild("legRB");
		legLF = body.getChild("legLF");
		legLM = body.getChild("legLM");
		legLB = body.getChild("legLB");
		wingRight = body.getChild("wingR");
		wingLeft = body.getChild("wingL");
	}

	private static final float LEG_Y_ANGLE = 10 * Mth.DEG_TO_RAD, LEG_CURL_Z = 30 * Mth.DEG_TO_RAD, BODY_CURL_X = 10*Mth.DEG_TO_RAD;

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0, 16, -5));
		body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-3, -6, 3, 6, 10, 6, CubeDeformation.NONE), PartPose.offsetAndRotation(0, 7, 5, 1.5708F, 0, 0));
		
		PartDefinition abdomen = body.addOrReplaceChild("abdomen", CubeListBuilder.create(), PartPose.offset(0, 1, 8.25F));
		abdomen.addOrReplaceChild("abdomen_r1", CubeListBuilder.create().texOffs(0, 32).addBox(-4, -1, -0.75F, 8, 12, 8, CubeDeformation.NONE), PartPose.offsetAndRotation(0, 4, 0.75F, 1.5708F, 0, 0));

		PartDefinition legRM = body.addOrReplaceChild("legRM", CubeListBuilder.create(), PartPose.offset(-3, 1, 4));

		PartDefinition visuallegRM = legRM.addOrReplaceChild("visuallegRM", CubeListBuilder.create().texOffs(24, 16).addBox(-1, -1, -1, 2, 6, 2, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0, 0, 0, 0, 0, 0.6981F));
		visuallegRM.addOrReplaceChild("forelegRM", CubeListBuilder.create().texOffs(24, 24).addBox(-1, 0, -1, 2, 4, 2, CubeDeformation.NONE), PartPose.offsetAndRotation(0, 4, 0, 0, 0, -0.6981F));

		PartDefinition legRF = body.addOrReplaceChild("legRF", CubeListBuilder.create(), PartPose.offsetAndRotation(-3, 1, 1.5F, 0, -LEG_Y_ANGLE, 0));
		PartDefinition visuallegRF = legRF.addOrReplaceChild("visuallegRF", CubeListBuilder.create().texOffs(24, 16).addBox(-1, -1, -1.5F, 2, 6, 2, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0, 0, 0.5F, 0, 0, 0.6981F));
		visuallegRF.addOrReplaceChild("forelegRF", CubeListBuilder.create().texOffs(24, 24).addBox(-1, 0, -1.5F, 2, 4, 2, CubeDeformation.NONE), PartPose.offsetAndRotation(0, 4, 0, 0, 0, -0.6981F));

		PartDefinition legRB = body.addOrReplaceChild("legRB", CubeListBuilder.create(), PartPose.offsetAndRotation(-3, 1, 6.5F, 0, LEG_Y_ANGLE, 0));
		PartDefinition visuallegRB = legRB.addOrReplaceChild("visuallegRB", CubeListBuilder.create().texOffs(24, 16).addBox(-1, -1, -1.5F, 2, 6, 2, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0, 0, 0.5F, 0, 0, 0.6981F));
		visuallegRB.addOrReplaceChild("forelegRB", CubeListBuilder.create().texOffs(24, 24).addBox(-1, 0, -1.5F, 2, 4, 2, CubeDeformation.NONE), PartPose.offsetAndRotation(0, 4, 0, 0, 0, -0.6981F));

		PartDefinition legLM = body.addOrReplaceChild("legLM", CubeListBuilder.create(), PartPose.offset(3, 1, 4));
		PartDefinition visuallegLM = legLM.addOrReplaceChild("visuallegLM", CubeListBuilder.create().texOffs(24, 16).mirror().addBox(-1, -1, -1, 2, 6, 2, new CubeDeformation(0.1F)).mirror(false),
				PartPose.offsetAndRotation(0, 0, 0, 0, 0, -0.6981F));
		visuallegLM.addOrReplaceChild("forelegLM", CubeListBuilder.create().texOffs(24, 24).mirror().addBox(-1, 0, -1, 2, 4, 2, CubeDeformation.NONE).mirror(false),
				PartPose.offsetAndRotation(0, 4, 0, 0, 0, 0.6981F));

		PartDefinition legLF = body.addOrReplaceChild("legLF", CubeListBuilder.create(), PartPose.offsetAndRotation(3, 1, 1.5F, 0, LEG_Y_ANGLE, 0));
		PartDefinition visuallegLF = legLF.addOrReplaceChild("visuallegLF", CubeListBuilder.create().texOffs(24, 16).mirror().addBox(-1, -1, -1.5F, 2, 6, 2, new CubeDeformation(0.1F)).mirror(false),
				PartPose.offsetAndRotation(0, 0, 0.5F, 0, 0, -0.6981F));
		visuallegLF.addOrReplaceChild("forelegLF", CubeListBuilder.create().texOffs(24, 24).mirror().addBox(-1, 0, -1.5F, 2, 4, 2, CubeDeformation.NONE).mirror(false),
				PartPose.offsetAndRotation(0, 4, 0, 0, 0, 0.6981F));

		PartDefinition legLB = body.addOrReplaceChild("legLB", CubeListBuilder.create(), PartPose.offsetAndRotation(3, 1, 6.5F, 0, -LEG_Y_ANGLE, 0));
		PartDefinition visuallegLB = legLB.addOrReplaceChild("visuallegLB", CubeListBuilder.create().texOffs(24, 16).mirror().addBox(-1, -1, -1.5F, 2, 6, 2, new CubeDeformation(0.1F)).mirror(false),
				PartPose.offsetAndRotation(0, 0, 0.5F, 0, 0, -0.6981F));
		visuallegLB.addOrReplaceChild("forelegLB", CubeListBuilder.create().texOffs(24, 24).mirror().addBox(-1, 0, -1.5F, 2, 4, 2, CubeDeformation.NONE).mirror(false),
				PartPose.offsetAndRotation(0, 4, 0, 0, 0, 0.6981F));

		PartDefinition wingR = body.addOrReplaceChild("wingR", CubeListBuilder.create(), PartPose.offset(-1, -2, 4));
		wingR.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(32, 0).addBox(-13, -16, 0, 16, 16, 0, CubeDeformation.NONE), PartPose.offsetAndRotation(0, 0, 0, 0, 1.5708F, 0));

		PartDefinition wingL = body.addOrReplaceChild("wingL", CubeListBuilder.create(), PartPose.offset(1, -2, 4));
		wingL.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(32, 16).addBox(-13, -16, 0, 16, 16, 0, CubeDeformation.NONE), PartPose.offsetAndRotation(0, 0, 0, 0, 1.5708F, 0));

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4, -3, -8, 8, 8, 8, CubeDeformation.NONE), PartPose.offset(0, 16.75F, -5));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//Mostly adapted from HumanoidModel
		boolean falling = entity.getFallFlyingTicks() > 4;
		head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		if (falling) head.xRot = -Mth.PI / 4;
		else head.xRot = headPitch * Mth.DEG_TO_RAD;

		legRF.yRot = -LEG_Y_ANGLE;
		legRM.yRot = 0;
		legRB.yRot = LEG_Y_ANGLE;
		legLF.yRot = LEG_Y_ANGLE;
		legLM.yRot = 0;
		legLB.yRot = -LEG_Y_ANGLE;
		//reset for bob
		legRF.xRot = 0;
		legRM.xRot = 0;
		legRB.xRot = 0;
		legLF.xRot = 0;
		legLM.xRot = 0;
		legLB.xRot = 0;
		//this part from bees for the wings
		boolean landed = entity.onGround();
		if (landed) {
			float f = ageInTicks * Mth.TWO_PI * 0.04f;
			wingLeft.zRot = (Mth.cos(f) + 1) * 5 * Mth.DEG_TO_RAD;
			wingRight.zRot = -wingLeft.zRot;
			//walking legs, from spider
			float f3 = -(Mth.cos(limbSwing * 0.6662F * 2 + 0) * 0.4F) * limbSwingAmount;
			float f4 = -(Mth.cos(limbSwing * 0.6662F * 2 + Mth.PI) * 0.4F) * limbSwingAmount;
			//float f5 = -(Mth.cos(limbSwing * 0.6662F * 2 + Mth.HALF_PI) * 0.4F) * limbSwingAmount;
			float f6 = -(Mth.cos(limbSwing * 0.6662F * 2 + Mth.PI * 1.5F) * 0.4F) * limbSwingAmount;
			legRB.yRot += f3;
			legLB.yRot += -f3;
			legRM.yRot += f4;
			legLM.yRot += -f4;
			//rightMiddleFrontLeg.yRot += f5;
			//leftMiddleFrontLeg.yRot += -f5;
			legRF.yRot += f6;
			legLF.yRot += -f6;
			//decurl up
			legRF.zRot = 0;
			legRM.zRot = 0;
			legRB.zRot = 0;
			legLF.zRot = 0;
			legLM.zRot = 0;
			legLB.zRot = 0;
			body.xRot = 0;
			abdomen.xRot = 0;
		}
		else {
			float f = ageInTicks * Mth.TWO_PI * 0.2f; //4 flaps a second, monarch butterflies are 5-12, butterflies are apparently 20
			wingLeft.zRot = (Mth.cos(f) + 1) * 40 * Mth.DEG_TO_RAD;
			wingRight.zRot = -wingLeft.zRot;
			//curl legs
			legRF.zRot = -LEG_CURL_Z;
			legRM.zRot = -LEG_CURL_Z;
			legRB.zRot = -LEG_CURL_Z;
			legLF.zRot = LEG_CURL_Z;
			legLM.zRot = LEG_CURL_Z;
			legLB.zRot = LEG_CURL_Z;
			body.xRot = -BODY_CURL_X;
			abdomen.xRot = -BODY_CURL_X;
			AnimationUtils.bobArms(legRF, legLF, ageInTicks + 30);
			AnimationUtils.bobArms(legRM, legLM, ageInTicks);
			AnimationUtils.bobArms(legRB, legLB, ageInTicks + 70);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, buffer, packedLight, packedOverlay);
		head.render(poseStack, buffer, packedLight, packedOverlay);
	}
}
