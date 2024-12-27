package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.GunomeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class GunomeRenderer extends MobRenderer<GunomeEntity, GunomeModel> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/gunome.png");

	public GunomeRenderer(EntityRendererProvider.Context context) {
		super(context, new GunomeModel(context.bakeLayer(GunomeModel.MODEL)), 0.375f);
		addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(GunomeEntity entity) {
		return TEXTURE;
	}
}
