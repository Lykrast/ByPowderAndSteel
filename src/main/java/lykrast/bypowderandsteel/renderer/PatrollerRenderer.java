package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.PatrollerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PatrollerRenderer extends MobRenderer<PatrollerEntity, PatrollerModel> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/patroller.png"), GLOW = ByPowderAndSteel.rl("textures/entity/patroller_glow.png");

	public PatrollerRenderer(EntityRendererProvider.Context context) {
		super(context, new PatrollerModel(context.bakeLayer(PatrollerModel.MODEL)), 0.5f);
		addLayer(new GenericGlowLayer<>(this, GLOW));
	}

	@Override
	public ResourceLocation getTextureLocation(PatrollerEntity entity) {
		return TEXTURE;
	}

	@Override
	protected float getFlipDegrees(PatrollerEntity entity) {
		return 180;
	}
}
