package com.scs.stevetech1.components;

import com.jme3.bounding.BoundingBox;
import com.jme3.scene.Spatial;

public interface IAvatarModel {

	Spatial createAndGetModel(boolean forClient, int side);
	
	BoundingBox getBoundingBox();
	
	float getCameraHeight();
	
	float getBulletStartHeight();
	
}
