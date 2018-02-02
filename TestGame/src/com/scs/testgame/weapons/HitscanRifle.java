package com.scs.testgame.weapons;

import java.util.HashMap;

import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.scs.stevetech1.components.ICalcHitInPast;
import com.scs.stevetech1.components.ICanShoot;
import com.scs.stevetech1.components.ICausesHarmOnContact;
import com.scs.stevetech1.components.IRequiresAmmoCache;
import com.scs.stevetech1.entities.AbstractAvatar;
import com.scs.stevetech1.server.AbstractGameServer;
import com.scs.stevetech1.server.Globals;
import com.scs.stevetech1.server.RayCollisionData;
import com.scs.stevetech1.shared.IEntityController;
import com.scs.stevetech1.weapons.AbstractMagazineGun;
import com.scs.testgame.TestGameClientEntityCreator;
import com.scs.testgame.entities.DebuggingSphere;

public class HitscanRifle extends AbstractMagazineGun implements ICalcHitInPast, ICausesHarmOnContact {

	private static final float RANGE = 99f;

	public RayCollisionData hitThisMoment = null; // Only used server-side

	public HitscanRifle(IEntityController game, int id, AbstractAvatar owner, int num) {
		super(game, id, TestGameClientEntityCreator.HITSCAN_RIFLE, owner, num, "Hitscan Rifle", .2f, 1f, 10);

	}


	@Override
	public boolean launchBullet() {
		if (game.isServer()) {
			// We have already calculated the hit as part of ICalcHitInPast
			if (hitThisMoment != null) {
				//Settings.p(hitThisMoment.entity + " has been shot!");
				Vector3f pos = this.hitThisMoment.point;

				new DebuggingSphere(game, game.getNextEntityID(), pos.x, pos.y, pos.z, true);
				/*if (hitThisMoment.entity instanceof MovingTarget && Globals.DEBUG_REWIND_POS1) {
					//Settings.p(hitThisMoment.entity.name + " is at " + hitThisMoment.entity.getWorldTranslation() + " at " + hitThisMoment.timestamp);
					Globals.appendToFile("ServerMovingtarget.csv", "ServerMovingtarget," + hitThisMoment.timestamp + "," + hitThisMoment.entity.getWorldTranslation());
				}*/

				AbstractGameServer server = (AbstractGameServer)game;
				server.collisionLogic.collision(hitThisMoment.entity, this);
				this.hitThisMoment = null; // Clear it ready for next loop
			}
		} else {
			ICanShoot shooter = (ICanShoot)owner; 
			Vector3f from = shooter.getBulletStartPos();
			if (Globals.DEBUG_SHOOTING_POS) {
				Globals.p("Client shooting from " + from);
			}
			Ray ray = new Ray(from, shooter.getShootDir());
			/* todo - re-add
			RayCollisionData rcd = shooter.checkForCollisions(ray, RANGE);
			if (rcd != null) {
				Vector3f pos = rcd.point;
				Globals.p("Hit " + rcd.entity.getName() + " at " + pos);
				new DebuggingSphere(game, game.getNextEntityID(), pos.x, pos.y, pos.z, false);
			} else {
				Globals.p("Not hit anything");
			}*/
		}
		return true;
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


	@Override
	public String getAvatarAnimationCode() {
		return "Shoot";
	}


	@Override
	protected void createBullet(IEntityController game, int entityid, IRequiresAmmoCache irac, int side) {
		// Do nothing
		
	}


}
