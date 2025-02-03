package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.MechaminatorEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class MechaminatorRenderer extends MobRenderer<MechaminatorEntity, MechaminatorModel> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/mechaminator.png"), GLOW = ByPowderAndSteel.rl("textures/entity/mechaminator_glow.png");

	public MechaminatorRenderer(EntityRendererProvider.Context context) {
		super(context, new MechaminatorModel(context.bakeLayer(MechaminatorModel.MODEL)), 0.5f);
		addLayer(new GenericGlowLayer<>(this, GLOW));
		addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(MechaminatorEntity entity) {
		return TEXTURE;
	}
}
