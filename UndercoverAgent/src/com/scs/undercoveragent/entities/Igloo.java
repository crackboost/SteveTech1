package com.scs.undercoveragent.entities;

import java.util.HashMap;
import java.util.concurrent.Callable;

import com.jme3.collision.Collidable;
import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.scs.simplephysics.SimpleRigidBody;
import com.scs.stevetech1.entities.PhysicalEntity;
import com.scs.stevetech1.server.Globals;
import com.scs.stevetech1.shared.IEntityController;
import com.scs.undercoveragent.UndercoverAgentClientEntityCreator;

public class Igloo extends PhysicalEntity {

	public Igloo(IEntityController _game, int id, float x, float y, float z, Quaternion q) {
		super(_game, id, UndercoverAgentClientEntityCreator.IGLOO, "Igloo", false, true, false);

		if (_game.isServer()) {
			creationData = new HashMap<String, Object>();
		}

		Spatial model = game.getAssetManager().loadModel("Models/Holiday/Igloo.blend");
		if (_game.isServer()) {
			model.setShadowMode(ShadowMode.CastAndReceive);
		}
		this.mainNode.attachChild(model); //This creates the model bounds!  mainNode.getWorldBound();

		mainNode.setLocalRotation(q);
		mainNode.setLocalTranslation(x, y, z);

		this.simpleRigidBody = new SimpleRigidBody<PhysicalEntity>(this, game.getPhysicsController(), false, this);
		simpleRigidBody.setModelComplexity(3);
		simpleRigidBody.setNeverMoves(true);

		model.setUserData(Globals.ENTITY, this);
	}

	
	@Override
	public Collidable getCollidable() {
		return this.mainNode;
	}


}
