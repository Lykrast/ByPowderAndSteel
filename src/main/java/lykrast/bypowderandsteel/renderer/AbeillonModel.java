package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Mob;

public class AbeillonModel<T extends Mob> extends EntityModel<T> {
	// Made with Blockbench 4.1.5 then adjusted later for animations and shit
	private final ModelPart body, head;

	public AbeillonModel(ModelPart root) {
		this.body = root.getChild("body");
		this.head = root.getChild("head");
	}

	//TODO animate that shit
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, -5.0F));
		body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, -5.0F, 3.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 7.0F, 5.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition abdomen = body.addOrReplaceChild("abdomen", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 8.25F));
		abdomen.addOrReplaceChild("abdomen_r1", CubeListBuilder.create().texOffs(0, 30).addBox(-4.0F, -1.0F, -0.75F, 8.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.0F, 0.75F, 1.5708F, 0.0F, 0.0F));

		PartDefinition legRM = body.addOrReplaceChild("legRM", CubeListBuilder.create(), PartPose.offset(-3.0F, 1.0F, 4.0F));

		PartDefinition visuallegRM = legRM.addOrReplaceChild("visuallegRM", CubeListBuilder.create().texOffs(24, 16).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));
		visuallegRM.addOrReplaceChild("forelegRM", CubeListBuilder.create().texOffs(24, 24).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		PartDefinition legRF = body.addOrReplaceChild("legRF", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 1.0F, 1.5F, 0.0F, -0.1745F, 0.0F));
		PartDefinition visuallegRF = legRF.addOrReplaceChild("visuallegRF", CubeListBuilder.create().texOffs(24, 16).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.6981F));
		visuallegRF.addOrReplaceChild("forelegRF", CubeListBuilder.create().texOffs(24, 24).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		PartDefinition legRB = body.addOrReplaceChild("legRB", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 1.0F, 6.5F, 0.0F, 0.1745F, 0.0F));
		PartDefinition visuallegRB = legRB.addOrReplaceChild("visuallegRB", CubeListBuilder.create().texOffs(24, 16).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.6981F));
		visuallegRB.addOrReplaceChild("forelegRB", CubeListBuilder.create().texOffs(24, 24).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		PartDefinition legLM = body.addOrReplaceChild("legLM", CubeListBuilder.create(), PartPose.offset(3.0F, 1.0F, 4.0F));
		PartDefinition visuallegLM = legLM.addOrReplaceChild("visuallegLM",
				CubeListBuilder.create().texOffs(24, 16).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.1F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));
		visuallegLM.addOrReplaceChild("forelegLM", CubeListBuilder.create().texOffs(24, 24).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition legLF = body.addOrReplaceChild("legLF", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 1.0F, 1.5F, 0.0F, 0.1745F, 0.0F));
		PartDefinition visuallegLF = legLF.addOrReplaceChild("visuallegLF",
				CubeListBuilder.create().texOffs(24, 16).mirror().addBox(-1.0F, -1.0F, -1.5F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.1F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, 0.0F, -0.6981F));
		visuallegLF.addOrReplaceChild("forelegLF", CubeListBuilder.create().texOffs(24, 24).mirror().addBox(-1.0F, 0.0F, -1.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition legLB = body.addOrReplaceChild("legLB", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 1.0F, 6.5F, 0.0F, -0.1745F, 0.0F));
		PartDefinition visuallegLB = legLB.addOrReplaceChild("visuallegLB",
				CubeListBuilder.create().texOffs(24, 16).mirror().addBox(-1.0F, -1.0F, -1.5F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.1F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, 0.0F, -0.6981F));
		visuallegLB.addOrReplaceChild("forelegLB", CubeListBuilder.create().texOffs(24, 24).mirror().addBox(-1.0F, 0.0F, -1.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition wingR = body.addOrReplaceChild("wingR", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, -2.0F, 4.0F, 0.0F, 0.0F, -1.3963F));
		wingR.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(32, 0).addBox(-13.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition wingL = body.addOrReplaceChild("wingL", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, -2.0F, 4.0F, 0.0F, 0.0F, 1.3963F));
		wingL.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(32, 16).addBox(-13.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -3.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.75F, -5.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, buffer, packedLight, packedOverlay);
		head.render(poseStack, buffer, packedLight, packedOverlay);
	}
}
