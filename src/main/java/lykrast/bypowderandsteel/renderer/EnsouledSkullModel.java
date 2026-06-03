package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.EnsouledSkullEntity;
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

public class EnsouledSkullModel extends EntityModel<EnsouledSkullEntity> {
	// Made with Blockbench 4.1.5 then adjusted later for animations and shit
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("ensouled_skull"), "main");
	private final ModelPart whole, head, jaw;

	public EnsouledSkullModel(ModelPart root) {
		whole = root.getChild("whole");
		head = whole.getChild("head");
		jaw = whole.getChild("jaw");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition whole = partdefinition.addOrReplaceChild("whole", CubeListBuilder.create(), PartPose.offset(0, 20, 0));

		whole.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4, -7, -7, 8, 8, 8).texOffs(0, 16).addBox(-4, -7, -7, 8, 6, 8, new CubeDeformation(-0.25F)),
				PartPose.offset(0, 3, 3));

		whole.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(32, 0).addBox(-4, -7, -7, 8, 8, 8, new CubeDeformation(0.25F)), PartPose.offset(0, 3, 3));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void prepareMobModel(EnsouledSkullEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
	}

	@Override
	public void setupAnim(EnsouledSkullEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//TODO animate
		whole.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		whole.xRot = headPitch * Mth.DEG_TO_RAD;
		if (entity.isAggressive()) {
			head.xRot = -10 * Mth.DEG_TO_RAD;
			jaw.xRot = 20 * Mth.DEG_TO_RAD;
		}
		else {
			head.xRot = 0;
			jaw.xRot = 0;
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		whole.render(poseStack, buffer, packedLight, packedOverlay);
	}
}
