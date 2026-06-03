package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import lykrast.bypowderandsteel.entity.FlamethrowerBulletEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class FlamethrowerBulletRenderer extends ThrownItemRenderer<FlamethrowerBulletEntity> {

	public FlamethrowerBulletRenderer(Context context) {
		super(context, FlamethrowerBulletEntity.SCALE, true);
	}

	@Override
	public void render(FlamethrowerBulletEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		poseStack.pushPose();
		float scale = 1;
		if (entity.tickCount < 10) scale = (entity.tickCount + partialTicks)/10f;
		poseStack.scale(scale, scale, scale);
		//not rendered until tick 2 because of super, so we do a little trolling
		entity.tickCount += 2;
		super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
		entity.tickCount -= 2;
		poseStack.popPose();
	}

}
