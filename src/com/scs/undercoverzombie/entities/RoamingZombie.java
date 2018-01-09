package com.scs.undercoverzombie.entities;

import java.util.HashMap;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.JmeContext;
import com.scs.simplephysics.SimpleRigidBody;
import com.scs.stevetech1.client.AbstractGameClient;
import com.scs.stevetech1.components.IAffectedByPhysics;
import com.scs.stevetech1.components.IAnimated;
import com.scs.stevetech1.components.IDamagable;
import com.scs.stevetech1.components.INotifiedOfCollision;
import com.scs.stevetech1.components.IProcessByClient;
import com.scs.stevetech1.entities.PhysicalEntity;
import com.scs.stevetech1.netmessages.EntityUpdateMessage;
import com.scs.stevetech1.server.AbstractGameServer;
import com.scs.stevetech1.server.Globals;
import com.scs.stevetech1.shared.ChronoList;
import com.scs.stevetech1.shared.HistoricalAnimationData;
import com.scs.stevetech1.shared.IEntityController;
import com.scs.testgame.TestGameClientEntityCreator;
import com.scs.undercoverzombie.models.ZombieModel;

public class RoamingZombie extends PhysicalEntity implements IAffectedByPhysics, IDamagable, INotifiedOfCollision, IProcessByClient, IAnimated {
	
	private static final float w = 1f; // todo
	private static final float d = 1f;
	private static final float h = 1f;

	//private static final float DURATION = 3;
	private static final float SPEED = 2;

	private Spatial spatial = null;
	private ZombieModel zm;
	
	private Vector3f currDir = new Vector3f(1f, 0, 0);
	//private float timeUntilTurn = DURATION;
	public ChronoList<HistoricalAnimationData> animList = new ChronoList<HistoricalAnimationData>(true, -1);

	public RoamingZombie(IEntityController _game, int id, float x, float y, float z) {
		super(_game, id, TestGameClientEntityCreator.ZOMBIE, "Zombie");

		if (_game.isServer()) {
			creationData = new HashMap<String, Object>();
		}

		if (_game.getJmeContext() != JmeContext.Type.Headless) { // !_game.isServer()) { // Not running in server
			//spatial = game.getAssetManager().loadModel("Models/zombie/Zombie.blend");
			//JMEFunctions.SetTextureOnSpatial(game.getAssetManager(), spatial, "Models/zombie/ZombieTexture.png");
			zm = new ZombieModel(game.getAssetManager());
			spatial = zm.getModel();
		} else {
			// Server
			Box box1 = new Box(w/2, h/2, d/2);
			spatial = new Geometry("Crate", box1);
		}
		this.mainNode.attachChild(spatial); //This creates the model bounds!
		spatial.setLocalTranslation(0, h/2, 0);
		mainNode.setLocalTranslation(x, y, z);
		game.getRootNode().attachChild(this.mainNode);

		this.simpleRigidBody = new SimpleRigidBody<PhysicalEntity>(this.mainNode, game.getPhysicsController(), this);

		spatial.setUserData(Globals.ENTITY, this);
		mainNode.setUserData(Globals.ENTITY, this);

		game.addEntity(this);

	}


	@Override
	public void processByServer(AbstractGameServer server, float tpf_secs) {
		// todo
		//this.spatial.lookAt(position, upVector);
		this.simpleRigidBody.setAdditionalForce(this.currDir.mult(SPEED)); // this.getMainNode();
		this.currentAnim = "ZombieWalk";
		super.processByServer(server, tpf_secs);
		//Settings.p("Pos: " + this.getWorldTranslation());
	}


	@Override
	public void fallenOffEdge() {
		this.respawn();
	}


	private void respawn() {
		this.setWorldTranslation(new Vector3f(10, 10, 10));

		EntityUpdateMessage eum = new EntityUpdateMessage();
		eum.addEntityData(this, true);
		AbstractGameServer server = (AbstractGameServer)this.game;
		server.networkServer.sendMessageToAll(eum);

	}
	
	
	@Override
	public void damaged(float amt, String reason) {
		this.currentAnim = "ZombieBite";
	}


	@Override
	public int getSide() {
		return 0;
	}


	@Override
	public void collided(PhysicalEntity pe) {
		// TODO turn around?
		
	}


	@Override
	public void processByClient(AbstractGameClient client, float tpf_secs) {
		HistoricalAnimationData had = this.animList.calcPosition(client.serverTimePast, true);
		if (had != null) {
			if (!had.animation.equals(this.currentAnim)) {
				this.currentAnim = had.animation;
				this.zm.channel.setAnim(had.animation);
			}
		}		
	}


	@Override
	public void addAnim(HistoricalAnimationData had) {
		this.animList.addData(had);
		
	}

}
