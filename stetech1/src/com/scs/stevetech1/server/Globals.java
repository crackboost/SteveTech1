package com.scs.stevetech1.server;

import java.util.Random;

import ssmith.io.IOFunctions;
import ssmith.lang.Functions;


public class Globals {
	
	public static final boolean HIDE_BELLS_WHISTLES = true;
	public static final boolean HIDE_EXPLOSION = false;
	public static final boolean RECORD_VID = false;
	public static final boolean STRICT = true; // Extra checks (which might slow stuff down)

	// Lots of consts for specific debugging output
	public static final boolean DEBUG_NO_ABILITY0 = true;
	public static final boolean TRANSPARENT_WALLS = true;
	public static final boolean DEBUG_AI_BULLET_POS = true;
	
	public static final boolean DEBUG_BULLET_HIT = false;
	public static final boolean DEBUG_AI_TARGETTING = false;
	public static final boolean DEBUG_HUD = false;
	public static final boolean DEBUG_ENTITY_ADD_REMOVE = false;
	public static final boolean DEBUG_AVATAR_SET = false;
	public static final boolean DEBUG_NO_AI_ANIM = false;
	public static final boolean DEBUG_SLIDING_DOORS = false;
	public static final boolean DEBUG_CLIENT_SERVER_FAR_APART = false;
	public static final boolean DEBUG_NO_BULLET = false;
	public static final boolean SHOW_SERVER_AVATAR_ON_CLIENT = false;
	public static final boolean SHOW_AVATAR_POS = false;
	public static final boolean SHOW_BULLET_COLLISION_POS = false;
	public static final boolean DEBUG_DIE_ANIM = false;
	public static final boolean DEBUG_NO_UPDATE_MSGS = false;
	public static final boolean SHOW_AVATAR_BOUNDS = false;
	public static final boolean USE_BOXES_FOR_AI_SOLDIER = false;
	public static final boolean USE_BOXES_FOR_AVATARS_SOLDIER = false;
	public static final boolean SHOW_IF_SYSTEM_TOO_SLOW = false;
	public static final boolean DEBUG_SLOW_CLIENT_AVATAR = false;
	public static final boolean TURN_OFF_CLIENT_POS_ADJ = false;
	public static final boolean PROFILE_SERVER = false;
	public static final boolean SHOW_SERVER_CLIENT_AVATAR_DIST = false;
	public static final boolean SHOW_NUM_ENT_UPDATES_SENT = false;
	public static final boolean FEW_MODELS = false;
	public static final boolean EMPTY_MAP = false;
	public static final boolean PLAYERS_START_IN_CORNER = false;
	public static final boolean DEBUG_CLIENT_ROTATION = false;
	public static final boolean DEBUG_PLAYER_RESTART = false;
	public static final boolean STOP_SERVER_AVATAR_MOVING = false;
	public static final boolean DEBUG_ADJ_AVATAR_POS = false;
	public static final boolean SHOW_LATEST_AVATAR_POS_DATA_TIMESTAMP = false;
	public static final boolean DEBUG_REWIND_POS1 = false;
	public static final boolean LOG_MOVING_TARGET_POS = false;
	public static final boolean DEBUG_MSGS = false;
	public static final boolean DEBUG_SHOOTING_POS = false;

	// Effects
	public static final boolean TOONISH = false; // Outlines

	//----
	public static final int PCENT_DROPPED_PACKETS = 10;
	public static final int MIN_ARTIFICIAL_COMMS_DELAY = 0;
	public static final int MAX_ARTIFICIAL_COMMS_DELAY = 0;
	//----
	
	public static final int PING_INTERVAL_MS = 5 * 1000; // How often server sends pings
	public static final boolean ONLY_ADJUST_CLIENT_ON_MOVE = false; // Only adjust the client avatar's position when the player moves them.

	public static final int KRYO_WRITE_BUFFER_SIZE = 16384*7;
	public static final int KRYO_OBJECT_BUFFER_SIZE = 4096*3*24;
	// Our movement speed
	public static final float SMALLEST_MOVE_DIST = 0.001f;// 0.02f;
	public static final float MAX_MOVE_DIST = 1f;

	public static final float CAM_DIST = 1500f;
	public static final float LASER_DIAM= 0.02f;

	// User Data
	public static final String ENTITY = "Entity";

	public static final Random rnd = new Random();

	private static final long GAME_START_TIME = System.currentTimeMillis();
	

	public static void p(String s) {
		System.out.println(System.currentTimeMillis() + ": " + s);
	}


	public static void pe(String s) {
		System.err.println(System.currentTimeMillis() + ": " + s);
	}

	
	public static void HandleError(Exception ex) {
		ex.printStackTrace();
		
		String filename = "Errors_" + GAME_START_TIME + ".log";
		IOFunctions.appendToFile(filename, Functions.Exception2String(ex));
		
	}
	
}
