package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.SaberSentryEntity;
import lykrast.bypowderandsteel.misc.BPASUtils;
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
	private float animProgress, spinAmount, bop;
	private Pose anim, prevAnim;
	//ANIM_NEUTRAL = 0, ANIM_RUN = 1, ANIM_WINDUP = 2, ANIM_SLASH = 3, ANIM_SPIN_START = 4, ANIM_SPINNING = 5, ANIM_SPIN_STOP = 6
	public static final Pose[] POSES = {new Pose(),
			new Pose().lowerBody(-30, 0, 0).upperBody(10, 0, 0).head(20, 0, 0).rightArm(0, 0, 30).rightForearm(40, 0, 0).leftArm(0, 0, -30).leftForearm(40, 0, 0),
			new Pose().lowerBody(10, 0, 0).upperBody(10, 0, 0).lockHead().rightArm(120, 20, 0).rightForearm(40, 0, 0).leftArm(120, -20, 0).leftForearm(40, 0, 0),
			new Pose().lowerBody(-35, 0, 0).upperBody(-15, 0, 0).lockHead().rightArm(60, 15, 0).leftArm(60, -15, 0),
			new Pose().lowerBody(-15, 0, 0).rightArm(0, 0, 90).leftArm(0, 0, -90).lockHead(),
			new Pose().lowerBody(-50, 0, 0).rightArm(0, 0, 90).leftArm(0, 0, -90).lockHead(),
			new Pose().rightArm(0, 0, 90).leftArm(0, 0, -90).lockHead()
			};

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

		PartDefinition UpperBody = Body.addOrReplaceChild("UpperBody", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -6.0F, 0.0F));

		UpperBody.addOrReplaceChild("breast_r1", CubeListBuilder.create().texOffs(16, 36).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, -2.0F, -0.5236F, 0.0F, 0.0F));

		UpperBody.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -6.0F, 0.0F));

		PartDefinition RightArm = UpperBody.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -4.0F, 0.0F));

		RightArm.addOrReplaceChild("RightForearm", CubeListBuilder.create().texOffs(40, 30).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(-0.1F))
		.texOffs(50, 42).addBox(-0.5F, 3.0F, -3.0F, 1.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 6.0F, 0.0F));

		PartDefinition LeftArm = UpperBody.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, -4.0F, 0.0F));

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
		spinAmount = entity.getSpinAngle(partialTick);
		anim = POSES[entity.clientAnim];
		prevAnim = POSES[entity.prevAnim];
		if (entity.clientAnim == SaberSentryEntity.ANIM_SLASH) animProgress = BPASUtils.easeOutQuad(animProgress);
		else animProgress = BPASUtils.easeInQuad(animProgress);
		//for rift of necrodancer easter egg
		//wanted to bop head at 130 bpm (like on ravevenge hard (favorite track (and it has best girl suzu <3))) but 133 makes it land on ticks
		bop = (entity.tickCount % 9) + partialTick;
		if (bop >= 4.5f) bop = 0;
		else bop = BPASUtils.easeInQuad(1-(bop/4.5f)); //starts at 1 and goes to 0, so use the opposite easing that I'd use
	}

	@Override
	public void setupAnim(SaberSentryEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//Mostly adapted from HumanoidModel
		boolean falling = entity.getFallFlyingTicks() > 4;
		head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		if (falling) head.xRot = -Mth.PI / 4;
		else head.xRot = headPitch * Mth.DEG_TO_RAD;
		head.zRot = 0;

		float swingModifier = 1;
		if (falling) {
			swingModifier = (float) entity.getDeltaMovement().lengthSqr();
			swingModifier /= 0.2F;
			swingModifier *= swingModifier * swingModifier;
		}

		if (swingModifier < 1) swingModifier = 1;

		//rightArm.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 2.0F * limbSwingAmount * 0.5F / swingModifier;
		//leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / swingModifier;
		//rightArm.zRot = 0.0F;
		//leftArm.zRot = 0.0F;
		rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / swingModifier;
		leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount / swingModifier;
		rightLeg.yRot = 0.005F;
		leftLeg.yRot = -0.005F;
		rightLeg.zRot = 0.005F;
		leftLeg.zRot = -0.005F;
		if (riding) {
			//rightArm.xRot += (-Mth.PI / 5F);
			//leftArm.xRot += (-Mth.PI / 5F);
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
		
		anim.interpolate(this, prevAnim, animProgress);
		//Upper body y rotation reserved for spinnin
		upperBody.yRot = spinAmount * Mth.DEG_TO_RAD;
		
		//rift of the necrodancer easter egg
		if (entity.getCosmetic() == 19) {
			head.xRot += bop * 15*Mth.DEG_TO_RAD;
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		lowerBody.render(poseStack, buffer, packedLight, packedOverlay);
		rightLeg.render(poseStack, buffer, packedLight, packedOverlay);
		leftLeg.render(poseStack, buffer, packedLight, packedOverlay);
	}
	
	//this might be a mess
	private static class Pose {
		//x y z rot
		public final float[] lowerBody = {0,0,0},
				upperBody = {0,0,0},
				head = {0,0,0},
				leftArm = {0,0,0},
				leftForearm = {0,0,0},
				rightArm = {0,0,0},
				rightForearm = {0,0,0};
		//head is the only one that has an innate animation
		public boolean headLock = false;
		
		public void interpolate(SaberSentryModel model, Pose prev, float progress) {
			if (progress > 0.99) {
				apply(model.lowerBody, lowerBody);
				apply(model.upperBody, upperBody);
				apply(model.leftArm, leftArm);
				apply(model.leftForearm, leftForearm);
				apply(model.rightArm, rightArm);
				apply(model.rightForearm, rightForearm);
				//head rotation is set before we call the pose
				if (headLock) apply(model.head, head);
				else {
					model.head.xRot += head[0];
					model.head.yRot += head[1];
					model.head.zRot += head[2];
				}
			}
			else {
				interpolateRot(model.lowerBody, lowerBody, prev.lowerBody, progress);
				interpolateRot(model.upperBody, upperBody, prev.upperBody, progress);
				interpolateRot(model.leftArm, leftArm, prev.leftArm, progress);
				interpolateRot(model.leftForearm, leftForearm, prev.leftForearm, progress);
				interpolateRot(model.rightArm, rightArm, prev.rightArm, progress);
				interpolateRot(model.rightForearm, rightForearm, prev.rightForearm, progress);
				//head rotation is set before we call the pose
				model.head.xRot = BPASUtils.rotlerpRad(progress, headLock ? head[0] : model.head.xRot + head[0], prev.headLock ? prev.head[0] : model.head.xRot + prev.head[0]);
				model.head.yRot = BPASUtils.rotlerpRad(progress, headLock ? head[1] : model.head.yRot + head[1], prev.headLock ? prev.head[1] : model.head.yRot + prev.head[1]);
				model.head.zRot = BPASUtils.rotlerpRad(progress, headLock ? head[2] : model.head.zRot + head[2], prev.headLock ? prev.head[2] : model.head.zRot + prev.head[2]);
			}
		}
		
		private static void interpolateRot(ModelPart part, float[] self, float[] prev, float progress) {
			part.xRot = BPASUtils.rotlerpRad(progress, prev[0], self[0]);
			part.yRot = BPASUtils.rotlerpRad(progress, prev[1], self[1]);
			part.zRot = BPASUtils.rotlerpRad(progress, prev[2], self[2]);
		}
		
		private static void apply(ModelPart part, float[] self) {
			part.xRot = self[0];
			part.yRot = self[1];
			part.zRot = self[2];
		}
		
		//Don't know why some of the blockbench angles are reversed so that's why there are -
		public Pose lowerBody(float x, float y, float z) {
			lowerBody[0] = x * -Mth.DEG_TO_RAD;
			lowerBody[1] = y * -Mth.DEG_TO_RAD;
			lowerBody[2] = z * Mth.DEG_TO_RAD;
			return this;
		}
		public Pose upperBody(float x, float y, float z) {
			upperBody[0] = x * -Mth.DEG_TO_RAD;
			upperBody[1] = y * -Mth.DEG_TO_RAD;
			upperBody[2] = z * Mth.DEG_TO_RAD;
			return this;
		}
		public Pose leftArm(float x, float y, float z) {
			leftArm[0] = x * -Mth.DEG_TO_RAD;
			leftArm[1] = y * -Mth.DEG_TO_RAD;
			leftArm[2] = z * Mth.DEG_TO_RAD;
			return this;
		}
		public Pose leftForearm(float x, float y, float z) {
			leftForearm[0] = x * -Mth.DEG_TO_RAD;
			leftForearm[1] = y * -Mth.DEG_TO_RAD;
			leftForearm[2] = z * Mth.DEG_TO_RAD;
			return this;
		}
		public Pose rightArm(float x, float y, float z) {
			rightArm[0] = x * -Mth.DEG_TO_RAD;
			rightArm[1] = y * -Mth.DEG_TO_RAD;
			rightArm[2] = z * Mth.DEG_TO_RAD;
			return this;
		}
		public Pose rightForearm(float x, float y, float z) {
			rightForearm[0] = x * -Mth.DEG_TO_RAD;
			rightForearm[1] = y * -Mth.DEG_TO_RAD;
			rightForearm[2] = z * Mth.DEG_TO_RAD;
			return this;
		}
		public Pose head(float x, float y, float z) {
			head[0] = x * -Mth.DEG_TO_RAD;
			head[1] = y * -Mth.DEG_TO_RAD;
			head[2] = z * Mth.DEG_TO_RAD;
			return this;
		}
		public Pose lockHead() {
			headLock = true;
			return this;
		}
	}

}
