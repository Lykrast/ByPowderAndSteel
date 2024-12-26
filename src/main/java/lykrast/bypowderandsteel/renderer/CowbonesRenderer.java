package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.CowbonesEntity;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class CowbonesRenderer extends HumanoidMobRenderer<CowbonesEntity, CowbonesModel> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/cowbones.png");

	public CowbonesRenderer(EntityRendererProvider.Context context) {
		super(context, new CowbonesModel(context.bakeLayer(CowbonesModel.MODEL)), 0.5f);
		addLayer(new HumanoidArmorLayer<>(this, new SkeletonModel<>(context.bakeLayer(CowbonesModel.INNER_ARMOR)), new SkeletonModel<>(context.bakeLayer(CowbonesModel.OUTER_ARMOR)), context.getModelManager()));
		addLayer(new CowbonesOverlayLayer<>(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(CowbonesEntity entity) {
		return TEXTURE;
	}
}
