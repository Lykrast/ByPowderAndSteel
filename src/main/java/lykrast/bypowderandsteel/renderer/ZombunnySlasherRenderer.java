package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ZombunnySlasherEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class ZombunnySlasherRenderer extends HumanoidMobRenderer<ZombunnySlasherEntity, ZombunnyModel<ZombunnySlasherEntity>> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/zombunny_slasher.png"), GLOW = ByPowderAndSteel.rl("textures/entity/zombunny_slasher_glow.png");
	
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("zombunny_slasher"), "main");
	public static final ModelLayerLocation INNER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("zombunny_slasher"), "inner_armor");
	public static final ModelLayerLocation OUTER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("zombunny_slasher"), "outer_armor");

	public ZombunnySlasherRenderer(EntityRendererProvider.Context context) {
		super(context, new ZombunnyModel<>(context.bakeLayer(MODEL)), 0.5f);
		addLayer(new GenericGlowLayer<>(this, GLOW));
		addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(OUTER_ARMOR)),
				context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(ZombunnySlasherEntity entity) {
		return TEXTURE;
	}
}
