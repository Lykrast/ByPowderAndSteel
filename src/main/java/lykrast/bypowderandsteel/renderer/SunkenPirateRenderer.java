package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.SunkenPirateEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class SunkenPirateRenderer extends HumanoidMobRenderer<SunkenPirateEntity, GunnubusModel<SunkenPirateEntity>> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/sunken_pirate.png"), GLOW = ByPowderAndSteel.rl("textures/entity/sunken_pirate_glow.png");
	
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("sunken_pirate"), "main");
	public static final ModelLayerLocation INNER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("sunken_pirate"), "inner_armor");
	public static final ModelLayerLocation OUTER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("sunken_pirate"), "outer_armor");

	public SunkenPirateRenderer(EntityRendererProvider.Context context) {
		super(context, new GunnubusModel<>(context.bakeLayer(MODEL)), 0.5f);
		addLayer(new GenericGlowLayer<>(this, GLOW));
		addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(OUTER_ARMOR)),
				context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(SunkenPirateEntity entity) {
		return TEXTURE;
	}
}
