package com.scs.stetech1.weapons;

import java.util.HashMap;

import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.scs.stetech1.components.ICalcHitInPast;
import com.scs.stetech1.components.ICanShoot;
import com.scs.stetech1.components.ICausesHarmOnContact;
import com.scs.stetech1.entities.AbstractAvatar;
import com.scs.stetech1.entities.DebuggingSphere;
import com.scs.stetech1.server.AbstractGameServer;
import com.scs.stetech1.server.RayCollisionData;
import com.scs.stetech1.server.Settings;
import com.scs.stetech1.shared.IEntityController;
import com.scs.testgame.TestGameEntityCreator;
import com.scs.testgame.entities.MovingTarget;

public class HitscanRifle extends AbstractMagazineGun implements ICalcHitInPast, ICausesHarmOnContact {

	private static final float RANGE = 99f;

	public RayCollisionData hitThisMoment = null; // Only used server-side

	public HitscanRifle(IEntityController game, int id, AbstractAvatar owner, int num) {
		super(game, id, TestGameEntityCreator.HITSCAN_RIFLE, owner, num, "Hitscan Rifle", .2f, 1f, 10);

	}


	@Override
	public void launchBullet() {
		if (game.isServer()) {
			// We have already calculated the hit as part of ICalcHitInPast
			if (hitThisMoment != null) {
				//Settings.p(hitThisMoment.entity + " has been shot!");
				Vector3f pos = this.hitThisMoment.point;

				new DebuggingSphere(game, game.getNextEntityID(), pos.x, pos.y, pos.z, true);
				if (hitThisMoment.entity instanceof MovingTarget && Settings.DEBUG_REWIND_POS1) {
					//Settings.p(hitThisMoment.entity.name + " is at " + hitThisMoment.entity.getWorldTranslation() + " at " + hitThisMoment.timestamp);
					Settings.appendToFile("ServerMovingtarget.csv", "ServerMovingtarget," + hitThisMoment.timestamp + "," + hitThisMoment.entity.getWorldTranslation());
				}

				AbstractGameServer server = (AbstractGameServer)game;
				server.collisionLogic.collision(hitThisMoment.entity, this);
				this.hitThisMoment = null; // Clear it ready for next loop
			}
		} else {
			// todo - nozzle flash or something
			ICanShoot shooter = (ICanShoot)owner; 
			Vector3f from = shooter.getBulletStartPos();
			if (Settings.DEBUG_SHOOTING_POS) {
				Settings.p("Client shooting from " + from);
			}
			Ray ray = new Ray(from, shooter.getShootDir());
			RayCollisionData rcd = shooter.checkForCollisions(ray, RANGE);
			if (rcd != null) {
				Vector3f pos = rcd.point;
				Settings.p("Hit " + rcd.entity.getName() + " at " + pos);
				new DebuggingSphere(game, game.getNextEntityID(), pos.x, pos.y, pos.z, false);
				if (rcd.entity instanceof MovingTarget && Settings.DEBUG_REWIND_POS1) {
					//Settings.p("Moving target hit at " + rcd.entity.getWorldTranslation());
					Settings.appendToFile("ClientMovingtarget.csv", "ClientMovingTarget," + (System.currentTimeMillis()-Settings.CLIENT_RENDER_DELAY) + "," + rcd.entity.getWorldTranslation());

				}
			} else {
				Settings.p("Not hit anything");
			}
		}

	}


	@Override
	public void setTarget(RayCollisionData hd) {
		this.hitThisMoment = hd;

	}


	@Override
	public float getRange() {
		return RANGE;
	}


	@Override
	public float getDamageCaused() {
		return 1;
	}


	@Override
	public int getSide() {
		return this.owner.getSide();
	}


	@Override
	public HashMap<String, Object> getCreationData() {
		return super.creationData;
	}


}
