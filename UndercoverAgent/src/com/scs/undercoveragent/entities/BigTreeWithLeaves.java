package com.scs.undercoveragent.entities;

import java.util.HashMap;

import com.jme3.collision.Collidable;
import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.scs.simplephysics.SimpleRigidBody;
import com.scs.stevetech1.entities.PhysicalEntity;
import com.scs.stevetech1.jme.JMEModelFunctions;
import com.scs.stevetech1.server.AbstractGameServer;
import com.scs.stevetech1.server.Globals;
import com.scs.stevetech1.shared.IEntityController;
import com.scs.undercoveragent.UndercoverAgentClientEntityCreator;

public class BigTreeWithLeaves extends PhysicalEntity {

	public BigTreeWithLeaves(IEntityController _game, int id, float x, float y, float z, Quaternion q) {
		super(_game, id, UndercoverAgentClientEntityCreator.BIG_TREE_WITH_LEAVES, "BigTreeWithLeaves", false, true, false);

		if (_game.isServer()) {
			creationData = new HashMap<String, Object>();
			//creationData.put("q", q);
		}

		Spatial model = game.getAssetManager().loadModel("Models/SnowNature/BigTreeWithLeaves.blend");
		if (!_game.isServer()) {
			JMEModelFunctions.setTextureOnSpatial(game.getAssetManager(), model, "Models/SnowNature/Textures/TreeTexture.png");
			model.setShadowMode(ShadowMode.CastAndReceive);
		}
		this.mainNode.attachChild(model); //This creates the model bounds!

		//float rads = (float)Math.toRadians(rotDegrees);
		//mainNode.rotate(0, rads, 0);
		mainNode.setLocalRotation(q);

		mainNode.setLocalTranslation(x, y, z);

		this.simpleRigidBody = new SimpleRigidBody<PhysicalEntity>(this, game.getPhysicsController(), false, this);
		simpleRigidBody.setModelComplexity(3);
		simpleRigidBody.setNeverMoves(true);

		model.setUserData(Globals.ENTITY, this);
		mainNode.setUserData(Globals.ENTITY, this);

		//game.getRootNode().attachChild(this.mainNode);
		//game.addEntity(this);

	}


	@Override
	public void processByServer(AbstractGameServer server, float tpf) {
		super.processByServer(server, tpf);
	}


	@Override
	public Collidable getCollidable() {
		return this.mainNode;
	}


}
