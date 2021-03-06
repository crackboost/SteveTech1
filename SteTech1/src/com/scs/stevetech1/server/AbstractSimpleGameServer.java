package com.scs.stevetech1.server;

import com.scs.stevetech1.client.ValidateClientSettings;
import com.scs.stevetech1.data.GameOptions;
import com.scs.stevetech1.entities.PhysicalEntity;
import com.scs.stevetech1.netmessages.MyAbstractMessage;
import com.scs.stevetech1.shared.AbstractCollisionValidator;

public abstract class AbstractSimpleGameServer extends AbstractGameServer {

	public static final double VERSION = 1d;
	public static final String GAME_CODE = "gamecode";
	public static final String KEY = "key";

	protected AbstractCollisionValidator collisionValidator = new AbstractCollisionValidator();

	public AbstractSimpleGameServer(int port) {
		super(
				new ValidateClientSettings(GAME_CODE, VERSION, KEY), 
				new GameOptions(Globals.DEFAULT_TICKRATE, Globals.DEFAULT_SEND_UPDATES_INTERVAL, Globals.DEFAULT_RENDER_DELAY, Globals.DEFAULT_NETWORK_TIMEOUT, 
						10*1000, 240*1000, 10*1000, 
						"localhost", port, 
						4, 4)
				);
	}


	@Override
	public boolean doWeHaveSpaces() {
		return true;
	}


	@Override
	protected Class<? extends Object>[] getListofMessageClasses() {
		return null;
	}


	/*
	 * Just use the client id as the side, to easily ensure every player is on a different side. 
	 */
	@Override
	public byte getSideForPlayer(ClientData client) {
		return (byte)client.getPlayerID(); // todo - check not  > 127
	}



	@Override
	public int getMinSidesRequiredForGame() {
		return 1;
	}



	@Override
	public final boolean canCollide(PhysicalEntity a, PhysicalEntity b) {
		return collisionValidator.canCollide(a, b);
	}

}
