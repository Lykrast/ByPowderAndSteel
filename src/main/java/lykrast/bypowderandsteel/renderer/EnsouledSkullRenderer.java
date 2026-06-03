package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.EnsouledSkullEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EnsouledSkullRenderer extends MobRenderer<EnsouledSkullEntity, EnsouledSkullModel> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/ensouled_skull.png"), GLOW = ByPowderAndSteel.rl("textures/entity/ensouled_skull_glow.png");

	public EnsouledSkullRenderer(EntityRendererProvider.Context context) {
		super(context, new EnsouledSkullModel(context.bakeLayer(EnsouledSkullModel.MODEL)), 0.25f);
		addLayer(new GenericGlowLayer<>(this, GLOW));
		//TODO armor?
	}

	@Override
	public ResourceLocation getTextureLocation(EnsouledSkullEntity entity) {
		return TEXTURE;
	}

	@Override
	protected float getFlipDegrees(EnsouledSkullEntity entity) {
		return 180;
	}

	@Override
	protected boolean isShaking(EnsouledSkullEntity entity) {
		return super.isShaking(entity) || entity.clientAnim == EnsouledSkullEntity.ANIM_RECOVERING;
	}
}
