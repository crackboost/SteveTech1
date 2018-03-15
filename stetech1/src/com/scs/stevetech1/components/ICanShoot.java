package com.scs.stevetech1.components;

import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.scs.stevetech1.server.RayCollisionData;

public interface ICanShoot {

	Vector3f getShootDir();
	
	Vector3f getBulletStartPos();
	
	RayCollisionData checkForCollisions(Ray r, float range);
	
}
