package com.scs.testgame;

import com.jme3.math.Vector3f;
import com.jme3.system.JmeContext;
import com.scs.simplephysics.SimpleRigidBody;
import com.scs.stevetech1.entities.AbstractAvatar;
import com.scs.stevetech1.entities.AbstractServerAvatar;
import com.scs.stevetech1.entities.PhysicalEntity;
import com.scs.stevetech1.server.AbstractSimpleGameServer;
import com.scs.stevetech1.server.ClientData;
import com.scs.stevetech1.shared.AbstractCollisionValidator;
import com.scs.testgame.entities.AnimatedWall;
import com.scs.testgame.entities.Floor;
import com.scs.testgame.entities.TestGameServerAvatar;
import com.scs.testgame.entities.Wall;

public class TestGameServer extends AbstractSimpleGameServer {

	public static final int GAME_PORT = 6143;
	public static final String GAME_ID = "Test Game";
	
	public static void main(String[] args) {
		try {
			new TestGameServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private TestGameServer() {
		super(GAME_PORT);

		start(JmeContext.Type.Headless);

	}


	@Override
	public void moveAvatarToStartPosition(AbstractAvatar avatar) {
		avatar.setWorldTranslation(new Vector3f(3f, 26f, 3f + (avatar.playerID*2)));
	}


	@Override
	protected void createGame() {
		//super.gameData = new SimpleGameData(nextGameID.getAndAdd(1));
		
		Floor floor = new Floor(this, getNextEntityID(), 0, 0, 0, 30, .5f, 30, "Textures/floor015.png", null);
		this.actuallyAddEntity(floor);
		
		//Terrain1 terrain = new Terrain1(this, getNextEntityID(), 0, 0, 0);
		//this.actuallyAddEntity(terrain);
		/*
		Crate c = new Crate(this, getNextEntityID(), 1, 30, 1, 1, 1, 1f, "Textures/crate.png", 45);
		this.actuallyAddEntity(c);
		c = new Crate(this, getNextEntityID(), 1, 30, 6, 1, 1, 1f, "Textures/crate.png", 65);
		this.actuallyAddEntity(c);
		c = new Crate(this, getNextEntityID(), 6, 30, 1, 1, 1, 1f, "Textures/crate.png", 45);
		this.actuallyAddEntity(c);
		c = new Crate(this, getNextEntityID(), 6, 30, 6, 1, 1, 1f, "Textures/crate.png", 65);
		this.actuallyAddEntity(c);*/
		
		AnimatedWall w1 = new AnimatedWall(this, getNextEntityID(), 0, 0, 0, 10, 10, 0);
		this.actuallyAddEntity(w1);
		Wall w2 = new Wall(this, getNextEntityID(), 10, 0, 0, 10, 10, "Textures/seamless_bricks/bricks2.png", 0);
		this.actuallyAddEntity(w2);
		Wall w3 = new Wall(this, getNextEntityID(), 20, 0, 0, 10, 10, "Textures/seamless_bricks/bricks2.png", 0);
		this.actuallyAddEntity(w3);
		AnimatedWall w4 = new AnimatedWall(this, getNextEntityID(), 30, 0, 0, 10, 10, 270);
		this.actuallyAddEntity(w4);

		//new MovingTarget(this, getNextEntityID(), 2, 2, 10, 1, 1, 1, "Textures/seamless_bricks/bricks2.png", 0);
		//new RoamingZombie(this, getNextEntityID(), 2, 2, 10);
		
		//House house = new House(this, getNextEntityID(), 20, 0, 20, 0);
		//this.actuallyAddEntity(house);
	}


	@Override
	protected AbstractServerAvatar createPlayersAvatarEntity(ClientData client, int entityid) {
		return new TestGameServerAvatar(this, client, client.remoteInput, entityid);
	}


	@Override
	protected byte getWinningSideAtEnd() {
		return 0;
	}


	@Override
	public void collisionOccurred(SimpleRigidBody<PhysicalEntity> a, SimpleRigidBody<PhysicalEntity> b) {
		PhysicalEntity pa = a.userObject; //pa.getMainNode().getWorldBound();
		PhysicalEntity pb = b.userObject; //pb.getMainNode().getWorldBound();

		if (pa.type == TestGameClientEntityCreator.TERRAIN1 || pb.type == TestGameClientEntityCreator.TERRAIN1) {
			//Globals.p("Collision between " + pa + " and " + pb);
		}

		super.collisionOccurred(a, b);

	}


	@Override
	public int getMinSidesRequiredForGame() {
		return 1;
	}


}
