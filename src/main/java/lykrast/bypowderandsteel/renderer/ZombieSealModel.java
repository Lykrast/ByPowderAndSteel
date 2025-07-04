package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ZombieSealEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.HumanoidArm;

public class ZombieSealModel extends HumanoidModel<ZombieSealEntity> {
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("zombie_seal"), "main");
	public static final ModelLayerLocation INNER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("zombie_seal"), "inner_armor");
	public static final ModelLayerLocation OUTER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("zombie_seal"), "outer_armor");

	public ZombieSealModel(ModelPart modelpart) {
		super(modelpart);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.getChild("head");

		//Blockbenched snout
		head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(24, 0).addBox(-3, -4, -6, 6, 4, 2, CubeDeformation.NONE), PartPose.offset(0, 0, 0));
		
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void prepareMobModel(ZombieSealEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
		//bow and arrow pose already aims like the head, so we just use it as a 2 hand carry
		rightArmPose = HumanoidModel.ArmPose.EMPTY;
		leftArmPose = HumanoidModel.ArmPose.EMPTY;
		if (!entity.getMainHandItem().isEmpty() && entity.isAggressive()) {
			if (entity.getMainArm() == HumanoidArm.RIGHT) rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
			else leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
		}

		super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
	}

}
