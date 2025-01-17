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
	public void render(ShrubsnapperFangEntity p_114528_, float p_114529_, float p_114530_, PoseStack p_114531_, MultiBufferSource p_114532_, int p_114533_) {
		float f = p_114528_.getAnimationProgress(p_114530_);
		if (f != 0.0F) {
			float f1 = 2.0F;
			if (f > 0.9F) {
				f1 *= (1.0F - f) / 0.1F;
			}

			p_114531_.pushPose();
			p_114531_.mulPose(Axis.YP.rotationDegrees(90.0F - p_114528_.getYRot()));
			p_114531_.scale(-f1, -f1, f1);
			p_114531_.translate(0.0D, -0.626D, 0.0D);
			p_114531_.scale(0.5F, 0.5F, 0.5F);
			this.model.setupAnim(p_114528_, f, 0.0F, 0.0F, p_114528_.getYRot(), p_114528_.getXRot());
			VertexConsumer vertexconsumer = p_114532_.getBuffer(this.model.renderType(TEXTURE));
			this.model.renderToBuffer(p_114531_, vertexconsumer, p_114533_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			p_114531_.popPose();
			super.render(p_114528_, p_114529_, p_114530_, p_114531_, p_114532_, p_114533_);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(ShrubsnapperFangEntity p_114526_) {
		return TEXTURE;
	}
}
