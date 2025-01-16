package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.ShrubhulkEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ShrubhulkRenderer extends MobRenderer<ShrubhulkEntity, ShrubhulkModel> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/shrubhulk.png"), GLOW = ByPowderAndSteel.rl("textures/entity/shrubhulk_glow.png");

	public ShrubhulkRenderer(EntityRendererProvider.Context context) {
		super(context, new ShrubhulkModel(context.bakeLayer(ShrubhulkModel.MODEL)), 0.7f);
		addLayer(new GenericGlowLayer<>(this, GLOW));
	}

	@Override
	protected void scale(ShrubhulkEntity entity, PoseStack pose, float partialTick) {
		pose.scale(1.25f, 1.25f, 1.25f);
	}

	@Override
	public ResourceLocation getTextureLocation(ShrubhulkEntity entity) {
		return TEXTURE;
	}
}
