package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.SunkenPirateEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SunkenPirateRenderer extends HumanoidMobRenderer<SunkenPirateEntity, SunkenPirateModel<SunkenPirateEntity>> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/sunken_pirate.png"), GLOW = ByPowderAndSteel.rl("textures/entity/sunken_pirate_glow.png");

	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("sunken_pirate"), "main");
	public static final ModelLayerLocation INNER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("sunken_pirate"), "inner_armor");
	public static final ModelLayerLocation OUTER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("sunken_pirate"), "outer_armor");

	public SunkenPirateRenderer(EntityRendererProvider.Context context) {
		super(context, new SunkenPirateModel<>(context.bakeLayer(MODEL)), 0.5f);
		addLayer(new GenericGlowLayer<>(this, GLOW));
		addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(SunkenPirateEntity entity) {
		return TEXTURE;
	}

	@Override
	protected void setupRotations(SunkenPirateEntity entity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks) {
		//From drowneds
		super.setupRotations(entity, stack, ageInTicks, rotationYaw, partialTicks);
		float swimAmount = entity.getSwimAmount(partialTicks);
		if (swimAmount > 0) {
			float f1 = -10.0F - entity.getXRot();
			float f2 = Mth.lerp(swimAmount, 0, f1);
			stack.rotateAround(Axis.XP.rotationDegrees(f2), 0, entity.getBbHeight() / 2f, 0);
		}

	}
}
