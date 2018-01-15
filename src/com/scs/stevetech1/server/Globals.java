package com.scs.stevetech1.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class Globals {

	public static final boolean RECORD_VID = false;
	public static final boolean SHOW_SERVER_POS_ON_CLIENT = false;
	public static final boolean SHOW_AVATAR_WALK_DIR = false;
	public static final boolean DEBUG_ADJ_AVATAR_POS = false;
	public static final boolean DEBUG_REWIND_POS1 = false;
	public static final boolean DEBUG_HUD = false;
	public static final boolean LOG_MOVING_TARGET_POS = false;
	public static final boolean DEBUG_MSGS = false;
	public static final boolean DEBUG_SHOOTING_POS = false;
	public static final boolean DEBUG_ENTITY_ADD_REMOVE = false;

	public static final String GAME_IP_ADDRESS = "localhost";
	public static final String LOBBY_IP_ADDRESS = "localhost";
	public static final int GAME_PORT = 6143;
	//public static final int UDP_PORT = 6143;
	public static final int LOBBY_PORT = 6144;
	//public static final int UDP_LOBBY_PORT = 6144;
	public static final float CONNECTION_IDLE_THRESH = 1000000;

	public static final int SERVER_TICKRATE_MS = 25; // Source: 15ms
	public static final int SERVER_SEND_UPDATE_INTERVAL_MS = 70; // How often server sends entity updates.  This must be fast enough so the client has recent data to work with 
	public static final int CLIENT_RENDER_DELAY = 1000; //SERVER_SEND_UPDATE_INTERVAL_MS*3; // How far in past the client should render the view.  Source: 50ms
	public static final int PING_INTERVAL_MS = 100 * 1000; // How often server sends pings
	public static final int ARTIFICIAL_COMMS_DELAY = 0;
	public static final float MAX_CLIENT_POSITION_DISCREP = 0.1f; // Max difference between what client and server think the pos of avatar is, before client is corrected

	public static final String VERSION_ = "0.01";
	public static final boolean SHOW_LOGO = false;
	public static final boolean USE_MODEL_FOR_PLAYERS = false;

	// Our movement speed
	public static final float PLAYER_MOVE_SPEED = 3f;
	public static final float JUMP_FORCE = 2f;
	public static final float SMALLEST_MOVE_DIST = 0.02f;

	public static final float CAM_DIST = 50f;
	public static final boolean LIGHTING = true;
	public static final String NAME = "SteTech1";

	// Changing settings
	public static boolean SYNC_AVATAR_POS = true;
	public static boolean SYNC_GRENADE_POS = true;

	// User Data
	public static final String ENTITY = "Entity";

	public static final Random rnd = new Random();

	static {
		if (ARTIFICIAL_COMMS_DELAY + SERVER_SEND_UPDATE_INTERVAL_MS >= CLIENT_RENDER_DELAY) {
			throw new RuntimeException("Data will not be sent in time for the client to use it to render");
		}
	}


	public static void p(String s) {
		System.out.println(System.currentTimeMillis() + ": " + s);
	}


	public static void appendToFile(String path, String text) {
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(path, true));
			bw.write(text);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally { // always close the file
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ioe2) {
					// just ignore it
				}
			}

		}
	}

}