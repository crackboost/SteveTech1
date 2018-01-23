package com.scs.stevetech1.components;

public interface IRequiresAmmoCache<T> { // Used by system UpdateAmmoCacheSystem

	int getID();
	
	boolean requiresAmmo();
	
	public int getAmmoType();
	
	void addToCache(T o);
	
	//void emptyCache(); // For when avatar/gun is removed from the game
}