package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.entity.ZombunnySlasherEntity;
import lykrast.bypowderandsteel.misc.BPASUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class ZombunnyModel<T extends ZombunnySlasherEntity> extends HumanoidModel<T> {	
	private float animProgress;
	
	public ZombunnyModel(ModelPart modelpart) {
		super(modelpart);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition body = partdefinition.getChild("body");
		PartDefinition head = partdefinition.getChild("head");

		//Blockbenched parts
		head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(24, 0).addBox(-2, -3, -6, 4, 3, 2, CubeDeformation.NONE), PartPose.offset(0, 0, 0));

		head.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(56, 16).addBox(-1.5F, -8, -1, 3, 8, 1, CubeDeformation.NONE), PartPose.offsetAndRotation(-2.5F, -8, 2, 0, -0.2618F, 0));
		head.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(56, 16).mirror().addBox(-1.5F, -8, -1, 3, 8, 1, CubeDeformation.NONE).mirror(false), PartPose.offsetAndRotation(2.5F, -8, 2, 0, 0.2618F, 0));
		
		body.addOrReplaceChild("breast", CubeListBuilder.create().texOffs(16, 32).addBox(-4, 0, 0, 8, 5, 3, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0, 0, -2, -0.5236F, 0, 0));
		
		return LayerDefinition.create(meshdefinition, 64, 64);
	}
	
	@Override
	public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
		//the super is empty
		animProgress = entity.getAnimProgress(partialTick);
		animProgress = BPASUtils.easeOutQuad(animProgress);
	}
	
	private float getTargetXRot(int anim, float neutral) {
		//take the neutral because idle is what the humanoid tries to set
		switch (anim) {
			default:
			case ZombunnySlasherEntity.ANIM_NEUTRAL:
			case ZombunnySlasherEntity.ANIM_LAND:
				return neutral;
			case ZombunnySlasherEntity.ANIM_WINDUP:
				return 70*Mth.DEG_TO_RAD;
			case ZombunnySlasherEntity.ANIM_JUMP:
				return -90*Mth.DEG_TO_RAD;
		}
	}
	
	private float getTargetZRot(int anim, float neutral, boolean leftArm) {
		//take the neutral because idle is what the humanoid tries to set
		switch (anim) {
			default:
			case ZombunnySlasherEntity.ANIM_NEUTRAL:
			case ZombunnySlasherEntity.ANIM_LAND:
				return neutral;
			case ZombunnySlasherEntity.ANIM_WINDUP:
				return (leftArm ? -40 : 40)*Mth.DEG_TO_RAD;
			case ZombunnySlasherEntity.ANIM_JUMP:
				return (leftArm ? -75 : 75)*Mth.DEG_TO_RAD;
		}
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		int anim = entity.clientAnim;
		crouching = (anim != ZombunnySlasherEntity.ANIM_NEUTRAL);
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		if (animProgress > 0.999) {
			leftArm.xRot = getTargetXRot(anim, leftArm.xRot);
			leftArm.zRot = getTargetZRot(anim, leftArm.zRot, true);
			rightArm.xRot = getTargetXRot(anim, rightArm.xRot);
			rightArm.zRot = getTargetZRot(anim, rightArm.zRot, false);
		}
		else {
			int prev = entity.prevAnim;
			leftArm.xRot = BPASUtils.rotlerpRad(animProgress, getTargetXRot(prev, leftArm.xRot), getTargetXRot(anim, leftArm.xRot));
			leftArm.zRot = BPASUtils.rotlerpRad(animProgress, getTargetZRot(prev, leftArm.zRot, true), getTargetZRot(anim, leftArm.zRot, true));
			rightArm.xRot = BPASUtils.rotlerpRad(animProgress, getTargetXRot(prev, rightArm.xRot), getTargetXRot(anim, rightArm.xRot));
			rightArm.zRot = BPASUtils.rotlerpRad(animProgress, getTargetZRot(prev, rightArm.zRot, false), getTargetZRot(anim, rightArm.zRot, false));
		}
	}

}
