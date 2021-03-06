package com.scs.stevetech1.client;

import com.jme3.math.Vector3f;
import com.scs.stevetech1.server.Globals;
import com.scs.stevetech1.shared.EntityPositionData;
import com.scs.stevetech1.shared.PositionCalculator;

public class HistoricalPositionCalculator {

	public HistoricalPositionCalculator() {

	}


	/**
	 * This calculates the difference between what the client and server think the position should be,
	 * so this can be added to the clients current position to get the correct position.
	 */
	public static Vector3f calcHistoricalPositionOffset(PositionCalculator serverPositionData, PositionCalculator clientPositionData, long serverTimeToUse) {
		if (serverPositionData.hasRecentData(serverTimeToUse)) {
			if (clientPositionData.hasRecentData(serverTimeToUse)) {
				EntityPositionData serverEPD = serverPositionData.calcPosition(serverTimeToUse, true);
				if (serverEPD != null) {
					long clientTimeToUse = serverTimeToUse;// - ping;
					// check where we should be based on where we were X ms ago
					EntityPositionData clientEPD = clientPositionData.calcPosition(clientTimeToUse, true);
					if (clientEPD != null) {
						
						// Don't to vertical into account - scs new
						clientEPD.position.y = serverEPD.position.y;
						
						Vector3f vdiff = serverEPD.position.subtract(clientEPD.position); 
						return vdiff;
					}
				}
			}
		}
		if (serverPositionData.hasAnyData()) {
			return serverPositionData.getMostRecent().position;
		}
		return null;
	}


}
