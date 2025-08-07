package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.PatrollerEntity;
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

public class PatrollerModel extends EntityModel<PatrollerEntity> {
	// Made with Blockbench 4.1.5 then adjusted later for animations and shit
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("patroller"), "main");
	private final ModelPart body, eye, legFL, legFR, legBL, legBR, footFL, footFR, footBL, footBR;

	public PatrollerModel(ModelPart root) {
		body = root.getChild("body");
		eye = body.getChild("eye");
		legFL = body.getChild("legFrontLeft");
		footFL = legFL.getChild("footFrontLeft");
		legFR = body.getChild("legFrontRight");
		footFR = legFR.getChild("footFrontRight");
		legBL = body.getChild("legBackLeft");
		footBL = legBL.getChild("footBackLeft");
		legBR = body.getChild("legBackRight");
		footBR = legBR.getChild("footBackRight");
	}
	
	private static final float LEG_ANGLE = 100*Mth.DEG_TO_RAD;

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -15.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 18.0F, 7.0F));

		body.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -13.0F, -6.25F, 2.0F, 2.0F, 1.0F, CubeDeformation.NONE), PartPose.offset(0.0F, 3.0F, -9.0F));

		PartDefinition legFrontRight = body.addOrReplaceChild("legFrontRight", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 10.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(7.0F, -1.0F, -14.0F, -LEG_ANGLE, -0.0873F, 0.0F));
		legFrontRight.addOrReplaceChild("footFrontRight", CubeListBuilder.create().texOffs(0, 46).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 9.0F, 0.0F, LEG_ANGLE, 0.0F, 0.0F));

		PartDefinition legFrontLeft = body.addOrReplaceChild("legFrontLeft",
				CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-3.0F, -1.0F, -2.0F, 4.0F, 10.0F, 4.0F, CubeDeformation.NONE).mirror(false),
				PartPose.offsetAndRotation(-6.0F, -1.0F, -14.0F, -LEG_ANGLE, 0.0873F, 0.0F));
		legFrontLeft.addOrReplaceChild("footFrontLeft", CubeListBuilder.create().texOffs(0, 46).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false),
				PartPose.offsetAndRotation(-1.0F, 9.0F, 0.0F, LEG_ANGLE, 0.0F, 0.0F));

		PartDefinition legBackRight = body.addOrReplaceChild("legBackRight", CubeListBuilder.create().texOffs(16, 32).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 10.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(7.0F, -1.0F, 0.0F, -LEG_ANGLE, -3.0543F, 0.0F));
		legBackRight.addOrReplaceChild("footBackRight", CubeListBuilder.create().texOffs(16, 46).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 9.0F, 0.0F, LEG_ANGLE, 0.0F, 0.0F));

		PartDefinition legBackLeft = body.addOrReplaceChild("legBackLeft",
				CubeListBuilder.create().texOffs(16, 32).mirror().addBox(-1.0F, -1.0F, -2.0F, 4.0F, 10.0F, 4.0F, CubeDeformation.NONE).mirror(false),
				PartPose.offsetAndRotation(-6.0F, -1.0F, 0.0F, -LEG_ANGLE, 3.0543F, 0.0F));
		legBackLeft.addOrReplaceChild("footBackLeft", CubeListBuilder.create().texOffs(16, 46).mirror().addBox(0.0F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false),
				PartPose.offsetAndRotation(-1.0F, 9.0F, 0.0F, LEG_ANGLE, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void prepareMobModel(PatrollerEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
	}

	@Override
	public void setupAnim(PatrollerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//My attempt to get the eye position depending on head pitch
		//center of body, center of eye, intersection of view vector and body border = right triangle
		//so eye offset = tan(yaw)*(to body border = 8px)
		//and of course so Mth.tan so sin/cos it is (with cos = 0 for 90° so putting a generous cap here)
		float cappedYaw = Mth.clamp(Mth.abs(netHeadYaw), 0, 70) * Mth.DEG_TO_RAD;
		float offset = 6*Mth.sin(cappedYaw)/Mth.cos(cappedYaw);
		if (offset > 2) offset = 2;
        eye.x = offset * -(float)Math.signum(netHeadYaw);
        
        //Legs, based on the humanoid ones
		boolean falling = entity.getFallFlyingTicks() > 4;
		float swingModifier = 1;
		if (falling) {
			swingModifier = (float) entity.getDeltaMovement().lengthSqr();
			swingModifier /= 0.2F;
			swingModifier *= swingModifier * swingModifier;
		}

		if (swingModifier < 1) swingModifier = 1;
		
		float swing1 = Mth.cos(limbSwing * 1.2F) * 0.8F * limbSwingAmount / swingModifier;
		float swing2 = Mth.cos(limbSwing * 1.2F + Mth.PI) * 0.8F * limbSwingAmount / swingModifier;
		float swing3 = Mth.cos(limbSwing * 1.2F + Mth.PI + Mth.HALF_PI) * 0.8F * limbSwingAmount / swingModifier;
		float swing4 = Mth.cos(limbSwing * 1.2F + Mth.HALF_PI) * 0.8F * limbSwingAmount / swingModifier;
		legFR.xRot = swing1 - LEG_ANGLE;
		legFL.xRot = swing2 - LEG_ANGLE;
		legBR.xRot = swing3 - LEG_ANGLE;
		legBL.xRot = swing4 - LEG_ANGLE;
		footFR.xRot = swing1 + LEG_ANGLE;
		footFL.xRot = swing2 + LEG_ANGLE;
		footBR.xRot = swing3 + LEG_ANGLE;
		footBL.xRot = swing4 + LEG_ANGLE;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, buffer, packedLight, packedOverlay);
	}
}
