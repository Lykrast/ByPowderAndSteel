package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.SkybenderEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SkybenderRenderer extends MobRenderer<SkybenderEntity, SkybenderModel> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/skybender.png"), GLOW = ByPowderAndSteel.rl("textures/entity/skybender_glow.png");

	public SkybenderRenderer(EntityRendererProvider.Context context) {
		//TODO transparent glowing shield
		super(context, new SkybenderModel(context.bakeLayer(SkybenderModel.MODEL)), 0.5f);
		addLayer(new GenericGlowLayer<>(this, GLOW));
		//addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(SkybenderEntity entity) {
		return TEXTURE;
	}
}
