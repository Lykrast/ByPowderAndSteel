package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.HeadlessSkeletonEntity;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class HeadlessSkeletonRenderer extends HumanoidMobRenderer<HeadlessSkeletonEntity, HeadlessSkeletonModel> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/headless_skeleton.png");

	public HeadlessSkeletonRenderer(EntityRendererProvider.Context context) {
		super(context, new HeadlessSkeletonModel(context.bakeLayer(HeadlessSkeletonModel.MODEL)), 0.5f);
		addLayer(new HumanoidArmorLayer<>(this, new SkeletonModel<>(context.bakeLayer(HeadlessSkeletonModel.INNER_ARMOR)), new SkeletonModel<>(context.bakeLayer(HeadlessSkeletonModel.OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(HeadlessSkeletonEntity entity) {
		return TEXTURE;
	}
}
