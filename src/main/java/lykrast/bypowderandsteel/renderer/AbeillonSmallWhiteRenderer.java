package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.AbeillonSmallWhiteEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AbeillonSmallWhiteRenderer extends MobRenderer<AbeillonSmallWhiteEntity, AbeillonModel<AbeillonSmallWhiteEntity>> {
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("abeillon_small_white"), "main");
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/abeillon_small_white.png");

	public AbeillonSmallWhiteRenderer(EntityRendererProvider.Context context) {
		super(context, new AbeillonModel<>(context.bakeLayer(MODEL)), 0.8f);
	}

	@Override
	public ResourceLocation getTextureLocation(AbeillonSmallWhiteEntity entity) {
		return TEXTURE;
	}

	@Override
	protected float getFlipDegrees(AbeillonSmallWhiteEntity entity) {
		return 180;
	}
}
