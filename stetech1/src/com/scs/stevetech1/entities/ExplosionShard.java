package com.scs.stevetech1.entities;

import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.simplephysics.SimpleRigidBody;
import com.scs.stevetech1.client.IClientApp;
import com.scs.stevetech1.components.IProcessByClient;
import com.scs.stevetech1.server.Globals;
import com.scs.stevetech1.shared.IEntityController;

import ssmith.lang.NumberFunctions;

public class ExplosionShard extends PhysicalEntity implements IProcessByClient {

	private float timeLeft = 1.5f;
	
	public ExplosionShard(IEntityController _game, float x, float y, float z, float minForce, float maxForce) {
		super(_game, _game.getNextEntityID(), Globals.BULLET_EXPLOSION_EFFECT, "ExplosionShard", true, false, true);

		float s = .05f;
		Box box1 = new Box(s, s, s);
		Geometry geometry = new Geometry("Crate", box1);
		TextureKey key3 = new TextureKey("Textures/sun.jpg");
		key3.setGenerateMips(true);
		Texture tex3 = game.getAssetManager().loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);

		Material floor_mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		floor_mat.setTexture("DiffuseMap", tex3);
		geometry.setMaterial(floor_mat);
		//floor_mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		//geometry.setQueueBucket(Bucket.Transparent);

		this.mainNode.attachChild(geometry);
		int rotDegreesX = NumberFunctions.rnd(0,365);
		float radsX = (float)Math.toRadians(rotDegreesX);
		int rotDegreesY = NumberFunctions.rnd(0,365);
		float radsY = (float)Math.toRadians(rotDegreesY);
		mainNode.rotate(radsX, radsY, 0);
		mainNode.setLocalTranslation(x, y, z);

		geometry.setUserData(Globals.ENTITY, this);
		mainNode.setUserData(Globals.ENTITY, this);

		this.simpleRigidBody = new SimpleRigidBody<PhysicalEntity>(this, game.getPhysicsController(), true, this);
		simpleRigidBody.setBounciness(.0001f);
		//simpleRigidBody.setCollidable(false);
		Vector3f forceDirection = new Vector3f(NumberFunctions.rndFloat(-1, 1), NumberFunctions.rndFloat(1, 2), NumberFunctions.rndFloat(-1, 1));
		float force = NumberFunctions.rndFloat(minForce,  maxForce);
		simpleRigidBody.setAdditionalForce(forceDirection.multLocal(force));

	}


	@Override
	public void processByClient(IClientApp client, float tpf_secs) {
		this.simpleRigidBody.process(tpf_secs);
		//Settings.p("Pos: " + this.getLocation());
		timeLeft -= tpf_secs;
		if (timeLeft <= 0) {
			this.remove();
		}
	}


}
