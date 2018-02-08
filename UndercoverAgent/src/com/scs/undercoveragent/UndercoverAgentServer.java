package com.scs.undercoveragent;

import java.io.IOException;

import com.jme3.math.Vector3f;
import com.jme3.system.JmeContext;
import com.scs.simplephysics.SimpleRigidBody;
import com.scs.stevetech1.data.GameOptions;
import com.scs.stevetech1.entities.AbstractAvatar;
import com.scs.stevetech1.entities.AbstractServerAvatar;
import com.scs.stevetech1.entities.PhysicalEntity;
import com.scs.stevetech1.jme.JMEFunctions;
import com.scs.stevetech1.server.AbstractGameServer;
import com.scs.stevetech1.server.ClientData;
import com.scs.stevetech1.server.Globals;
import com.scs.stevetech1.shared.IAbility;
import com.scs.undercoveragent.entities.InvisibleMapBorder;
import com.scs.undercoveragent.entities.MountainMapBorder;
import com.scs.undercoveragent.entities.SnowFloor;
import com.scs.undercoveragent.entities.SnowmanServerAvatar;
import com.scs.undercoveragent.entities.StaticSnowman;
import com.scs.undercoveragent.weapons.SnowballLauncher;

import ssmith.lang.NumberFunctions;

public class UndercoverAgentServer extends AbstractGameServer {

	public static void main(String[] args) {
		try {
			// Run the lobby server as well
			Thread r = new Thread("LobbyServer") {

				@Override
				public void run() {
					try {
						new UndercoverAgentLobbyServer();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			r.start();
			
			AbstractGameServer app = new UndercoverAgentServer();
			app.setPauseOnLostFocus(false);
			app.start(JmeContext.Type.Headless);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public UndercoverAgentServer() throws IOException {
		super(new GameOptions("Undercover Agent", 1, 999, 10*1000, 5*60*1000, 10*1000, UndercoverAgentStaticData.GAME_IP_ADDRESS, UndercoverAgentStaticData.GAME_PORT, UndercoverAgentStaticData.LOBBY_IP_ADDRESS, UndercoverAgentStaticData.LOBBY_PORT, 5, 5));

		//properties = new GameProperties(PROPS_FILE);
	}


	@Override
	public void moveAvatarToStartPosition(AbstractAvatar avatar) {
		avatar.setWorldTranslation(new Vector3f(3f, 1f, 3f + (avatar.playerID*2)));
		/*todo - re-add SimpleRigidBody<PhysicalEntity> collider;
		do {
			float x = NumberFunctions.rndFloat(2, UndercoverAgentStaticData.MAP_SIZE-3);
			float z = NumberFunctions.rndFloat(2, UndercoverAgentStaticData.MAP_SIZE-3);
			avatar.setWorldTranslation(x, 2f, z);
			collider = avatar.simpleRigidBody.checkForCollisions();
		} while (collider != null);*/
		Globals.p("Player starting at " + avatar.getWorldTranslation());
	}


	protected void createGame() {
		// Create border
		/*for (int z=0; z<UndercoverAgentStaticData.MAP_SIZE ; z+=2) {
			for (int x=0; x<UndercoverAgentStaticData.MAP_SIZE ; x+=2) {
				if (x == 0 || z == 0 || x >= UndercoverAgentStaticData.MAP_SIZE-1 || z >= UndercoverAgentStaticData.MAP_SIZE-2) {
				} else {
					int rnd = NumberFunctions.rnd(0, 6);
					switch (rnd) {
					}
				}
			}			
		}*/

		/*
		new Igloo(this, getNextEntityID(), 5, 0, 5, JMEFunctions.GetRotation(-1, 0));
		//new SnowHill1(this, getNextEntityID(), 10, 0, 10, 0);
		new StaticSnowman(this, getNextEntityID(), 5, 0, 10, JMEFunctions.GetRotation(-1, 0));
		new SnowTree2(this, getNextEntityID(), 10, 0, 5, JMEFunctions.GetRotation(-1, 0));
*/
		
		// Place snowman
		/*int numSnowmen = 30;
		for (int i=0 ; i<numSnowmen ; i++) {
			//while (numSnowmen > 0) {
			float x = NumberFunctions.rndFloat(2, UndercoverAgentStaticData.MAP_SIZE-3);
			float z = NumberFunctions.rndFloat(2, UndercoverAgentStaticData.MAP_SIZE-3);
			StaticSnowman snowman = new StaticSnowman(this, getNextEntityID(), x, 0, z, JMEFunctions.GetRotation(-1, 0));
			this.actuallyAddEntity(snowman);
			SimpleRigidBody<PhysicalEntity> collider = snowman.simpleRigidBody.checkForCollisions();
			while (collider != null) {
				x = NumberFunctions.rndFloat(2, UndercoverAgentStaticData.MAP_SIZE-3);
				z = NumberFunctions.rndFloat(2, UndercoverAgentStaticData.MAP_SIZE-3);
				snowman.setWorldTranslation(x, z);
				collider = snowman.simpleRigidBody.checkForCollisions();
			}
			// todo - randomly rotate snowman
			//numSnowmen--;
			Globals.p("Placed " + i + " snowmen.");
		}*/
		
		// Place floor last so the snowmen don't collide with it when being placed
		SnowFloor floor = new SnowFloor(this, getNextEntityID(), 0, 0, 0, UndercoverAgentStaticData.MAP_SIZE, .5f, UndercoverAgentStaticData.MAP_SIZE, "Textures/snow.jpg");
		this.actuallyAddEntity(floor);

		// Walls
		InvisibleMapBorder borderL = new InvisibleMapBorder(this, getNextEntityID(), 0, 0, 0, UndercoverAgentStaticData.MAP_SIZE, Vector3f.UNIT_Z);
		this.actuallyAddEntity(borderL);  // works
		InvisibleMapBorder borderR = new InvisibleMapBorder(this, getNextEntityID(), UndercoverAgentStaticData.MAP_SIZE, 0, 0, UndercoverAgentStaticData.MAP_SIZE, Vector3f.UNIT_Z);
		this.actuallyAddEntity(borderR); // works
		InvisibleMapBorder borderBack = new InvisibleMapBorder(this, getNextEntityID(), 0, 0, UndercoverAgentStaticData.MAP_SIZE, UndercoverAgentStaticData.MAP_SIZE, Vector3f.UNIT_X);
		this.actuallyAddEntity(borderBack);
		InvisibleMapBorder borderFront = new InvisibleMapBorder(this, getNextEntityID(), 0, 0, -InvisibleMapBorder.BORDER_WIDTH, UndercoverAgentStaticData.MAP_SIZE, Vector3f.UNIT_X);
		this.actuallyAddEntity(borderFront);

		MountainMapBorder mborderL = new MountainMapBorder(this, getNextEntityID(), 0, 0, 0, UndercoverAgentStaticData.MAP_SIZE, Vector3f.UNIT_Z);
		this.actuallyAddEntity(mborderL); 
		MountainMapBorder mborderR = new MountainMapBorder(this, getNextEntityID(), UndercoverAgentStaticData.MAP_SIZE, 0, 0, UndercoverAgentStaticData.MAP_SIZE, Vector3f.UNIT_Z);
		this.actuallyAddEntity(mborderR);
		MountainMapBorder mborderBack = new MountainMapBorder(this, getNextEntityID(), 0, 0, UndercoverAgentStaticData.MAP_SIZE, UndercoverAgentStaticData.MAP_SIZE, Vector3f.UNIT_X);
		this.actuallyAddEntity(mborderBack);
		MountainMapBorder mborderFront = new MountainMapBorder(this, getNextEntityID(), 0, 0, 0, UndercoverAgentStaticData.MAP_SIZE, Vector3f.UNIT_X);
		this.actuallyAddEntity(mborderFront);
	}


	@Override
	protected AbstractServerAvatar createPlayersAvatarEntity(ClientData client, int entityid, int side) {
		SnowmanServerAvatar avatar = new SnowmanServerAvatar(this, client.getPlayerID(), client.remoteInput, entityid, side);
		//avatar.getMainNode().lookAt(new Vector3f(15, avatar.avatarModel.getCameraHeight(), 15), Vector3f.UNIT_Y); // Look towards the centre

		IAbility abilityGun = new SnowballLauncher(this, getNextEntityID(), avatar, 0);
		this.actuallyAddEntity(abilityGun);

		return avatar;
	}


	@Override
	public void collisionOccurred(SimpleRigidBody<PhysicalEntity> a, SimpleRigidBody<PhysicalEntity> b, Vector3f point) {
		PhysicalEntity pea = a.userObject;
		PhysicalEntity peb = b.userObject;

		if (pea instanceof SnowFloor == false && peb instanceof SnowFloor == false) {
			Globals.p("Collision between " + pea + " and " + peb);
		}

		super.collisionOccurred(a, b, point);

	}


	@Override
	public float getAvatarStartHealth(AbstractAvatar avatar) {
		return 1;
	}


}
