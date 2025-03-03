package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.BlasterSentryEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class BlasterSentryRenderer extends MobRenderer<BlasterSentryEntity, BlasterSentryModel> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/blastersentry.png"), GLOW = ByPowderAndSteel.rl("textures/entity/blastersentry_glow.png");

	public BlasterSentryRenderer(EntityRendererProvider.Context context) {
		super(context, new BlasterSentryModel(context.bakeLayer(BlasterSentryModel.MODEL)), 0.5f);
		addLayer(new GenericGlowLayer<>(this, GLOW));
		addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(BlasterSentryEntity entity) {
		return TEXTURE;
	}
}
