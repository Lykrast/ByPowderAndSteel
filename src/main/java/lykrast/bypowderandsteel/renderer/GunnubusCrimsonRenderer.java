package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.GunnubusCrimsonEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class GunnubusCrimsonRenderer extends HumanoidMobRenderer<GunnubusCrimsonEntity, GunnubusModel<GunnubusCrimsonEntity>> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/gunnubus_crimson.png");
	
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("gunnubus_crimson"), "main");
	public static final ModelLayerLocation INNER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("gunnubus_crimson"), "inner_armor");
	public static final ModelLayerLocation OUTER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("gunnubus_crimson"), "outer_armor");

	public GunnubusCrimsonRenderer(EntityRendererProvider.Context context) {
		super(context, new GunnubusModel<>(context.bakeLayer(MODEL)), 0.75f);
		addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(OUTER_ARMOR)),
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
