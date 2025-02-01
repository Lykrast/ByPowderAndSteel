package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.SaberSentryEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SaberSentryRenderer extends MobRenderer<SaberSentryEntity, SaberSentryModel> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/sabersentry.png"), GLOW = ByPowderAndSteel.rl("textures/entity/sabersentry_glow.png");

	public SaberSentryRenderer(EntityRendererProvider.Context context) {
		super(context, new SaberSentryModel(context.bakeLayer(SaberSentryModel.MODEL)), 0.7f);
		addLayer(new GenericGlowLayer<>(this, GLOW));
	}

	@Override
	protected void scale(SaberSentryEntity entity, PoseStack pose, float partialTick) {
		pose.scale(1.5f, 1.5f, 1.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(SaberSentryEntity entity) {
		return TEXTURE;
	}
}
