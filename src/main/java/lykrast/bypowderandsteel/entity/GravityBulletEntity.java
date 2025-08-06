package lykrast.bypowderandsteel.entity;

import lykrast.bypowderandsteel.registry.BPASEntities;
import lykrast.gunswithoutroses.entity.BulletEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GravityBulletEntity extends BulletEntity {
	public GravityBulletEntity(EntityType<? extends BulletEntity> type, Level level) {
		super(type, level);
	}

	public GravityBulletEntity(Level level, LivingEntity shooter) {
		super(BPASEntities.gravityBullet.get(), shooter, level);
	}
	
	//putting those here so I can tweak easily
	private static final double FRICTION = 0.9, GRAVITY = 0.2;
	
	@Override
	public void tick() {
		super.tick();
		Vec3 delta = getDeltaMovement();
		//saving one object allocation here by mathing direction instead of doing .mulitiply and stuff
		//I mean would that change much? not sure but it makes me feel better
		setDeltaMovement(new Vec3(delta.x*FRICTION, delta.y*FRICTION-GRAVITY, delta.z*FRICTION));
		//bullet get disappeared below total squared velocity of 0.01 and I didn't put an easy way to bypass that so a little manual check
		if (getDeltaMovement().lengthSqr() < 0.01) setDeltaMovement(getDeltaMovement().add(0, -0.2, 0));
	}
}
