package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.EnsouledSkullEntity;
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

public class EnsouledSkullModel extends EntityModel<EnsouledSkullEntity> {
	// Made with Blockbench 4.1.5 then adjusted later for animations and shit
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("ensouled_skull"), "main");
	private final ModelPart whole, head, light, jaw;
	private float animProgress;

	public EnsouledSkullModel(ModelPart root) {
		whole = root.getChild("whole");
		head = whole.getChild("head");
		light = head.getChild("light");
		jaw = whole.getChild("jaw");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition whole = partdefinition.addOrReplaceChild("whole", CubeListBuilder.create(), PartPose.offset(0, 20, 0));

		PartDefinition head = whole.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4, -7, -7, 8, 8, 8), PartPose.offset(0, 3, 3));
		head.addOrReplaceChild("light", CubeListBuilder.create().texOffs(0, 16).addBox(-4, -7, -7, 8, 6, 8, new CubeDeformation(-0.25F)), PartPose.ZERO);

		whole.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(32, 0).addBox(-4, -7, -7, 8, 8, 8, new CubeDeformation(0.25F)), PartPose.offset(0, 3, 3));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void prepareMobModel(EnsouledSkullEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
		//the super is empty
		animProgress = entity.getAnimProgress(partialTick);
		animProgress = BPASUtils.easeOutQuad(animProgress);
	}
	
	private float getHeadXRot(int anim) {
		switch (anim) {
			default:
				return 0;
			case EnsouledSkullEntity.ANIM_OPEN:
				return -10*Mth.DEG_TO_RAD;
		}
	}
	
	private float getJawXRot(int anim) {
		switch (anim) {
			default:
				return 0;
			case EnsouledSkullEntity.ANIM_OPEN:
				return 20*Mth.DEG_TO_RAD;
			case EnsouledSkullEntity.ANIM_STUN:
			case EnsouledSkullEntity.ANIM_RECOVERING:
				return 80*Mth.DEG_TO_RAD;
		}
	}

	@Override
	public void setupAnim(EnsouledSkullEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		whole.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		whole.xRot = headPitch * Mth.DEG_TO_RAD;
		light.xScale = 1;
		light.yScale = 1;
		light.zScale = 1;
		int anim = entity.clientAnim, prev = entity.prevAnim;
		if (animProgress > 0.999) {
			head.xRot = getHeadXRot(anim);
			jaw.xRot = getJawXRot(anim);
			if (anim == EnsouledSkullEntity.ANIM_STUN || anim == EnsouledSkullEntity.ANIM_RECOVERING) {
				light.xScale = 0;
				light.yScale = 0;
				light.zScale = 0;
			}
		}
		else {
			head.xRot = BPASUtils.rotlerpRad(animProgress, getHeadXRot(prev), getHeadXRot(anim));
			jaw.xRot = BPASUtils.rotlerpRad(animProgress, getJawXRot(prev), getJawXRot(anim));
			if (prev == EnsouledSkullEntity.ANIM_STUN || prev == EnsouledSkullEntity.ANIM_RECOVERING) {
				if (anim == EnsouledSkullEntity.ANIM_STUN || anim == EnsouledSkullEntity.ANIM_RECOVERING) {
					light.xScale = 0;
					light.yScale = 0;
					light.zScale = 0;
				}
				else {
					light.xScale = animProgress;
					light.yScale = animProgress;
					light.zScale = animProgress;
				}
			}
		}
		//animation util bob model part
		head.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.025F;
		jaw.xRot += Mth.sin(ageInTicks * 0.067F) * 0.025F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		whole.render(poseStack, buffer, packedLight, packedOverlay);
	}
}
