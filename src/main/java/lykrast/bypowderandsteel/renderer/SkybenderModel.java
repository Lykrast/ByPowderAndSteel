package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.SkybenderEntity;
import lykrast.bypowderandsteel.misc.BPASUtils;
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
	private final ModelPart seat, head, armRight, armLeft, forearmRight, forearmLeft, blade, shield;
	//the 2 scales are for scaling the blade and shield (cause different components will scale at different speed for effect
	private float animProgress, scaleEaseIn, scaleEaseOut;
	private Pose anim, prevAnim;
	//ANIM_NEUTRAL = 0, ANIM_SHIELD = 1, ANIM_WINDUP = 2, ANIM_SLASH = 3, ANIM_STUN = 4;
	public static final Pose[] POSES = {new Pose(),
			new Pose().leftArm(90, 0, -90).leftForearm(90, 0, 0).rightArm(-30, 0, 0).rightForearm(130, 0, 0).shield(),
			new Pose().leftArm(30, 0, -60).leftForearm(80, 0, 0).rightArm(-45, 0, 50).rightForearm(15, 0, 0).sword(),
			new Pose().leftArm(25, 0, -40).rightArm(115, 0, 40).rightForearm(50, 0, 0).sword(),
			new Pose()}; //TODO stun animation

	public SkybenderModel(ModelPart root) {
		seat = root.getChild("seat");
		head = root.getChild("head");
		armRight = root.getChild("armRight");
		armLeft = root.getChild("armLeft");
		forearmRight = armRight.getChild("forearmRight");
		forearmLeft = armLeft.getChild("forearmLeft");
		blade = forearmRight.getChild("blade");
		shield = forearmLeft.getChild("shield");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(0, 16).addBox(-6.0F, -8.0F, -6.0F, 12.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-6.0F, -18.0F, 4.0F, 12.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition armRight = partdefinition.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(64, 0).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 16.0F, 0.0F));

		PartDefinition forearmRight = armRight.addOrReplaceChild("forearmRight", CubeListBuilder.create().texOffs(64, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(-2.0F, 9.0F, 0.0F));

		forearmRight.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(64, 32).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, -Mth.HALF_PI, 0.0F));

		PartDefinition armLeft = partdefinition.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(80, 0).addBox(0.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 16.0F, 0.0F));

		PartDefinition forearmLeft = armLeft.addOrReplaceChild("forearmLeft", CubeListBuilder.create().texOffs(80, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(2.0F, 9.0F, 0.0F));

		forearmLeft.addOrReplaceChild("shield", CubeListBuilder.create().texOffs(80, 32).addBox(-10.0F, -8.0F, 0.0F, 20.0F, 20.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}
	
	@Override
	public void prepareMobModel(SkybenderEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
		//the super is empty
		animProgress = entity.getAnimProgress(partialTick);
		scaleEaseIn = BPASUtils.easeInQuart(animProgress);
		scaleEaseOut = BPASUtils.easeOutQuart(animProgress);
		anim = POSES[entity.clientAnim];
		prevAnim = POSES[entity.prevAnim];
		if (entity.clientAnim == SkybenderEntity.ANIM_SLASH || entity.clientAnim == SkybenderEntity.ANIM_SHIELD || entity.clientAnim == SkybenderEntity.ANIM_STUN) animProgress = BPASUtils.easeOutQuad(animProgress);
		else animProgress = BPASUtils.easeInQuad(animProgress);
	}

	@Override
	public void setupAnim(SkybenderEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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
		
		anim.interpolate(this, prevAnim, animProgress, scaleEaseIn, scaleEaseOut);
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
	
	//this might be a mess number 2 (copied the saber sentry)
	private static class Pose {
		//x y z rot
		public final float[] leftArm = {0,0,0},
				leftForearm = {0,0,0},
				rightArm = {0,0,0},
				rightForearm = {0,0,0};
		public boolean sword = false, shield = false, neutralArms = true;
		
		public void interpolate(SkybenderModel model, Pose prev, float progress, float scaleEaseIn, float scaleEaseOut) {
			if (progress > 0.99) {
				if (!neutralArms) {
					apply(model.armLeft, leftArm);
					apply(model.forearmLeft, leftForearm);
					apply(model.armRight, rightArm);
					apply(model.forearmRight, rightForearm);
				}
				scaleWeapon(model.blade, sword);
				scaleWeapon(model.shield, shield);
			}
			else {
				if (neutralArms) {
					if (!prev.neutralArms) {
						//previous anim had posed arms, we don't
						interpolateToNeutral(model.armLeft, prev.leftArm, progress);
						interpolateToNeutral(model.forearmLeft, prev.leftForearm, progress);
						interpolateToNeutral(model.armRight, prev.rightArm, progress);
						interpolateToNeutral(model.forearmRight, prev.rightForearm, progress);
					}
					//neither anim posed arms so do nothing
				}
				else {
					if (prev.neutralArms) {
						//previous anim did not pose but we do
						interpolateFromNeutral(model.armLeft, leftArm, progress);
						interpolateFromNeutral(model.forearmLeft, leftForearm, progress);
						interpolateFromNeutral(model.armRight, rightArm, progress);
						interpolateFromNeutral(model.forearmRight, rightForearm, progress);
					}
					else {
						//pose to pose
						interpolateRot(model.armLeft, leftArm, prev.leftArm, progress);
						interpolateRot(model.forearmLeft, leftForearm, prev.leftForearm, progress);
						interpolateRot(model.armRight, rightArm, prev.rightArm, progress);
						interpolateRot(model.forearmRight, rightForearm, prev.rightForearm, progress);
					}
				}
				if (sword != prev.sword) {
					//y scale comes faster
					if (sword) {
						model.blade.xScale = scaleEaseIn;
						model.blade.yScale = scaleEaseOut;
						model.blade.zScale = scaleEaseIn;
					}
					else {
						model.blade.xScale = 1-scaleEaseOut;
						model.blade.yScale = 1-scaleEaseIn;
						model.blade.zScale = 1-scaleEaseOut;
					}
				}
				else scaleWeapon(model.blade, sword);
				if (shield != prev.shield) {
					//x scale comes faster
					if (shield) {
						model.shield.xScale = scaleEaseOut;
						model.shield.yScale = scaleEaseIn;
						model.shield.zScale = scaleEaseIn;
					}
					else {
						model.shield.xScale = 1-scaleEaseIn;
						model.shield.yScale = 1-scaleEaseOut;
						model.shield.zScale = 1-scaleEaseOut;
					}
				}
				else scaleWeapon(model.shield, shield);
			}
		}
		
		private static void scaleWeapon(ModelPart part, boolean should) {
			float amt = should ? 1 : 0;
			part.xScale = amt;
			part.yScale = amt;
			part.zScale = amt;
		}
		
		private static void interpolateRot(ModelPart part, float[] self, float[] prev, float progress) {
			part.xRot = BPASUtils.rotlerpRad(progress, prev[0], self[0]);
			part.yRot = BPASUtils.rotlerpRad(progress, prev[1], self[1]);
			part.zRot = BPASUtils.rotlerpRad(progress, prev[2], self[2]);
		}
		
		private static void interpolateFromNeutral(ModelPart part, float[] pose, float progress) {
			part.xRot = BPASUtils.rotlerpRad(progress, part.xRot, pose[0]);
			part.yRot = BPASUtils.rotlerpRad(progress, part.yRot, pose[1]);
			part.zRot = BPASUtils.rotlerpRad(progress, part.zRot, pose[2]);
		}
		
		private static void interpolateToNeutral(ModelPart part, float[] pose, float progress) {
			part.xRot = BPASUtils.rotlerpRad(progress, pose[0], part.xRot);
			part.yRot = BPASUtils.rotlerpRad(progress, pose[1], part.yRot);
			part.zRot = BPASUtils.rotlerpRad(progress, pose[2], part.zRot);
		}
		
		private static void apply(ModelPart part, float[] self) {
			part.xRot = self[0];
			part.yRot = self[1];
			part.zRot = self[2];
		}
		
		//Don't know why some of the blockbench angles are reversed so that's why there are -
		public Pose leftArm(float x, float y, float z) {
			leftArm[0] = x * -Mth.DEG_TO_RAD;
			leftArm[1] = y * -Mth.DEG_TO_RAD;
			leftArm[2] = z * Mth.DEG_TO_RAD;
			neutralArms = false;
			return this;
		}
		public Pose leftForearm(float x, float y, float z) {
			leftForearm[0] = x * -Mth.DEG_TO_RAD;
			leftForearm[1] = y * -Mth.DEG_TO_RAD;
			leftForearm[2] = z * Mth.DEG_TO_RAD;
			neutralArms = false;
			return this;
		}
		public Pose rightArm(float x, float y, float z) {
			rightArm[0] = x * -Mth.DEG_TO_RAD;
			rightArm[1] = y * -Mth.DEG_TO_RAD;
			rightArm[2] = z * Mth.DEG_TO_RAD;
			neutralArms = false;
			return this;
		}
		public Pose rightForearm(float x, float y, float z) {
			rightForearm[0] = x * -Mth.DEG_TO_RAD;
			rightForearm[1] = y * -Mth.DEG_TO_RAD;
			rightForearm[2] = z * Mth.DEG_TO_RAD;
			neutralArms = false;
			return this;
		}
		public Pose sword() {
			sword = true;
			return this;
		}
		public Pose shield() {
			shield = true;
			return this;
		}
	}

}
