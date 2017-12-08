package com.scs.stetech1.components;

import com.jme3.math.Vector3f;

public interface ICanShoot {

	//Vector3f getWorldTranslation();

	Vector3f getShootDir();
	
	//Vector3f getBulletStartOffset();
	
	Vector3f getBulletStartPos();
	
	void hasSuccessfullyHit(IEntity e);
	
}
