package lykrast.bypowderandsteel.renderer;

import java.util.Locale;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.GunomeEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class GunomeRenderer extends MobRenderer<GunomeEntity, GunomeModel> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/gunome.png"), YURF = ByPowderAndSteel.rl("textures/entity/gunome_yurf.png");

	public GunomeRenderer(EntityRendererProvider.Context context) {
		super(context, new GunomeModel(context.bakeLayer(GunomeModel.MODEL)), 0.375f);
		addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(GunomeEntity entity) {
		String name = ChatFormatting.stripFormatting(entity.getName().getString()).toLowerCase(Locale.ROOT);
		if ("yurf".equals(name)) return YURF;
		return TEXTURE;
	}
}
