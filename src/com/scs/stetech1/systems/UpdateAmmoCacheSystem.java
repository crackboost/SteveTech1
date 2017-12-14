package com.scs.stetech1.systems;

import com.scs.stetech1.client.AbstractGameClient;
import com.scs.stetech1.components.IEntity;
import com.scs.stetech1.components.IRequiresAmmoCache;
import com.scs.stetech1.netmessages.RequestNewBulletMessage;

public class UpdateAmmoCacheSystem extends AbstractSystem {

	private AbstractGameClient server;
	
	public UpdateAmmoCacheSystem(AbstractGameClient _server) {
		super();
		
		server = _server;
	}

	
	@Override
	public void process(IEntity entity, float tpf_secs) {
		if (entity instanceof IRequiresAmmoCache) {
			IRequiresAmmoCache irac = (IRequiresAmmoCache)entity;
			RequestNewBulletMessage rnbm = new RequestNewBulletMessage();
			server.networkClient.sendMessageToServer(rnbm);
		}
		
	}

}
