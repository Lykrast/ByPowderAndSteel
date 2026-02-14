package lykrast.bypowderandsteel.renderer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Mob;

public class ZombunnyModel<T extends Mob> extends HumanoidModel<T> {	
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
		body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 32).addBox(-2, 8, 2, 4, 4, 2, CubeDeformation.NONE), PartPose.offset(0, 0, 0));
		
		return LayerDefinition.create(meshdefinition, 64, 64);
	}
}
