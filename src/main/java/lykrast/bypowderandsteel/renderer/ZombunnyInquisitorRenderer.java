package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ZombunnyInquisitorEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class ZombunnyInquisitorRenderer extends HumanoidMobRenderer<ZombunnyInquisitorEntity, ZombunnyInquisitorModel> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/zombunny_inquisitor.png"), GLOW = ByPowderAndSteel.rl("textures/entity/zombunny_inquisitor_glow.png");

	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("zombunny_inquisitor"), "main");
	public static final ModelLayerLocation INNER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("zombunny_inquisitor"), "inner_armor");
	public static final ModelLayerLocation OUTER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("zombunny_inquisitor"), "outer_armor");

	public ZombunnyInquisitorRenderer(EntityRendererProvider.Context context) {
		super(context, new ZombunnyInquisitorModel(context.bakeLayer(MODEL)), 0.5f);
		addLayer(new GenericGlowLayer<>(this, GLOW));
		addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(ZombunnyInquisitorEntity entity) {
		return TEXTURE;
	}
}
