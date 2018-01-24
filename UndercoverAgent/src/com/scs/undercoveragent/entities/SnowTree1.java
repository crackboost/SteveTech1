package com.scs.undercoveragent.entities;

import java.util.HashMap;

import com.jme3.scene.Spatial;
import com.scs.simplephysics.SimpleRigidBody;
import com.scs.stevetech1.entities.PhysicalEntity;
import com.scs.stevetech1.jme.JMEFunctions;
import com.scs.stevetech1.server.AbstractGameServer;
import com.scs.stevetech1.server.Globals;
import com.scs.stevetech1.shared.IEntityController;
import com.scs.undercoveragent.UndercoverAgentClientEntityCreator;

public class SnowTree1 extends PhysicalEntity {

	public SnowTree1(IEntityController _game, int id, float x, float y, float z, float rotDegrees) {
		super(_game, id, UndercoverAgentClientEntityCreator.SNOW_TREE_1, "SnowTree1", false);

		if (_game.isServer()) {
			creationData = new HashMap<String, Object>();
			creationData.put("rot", rotDegrees);
		}

		Spatial model = game.getAssetManager().loadModel("Models/SnowNature/Tree.blend");
		JMEFunctions.SetTextureOnSpatial(game.getAssetManager(), model, "Models/SnowNature/Textures/TreeTexture.png");

		this.mainNode.attachChild(model); //This creates the model bounds!
		
		float rads = (float)Math.toRadians(rotDegrees);
		mainNode.rotate(0, rads, 0);
		
		mainNode.setLocalTranslation(x, y, z);
		game.getRootNode().attachChild(this.mainNode);

		this.simpleRigidBody = new SimpleRigidBody<PhysicalEntity>(this.mainNode, game.getPhysicsController(), false, this);

		model.setUserData(Globals.ENTITY, this);
		mainNode.setUserData(Globals.ENTITY, this);

		game.addEntity(this);

	}


	@Override
	public void processByServer(AbstractGameServer server, float tpf) {
		super.processByServer(server, tpf);
	}

}
