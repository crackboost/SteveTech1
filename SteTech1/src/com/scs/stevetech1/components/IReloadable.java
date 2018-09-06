package com.scs.stevetech1.components;

import com.scs.stevetech1.server.AbstractGameServer;

public interface IReloadable {

	void setToBeReloaded();
	
	void reload();
}
