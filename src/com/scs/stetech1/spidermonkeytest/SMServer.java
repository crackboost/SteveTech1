package com.scs.stetech1.spidermonkeytest;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.MessageListener;
import com.scs.stetech1.shared.IEntityController;

public class SMServer extends SimpleApplication implements IEntityController, ConnectionListener, MessageListener<HostedConnection>, PhysicsCollisionListener  {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
