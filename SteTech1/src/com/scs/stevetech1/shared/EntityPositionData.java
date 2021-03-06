package com.scs.stevetech1.shared;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class EntityPositionData {

	public long serverTimestamp;	
	public Vector3f position = new Vector3f();

	public EntityPositionData() {
		super();
	}


	public EntityPositionData getInterpol(EntityPositionData other, long time) {
		// interpolate between timestamps
		float frac = ((float)(serverTimestamp - time) / (float)(serverTimestamp - other.serverTimestamp));
		Vector3f posToSet = new Vector3f();
		//posToSet.interpolateLocal(this.position, other.position, frac);
		posToSet.interpolateLocal(this.position, other.position, frac);

		Quaternion newRot = new Quaternion();
		/*Quaternion newRot2 = newRot;
		if (this.rotation != null) { // client-side EPD doesn't have any rot
			newRot2 = newRot.slerp(this.rotation, other.rotation, frac);
		}*/

		EntityPositionData epd = new EntityPositionData();
		epd.position = posToSet;
		//epd.rotation = newRot2;
		epd.serverTimestamp = time;
		return epd;
	}
	
	
	public String toString() {
		return super.toString() + ": " + this.position.toString();
	}

}
