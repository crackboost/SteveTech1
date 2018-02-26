package com.scs.testgame;

import java.io.IOException;

import com.jme3.math.Vector3f;
import com.jme3.system.JmeContext;
import com.scs.stevetech1.data.GameOptions;
import com.scs.stevetech1.entities.AbstractAvatar;
import com.scs.stevetech1.entities.AbstractServerAvatar;
import com.scs.stevetech1.server.AbstractGameServer;
import com.scs.stevetech1.server.ClientData;
import com.scs.testgame.entities.Floor;
import com.scs.testgame.entities.House;
import com.scs.testgame.entities.TestGameServerAvatar;
import com.scs.testgame.entities.Wall;

public class TestGameServer extends AbstractGameServer {

	public static void main(String[] args) {
		try {
			AbstractGameServer app = new TestGameServer();
			app.setPauseOnLostFocus(false);
			app.start(JmeContext.Type.Headless);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public TestGameServer() throws IOException {
		super(new GameOptions("Test Game", 1, 999, 10*1000, 5*60*1000, 10*1000, TestGameStaticData.GAME_IP_ADDRESS, TestGameStaticData.GAME_PORT, TestGameStaticData.LOBBY_IP_ADDRESS, TestGameStaticData.LOBBY_PORT, 5, 5),
				25, 40, 200, 10000, -5f, 0.99f);
	}


	@Override
	public void moveAvatarToStartPosition(AbstractAvatar avatar) {
		avatar.setWorldTranslation(new Vector3f(3f, 0.6f, 3f + (avatar.playerID*2)));
	}


	protected void createGame() {
		new Floor(this, getNextEntityID(), 0, 0, 0, 30, .5f, 30, "Textures/floor015.png", null);
		//new Crate(this, getNextEntityID(), 8, 2, 8, 1, 1, 1f, "Textures/crate.png", 45);
		//new Crate(this, getNextEntityID(), 8, 5, 8, 1, 1, 1f, "Textures/crate.png", 65);
		new Wall(this, getNextEntityID(), 0, 0, 0, 10, 10, "Textures/seamless_bricks/bricks2.png", 0);
		new Wall(this, getNextEntityID(), 10, 0, 0, 10, 10, "Textures/seamless_bricks/bricks2.png", 0);
		new Wall(this, getNextEntityID(), 20, 0, 0, 10, 10, "Textures/seamless_bricks/bricks2.png", 0);
		new Wall(this, getNextEntityID(), 30, 0, 0, 10, 10, "Textures/seamless_bricks/bricks2.png", 270);

		//new MovingTarget(this, getNextEntityID(), 2, 2, 10, 1, 1, 1, "Textures/seamless_bricks/bricks2.png", 0);
		
		//new FlatFloor(this, getNextEntityID(), 5, .1f, 5, 3, 3, "Textures/crate.png");
		
		//new RoamingZombie(this, getNextEntityID(), 2, 2, 10);
		
		new House(this, getNextEntityID(), 20, 0, 20, 0);
	}

/*
	@Override
	protected AbstractServerAvatar createPlayersAvatarEntity(ClientData client, int entityid) {
		return new TestGameServerAvatar(this, client.getPlayerID(), client.getPlayerID(), client.remoteInput, entityid);
	}

/*
	//@Override
	public IEntity createGameSpecificEntiy(int type, int entityid, int side, IRequiresAmmoCache irac) {
		switch (type) {
		case TestGameClientEntityCreator.LASER_BULLET:
			return new LaserBullet(this, entityid, irac, side);
		default:
			return null;//super.createEntity(type, entityid, side, irac);
		}
	}
*/

	@Override
	public float getAvatarStartHealth(AbstractAvatar avatar) {
		return 1;
	}


	@Override
	public float getAvatarMoveSpeed(AbstractAvatar avatar) {
		return 3f;
	}


	@Override
	public float getAvatarJumpForce(AbstractAvatar avatar) {
		return 2f;
	}


	@Override
	protected AbstractServerAvatar createPlayersAvatarEntity(ClientData client, int entityid) {
		return new TestGameServerAvatar(this, client, client.getPlayerID(), client.remoteInput, entityid);
	}


	@Override
	protected int getWinningSide() {
		return 0;
	}

}
