package lykrast.bypowderandsteel.renderer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.SunkenPirateEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class SunkenPirateRenderer extends HumanoidMobRenderer<SunkenPirateEntity, SunkenPirateModel> {
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/sunken_pirate.png"), GLOW = ByPowderAndSteel.rl("textures/entity/sunken_pirate_glow.png");
	private static final ResourceLocation BEAM_TEXTURE = ByPowderAndSteel.rl("textures/entity/sunken_pirate_laser.png");
	private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(BEAM_TEXTURE);

	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("sunken_pirate"), "main");
	public static final ModelLayerLocation INNER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("sunken_pirate"), "inner_armor");
	public static final ModelLayerLocation OUTER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("sunken_pirate"), "outer_armor");

	public SunkenPirateRenderer(EntityRendererProvider.Context context) {
		super(context, new SunkenPirateModel(context.bakeLayer(MODEL)), 0.5f);
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

	//Then I just copied that back from the Zombie Seals so might be worth cleaning more and like refactoring or something?
	@Override
	public void render(SunkenPirateEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
		LivingEntity target = entity.getActiveAttackTarget();
		if (target != null && entity.isAlive()) {
			float animTime = 0;//(entity.tickCount % 80) + partialTicks;//entity.getClientSideAttackTime() + partialTicks;
			float f2 = animTime * 0.5F % 1.0F;
			float sealEyeOffset = entity.getEyeHeight();
			poseStack.pushPose();
			poseStack.translate(0.0F, sealEyeOffset, 0.0F);
			Vec3 vec3 = getPosition(target, (target.getBbHeight()/2.0 + target.getEyeHeight())/2.0, partialTicks);
			Vec3 vec31 = getPosition(entity, sealEyeOffset, partialTicks);
			Vec3 vec32 = vec3.subtract(vec31);
			float f4 = (float) (vec32.length() + 1.0D);
			vec32 = vec32.normalize();
			float f5 = (float) Math.acos(vec32.y);
			float f6 = (float) Math.atan2(vec32.z, vec32.x);
			poseStack.mulPose(Axis.YP.rotationDegrees((Mth.HALF_PI - f6) * (180F / Mth.PI)));
			poseStack.mulPose(Axis.XP.rotationDegrees(f5 * (180F / Mth.PI)));
			float f7 = animTime * 0.05F * -1.5F;
			float f9 = 0.2F;
			float f10 = 0.282F;
			float f11 = Mth.cos(f7 + 2.3561945F) * f10;
			float f12 = Mth.sin(f7 + 2.3561945F) * f10;
			float f13 = Mth.cos(f7 + (Mth.PI / 4F)) * f10;
			float f14 = Mth.sin(f7 + (Mth.PI / 4F)) * f10;
			float f15 = Mth.cos(f7 + 3.926991F) * f10;
			float f16 = Mth.sin(f7 + 3.926991F) * f10;
			float f17 = Mth.cos(f7 + 5.4977875F) * f10;
			float f18 = Mth.sin(f7 + 5.4977875F) * f10;
			float f19 = Mth.cos(f7 + Mth.PI) * f9;
			float f20 = Mth.sin(f7 + Mth.PI) * f9;
			float f21 = Mth.cos(f7 + 0.0F) * f9;
			float f22 = Mth.sin(f7 + 0.0F) * f9;
			float f23 = Mth.cos(f7 + Mth.HALF_PI) * f9;
			float f24 = Mth.sin(f7 + Mth.HALF_PI) * f9;
			float f25 = Mth.cos(f7 + (Mth.PI * 1.5F)) * f9;
			float f26 = Mth.sin(f7 + (Mth.PI * 1.5F)) * f9;
			float f27 = 0.0F;
			float f28 = 0.4999F;
			float f29 = -1.0F + f2;
			float f30 = f4 * 2.5F + f29;
			VertexConsumer vertexconsumer = buffer.getBuffer(BEAM_RENDER_TYPE);
			PoseStack.Pose posestack$pose = poseStack.last();
			Matrix4f matrix4f = posestack$pose.pose();
			Matrix3f matrix3f = posestack$pose.normal();
			vertex(vertexconsumer, matrix4f, matrix3f, f19, f4, f20, f28, f30);
			vertex(vertexconsumer, matrix4f, matrix3f, f19, 0.0F, f20, f28, f29);
			vertex(vertexconsumer, matrix4f, matrix3f, f21, 0.0F, f22, f27, f29);
			vertex(vertexconsumer, matrix4f, matrix3f, f21, f4, f22, f27, f30);
			vertex(vertexconsumer, matrix4f, matrix3f, f23, f4, f24, f28, f30);
			vertex(vertexconsumer, matrix4f, matrix3f, f23, 0.0F, f24, f28, f29);
			vertex(vertexconsumer, matrix4f, matrix3f, f25, 0.0F, f26, f27, f29);
			vertex(vertexconsumer, matrix4f, matrix3f, f25, f4, f26, f27, f30);
			float f31 = 0.0F;
			if (entity.tickCount % 2 == 0) {
				f31 = 0.5F;
			}

			vertex(vertexconsumer, matrix4f, matrix3f, f11, f4, f12, 0.5F, f31 + 0.5F);
			vertex(vertexconsumer, matrix4f, matrix3f, f13, f4, f14, 1.0F, f31 + 0.5F);
			vertex(vertexconsumer, matrix4f, matrix3f, f17, f4, f18, 1.0F, f31);
			vertex(vertexconsumer, matrix4f, matrix3f, f15, f4, f16, 0.5F, f31);
			poseStack.popPose();
		}

	}

	private Vec3 getPosition(LivingEntity entity, double yOffset, float progress) {
		double d0 = Mth.lerp(progress, entity.xOld, entity.getX());
		double d1 = Mth.lerp(progress, entity.yOld, entity.getY()) + yOffset;
		double d2 = Mth.lerp(progress, entity.zOld, entity.getZ());
		return new Vec3(d0, d1, d2);
	}

	private static void vertex(VertexConsumer p_253637_, Matrix4f p_253920_, Matrix3f p_253881_, float p_253994_, float p_254492_, float p_254474_,
			float texX, float texY) {
		p_253637_.vertex(p_253920_, p_253994_, p_254492_, p_254474_).color(255, 255, 255, 255).uv(texX, texY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880)
				.normal(p_253881_, 0.0F, 1.0F, 0.0F).endVertex();
	}
}
