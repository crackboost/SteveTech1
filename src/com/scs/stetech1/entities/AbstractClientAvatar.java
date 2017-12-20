package com.scs.stetech1.entities;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.scs.simplephysics.SimpleCharacterControl;
import com.scs.stetech1.client.AbstractGameClient;
import com.scs.stetech1.client.HistoricalPositionCalculator;
import com.scs.stetech1.client.syncposition.ICorrectClientEntityPosition;
import com.scs.stetech1.client.syncposition.InstantPositionAdjustment;
import com.scs.stetech1.components.IEntity;
import com.scs.stetech1.components.IProcessByClient;
import com.scs.stetech1.components.IShowOnHUD;
import com.scs.stetech1.hud.HUD;
import com.scs.stetech1.input.IInputDevice;
import com.scs.stetech1.netmessages.AbilityUpdateMessage;
import com.scs.stetech1.server.AbstractGameServer;
import com.scs.stetech1.server.Settings;
import com.scs.stetech1.shared.EntityPositionData;
import com.scs.stetech1.shared.IAbility;
import com.scs.stetech1.shared.PositionCalculator;

public abstract class AbstractClientAvatar extends AbstractAvatar implements IShowOnHUD, IProcessByClient {

	public HUD hud;
	public Camera cam;
	private ICorrectClientEntityPosition syncPos;
	public PositionCalculator clientAvatarPositionData = new PositionCalculator(true, 500); // So we know where we were in the past to compare against where the server says we should have been
	
	public AbstractClientAvatar(AbstractGameClient _module, int _playerID, IInputDevice _input, Camera _cam, HUD _hud, int eid, float x, float y, float z, int side) {
		super(_module, _playerID, _input, eid, side);

		cam = _cam;
		hud = _hud;

		this.setWorldTranslation(new Vector3f(x, y, z));

		syncPos = new InstantPositionAdjustment();
		//syncPos = new MoveSlowlyToCorrectPosition();
		//syncPos = new AdjustBasedOnDistance();

		this.simpleRigidBody.setGravity(0);

		_module.avatar = this;
		
	}


	@Override
	public void processByClient(AbstractGameClient client, float tpf_secs) {
		final long serverTime = System.currentTimeMillis() + client.clientToServerDiffTime;

		storeAvatarPosition(serverTime);

		super.serverAndClientProcess(null, client, tpf_secs);

/*		if (simpleRigidBody != null) {
			simpleRigidBody.process(tpf_secs);
		}
*/
		hud.processByClient(client, tpf_secs);

		// Position camera at node
		Vector3f vec = this.getWorldTranslation();
		cam.getLocation().x = vec.x;
		cam.getLocation().y = vec.y + PLAYER_HEIGHT;
		cam.getLocation().z = vec.z;
		cam.update();

		// Rotate us to point in the direction of the camera
		Vector3f lookAtPoint = cam.getLocation().add(cam.getDirection().mult(10));
		lookAtPoint.y = cam.getLocation().y; // Look horizontal
		//todo -re-add? But rotating spatial makes us stick to the floor   this.playerGeometry.lookAt(lookAtPoint, Vector3f.UNIT_Y);

	}


	public void storeAvatarPosition(long serverTime) {
		// Store our position
		//EntityPositionData epd = new EntityPositionData(getWorldTranslation().clone(), null, serverTime);
		this.clientAvatarPositionData.addPositionData(getWorldTranslation(), null, serverTime);

	}


	// Avatars have their own special position calculator
	@Override
	public void calcPosition(AbstractGameClient mainApp, long serverTimeToUse) {
		SimpleCharacterControl<PhysicalEntity> simplePlayerControl = (SimpleCharacterControl<PhysicalEntity>)this.simpleRigidBody; 
		//scs new simplePlayerControl.getAdditionalForce().set(0, 0, 0);
		if (Settings.SYNC_CLIENT_POS) {
			Vector3f offset = HistoricalPositionCalculator.calcHistoricalPositionOffset(serverPositionData, clientAvatarPositionData, serverTimeToUse, mainApp.pingRTT/2);
			if (offset != null) {
				this.syncPos.adjustPosition(this, offset);
			}
		}
	}


	@Override
	public void hasSuccessfullyHit(IEntity e) {
		// Do nothing - done server-side
	}


	@Override
	public Vector3f getShootDir() {
		return this.cam.getDirection();
	}


	public Camera getCamera() {
		return this.cam;
	}


	public FrustumIntersect getInsideOutside(PhysicalEntity entity) {
		FrustumIntersect insideoutside = cam.contains(entity.getMainNode().getWorldBound());
		return insideoutside;
	}


	@Override
	public void processByServer(AbstractGameServer server, float tpf_secs) {
		// Do nothing

	}


}