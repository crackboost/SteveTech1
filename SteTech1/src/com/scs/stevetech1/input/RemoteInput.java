package com.scs.stevetech1.input;

import com.jme3.math.Vector3f;
import com.scs.stevetech1.netmessages.PlayerInputMessage;
import com.scs.stevetech1.server.Globals;

public class RemoteInput implements IInputDevice {

	private PlayerInputMessage pim = new PlayerInputMessage(); // create default so we don't get an NPE

	public RemoteInput() {
		super();
	}


	public void decodeMessage(PlayerInputMessage _pim) {
		pim = _pim;
		
		//Settings.p("Shoot dir=" + this.getDirection());

	}

	@Override
	public boolean getFwdValue() {
		return pim.fwd;
	}

	@Override
	public boolean getBackValue() {
		return pim.back;
	}


	@Override
	public boolean getStrafeLeftValue() {
		return pim.strafeLeft;
	}


	@Override
	public boolean getStrafeRightValue() {
		return pim.strafeRight;
	}


	@Override
	public boolean isJumpPressed() {
		return pim.jump;
	}


	/**
	 * Don't send these to the server; instead, the client sends a specific message that tells the server
	 * that the client has fired weapon.  We do this to ensure that the client and server always
	 * get a "shoot" message consistently.
	 */
	@Override
	public boolean isAbilityPressed(int i) {
		return false;
	}


	/**
	 * Don't send these to the server; instead, the client sends a specific message that tells the server
	 * that the client has chosen to reload.  We do this to ensure that the client and server always
	 * get a "shoot" message consistently.
	 */
	@Override
	public boolean isReloadPressed() {
		return false;
	}


	@Override
	public Vector3f getDirection() {
		return pim.direction;
	}


	@Override
	public Vector3f getLeft() {
		return pim.leftDir;
	}


}
