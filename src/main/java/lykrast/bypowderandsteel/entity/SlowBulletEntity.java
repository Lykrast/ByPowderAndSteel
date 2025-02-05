package lykrast.bypowderandsteel.entity;

import lykrast.bypowderandsteel.registry.BPASEntities;
import lykrast.gunswithoutroses.entity.BulletEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SlowBulletEntity extends BulletEntity {
	public SlowBulletEntity(EntityType<? extends BulletEntity> type, Level level) {
		super(type, level);
	}

	public SlowBulletEntity(Level level, LivingEntity shooter) {
		super(BPASEntities.slowBullet.get(), shooter, level);
	}
	
	@Override
	public void shoot(double dirx, double diry, double dirz, float speed, float spread) {
		if (speed > 1) speed = 1;
		super.shoot(dirx, diry, dirz, speed, spread);
	}
}
