package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.GunnubusCrimsonEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class GunnubusCrimsonRenderer extends HumanoidMobRenderer<GunnubusCrimsonEntity, GunnubusModel<GunnubusCrimsonEntity>> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/gunnubus_crimson.png");

	public GunnubusCrimsonRenderer(EntityRendererProvider.Context context) {
		super(context, new GunnubusModel<>(context.bakeLayer(GunnubusModel.MODEL)), 0.75f);
		addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(GunnubusModel.INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(GunnubusModel.OUTER_ARMOR)),
				context.getModelManager()));
	}

	@Override
	protected void scale(GunnubusCrimsonEntity entity, PoseStack pose, float partialTick) {
		pose.scale(2f, 2f, 2f);
	}

	@Override
	public ResourceLocation getTextureLocation(GunnubusCrimsonEntity entity) {
		return TEXTURE;
	}
}
