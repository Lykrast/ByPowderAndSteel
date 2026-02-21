package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.AbeillonMonarchEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AbeillonMonarchRenderer extends MobRenderer<AbeillonMonarchEntity, AbeillonModel<AbeillonMonarchEntity>> {
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("abeillon_monarch"), "main");
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/abeillon_monarch.png");

	public AbeillonMonarchRenderer(EntityRendererProvider.Context context) {
		super(context, new AbeillonModel<>(context.bakeLayer(MODEL)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(AbeillonMonarchEntity entity) {
		return TEXTURE;
	}

	@Override
	protected float getFlipDegrees(AbeillonMonarchEntity entity) {
		return 180;
	}
}
