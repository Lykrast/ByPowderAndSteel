package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;

public class CowbonesModel<T extends Mob & RangedAttackMob> extends HumanoidModel<T> {
	public CowbonesModel(ModelPart modelpart) {
		super(modelpart);
	}
	
	//technically it's just for the buckaroo + the static bodylayer creation
	//pistolero has its own model

	public static LayerDefinition createBodyLayer() {
		//fuck it's tangled gotta copy paste the skeleton model to get the model definition
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-5.0F, 2.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(5.0F, 2.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-2.0F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(2.0F, 12.0F, 0.0F));

		PartDefinition head = partdefinition.getChild("head");

		//Blockbenched horns
		PartDefinition hornRight = head.addOrReplaceChild("hornRight", CubeListBuilder.create().texOffs(48, 16).addBox(-3.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-4.0F, -6.0F, -1.0F, 0.5236F, 0.0F, 0.1745F));
		hornRight.addOrReplaceChild("hornTipRight", CubeListBuilder.create().texOffs(48, 20).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
		PartDefinition hornLeft = head.addOrReplaceChild("hornLeft",
				CubeListBuilder.create().texOffs(48, 16).mirror().addBox(-1.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(4.0F, -6.0F, -1.0F, 0.5236F, 0.0F, -0.1745F));
		hornLeft.addOrReplaceChild("hornTipLeft", CubeListBuilder.create().texOffs(48, 20).mirror().addBox(0.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false),
				PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
		//bow and arrow pose already aims like the head, so we just use it as a 2 hand carry
		rightArmPose = HumanoidModel.ArmPose.EMPTY;
		leftArmPose = HumanoidModel.ArmPose.EMPTY;
		if (!entity.getMainHandItem().isEmpty() && entity.isAggressive()) {
			if (entity.getMainArm() == HumanoidArm.RIGHT) rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
			else leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
		}

		super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack posestack) {
		//this is the only part of SkeletonModel I care about, I don't want the rest of the supers
		float offset = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
		ModelPart modelpart = getArm(arm);
		modelpart.x += offset;
		modelpart.translateAndRotate(posestack);
		modelpart.x -= offset;
	}

}
