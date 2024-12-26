package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.CowbonesEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class CowbonesModel extends SkeletonModel<CowbonesEntity> {
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("cowbones"), "main");
	public static final ModelLayerLocation OVERLAY = new ModelLayerLocation(ByPowderAndSteel.rl("cowbones"), "overlay");
	public static final ModelLayerLocation INNER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("cowbones"), "inner_armor");
	public static final ModelLayerLocation OUTER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("cowbones"), "outer_armor");

	public CowbonesModel(ModelPart modelpart) {
		super(modelpart);
	}

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
	
	//TODO arm pose

}
