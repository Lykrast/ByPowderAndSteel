package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ZombieSealEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class ZombieSealRenderer extends HumanoidMobRenderer<ZombieSealEntity, ZombieSealModel> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/zombie_seal.png");

	public ZombieSealRenderer(EntityRendererProvider.Context context) {
		super(context, new ZombieSealModel(context.bakeLayer(ZombieSealModel.MODEL)), 0.5f);
		addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ZombieSealModel.INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(ZombieSealModel.OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(ZombieSealEntity entity) {
		return TEXTURE;
	}
}
