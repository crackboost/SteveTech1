package com.scs.unittestgame;

import java.io.IOException;
import java.util.ArrayList;

import com.jme3.app.SimpleApplication;
import com.scs.stevetech1.server.Globals;

import ssmith.lang.Functions;

public class RunAll {

	public static final int NUM_ENTITIES = 5;

	public static void main(String[] args) {
		try {
			RunAll runAll = new RunAll();
			runAll.runServerAndClients();
			runAll.connDisconn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void runServerAndClients() throws IOException {
		UnitTestGameServer server = new UnitTestGameServer();

		ArrayList<SimpleApplication> clientInstances = new ArrayList<>();
		for (int i=0 ; i<1 ; i++) {
			clientInstances.add(new UnitTestGameClient());
		}

		Functions.sleep(10 * 1000);

		for(SimpleApplication app : clientInstances) {
			UnitTestGameClient client = (UnitTestGameClient)app;
			Globals.p("Num ents on client: " + client.entities.size());
			if (client.entities.size() != server.getNumEntities()) {
				throw new RuntimeException("Inconsistent entities!  Client=" + client.getNumEntities() + ", Server=" + server.getNumEntities());
			}
		}

		// Close clients
		for(SimpleApplication app : clientInstances) {
			UnitTestGameClient client = (UnitTestGameClient)app;
			client.quit("Manual");
		}

		Globals.p("Finished.");
		Functions.sleep(50000);
	}


	public void connDisconn() throws IOException {
		UnitTestGameServer server = new UnitTestGameServer();

		for (int i=0 ; i<30 ; i++) {
			UnitTestGameClient client = new UnitTestGameClient();
			Functions.sleep(20 * 1000);

			Globals.p("Num ents on client: " + client.getNumEntities());
			Globals.p("Num ents on server: " + server.getNumEntities());
			if (client.getNumEntities() != server.getNumEntities()) {
				throw new RuntimeException("Inconsistent entities!  Client=" + client.getNumEntities() + ", Server=" + server.getNumEntities());
			}

			client.quit("Manual");
		}

		UnitTestGameClient client = new UnitTestGameClient();
		Functions.sleep(10 * 1000);

		Globals.p("Num clients: " + server.clientList.getNumClients());
		Globals.p("Num ents on client: " + client.entities.size());
		Globals.p("Num ents on server: " + server.getNumEntities());
		if (client.entities.size() != server.getNumEntities()) {
			throw new RuntimeException("Inconsistent entities!");
		}


		client.quit("Manual");

		Globals.p("Finished.");

		Functions.sleep(50000);


	}


}
