package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ShrubsnapperFangEntity;
import net.minecraft.client.model.EvokerFangsModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ShrubsnapperFangRenderer extends EntityRenderer<ShrubsnapperFangEntity> {
	//Copied the evoker fang one (since the model is generic)
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/shrubsnapper_fang.png");
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("shrubsnapper_fang"), "main");
	private final EvokerFangsModel<ShrubsnapperFangEntity> model;

	public ShrubsnapperFangRenderer(EntityRendererProvider.Context p_174100_) {
		super(p_174100_);
		this.model = new EvokerFangsModel<>(p_174100_.bakeLayer(MODEL));
	}

	@Override
	public void render(ShrubsnapperFangEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		float f = entity.getAnimationProgress(partialTicks);
		if (f != 0.0F) {
			float f1 = 2.0F;
			if (f > 0.9F) {
				f1 *= (1.0F - f) / 0.1F;
			}

			poseStack.pushPose();
			poseStack.mulPose(Axis.YP.rotationDegrees(90.0F - entity.getYRot()));
			poseStack.scale(-f1, -f1, f1);
			poseStack.translate(0.0D, -0.626D, 0.0D);
			poseStack.scale(0.5F, 0.5F, 0.5F);
			this.model.setupAnim(entity, f, 0.0F, 0.0F, entity.getYRot(), entity.getXRot());
			VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(TEXTURE));
			this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			poseStack.popPose();
			super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(ShrubsnapperFangEntity p_114526_) {
		return TEXTURE;
	}
}
