package com.scs.undercoveragent.models;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.scs.stevetech1.client.AbstractGameClient;
import com.scs.stevetech1.components.IAvatarModel;
import com.scs.stevetech1.jme.JMEAngleFunctions;
import com.scs.stevetech1.jme.JMEModelFunctions;
import com.scs.stevetech1.server.Globals;

/**
 * This class, and classes like this (i.e. a class for a model), are designed to keep all the model-specific settings in one place.
 */
public class SnowmanModel implements IAvatarModel {

	public static final float MODEL_WIDTH = 0.4f;
	private static final float MODEL_DEPTH = 0.3f;
	public static final float MODEL_HEIGHT = 0.7f;

	private AssetManager assetManager;
	private Spatial model;
	private Vector3f origPos;

	private boolean showingDied = false;

	public SnowmanModel(AssetManager _assetManager) {
		assetManager = _assetManager;

	}


	@Override
	public Spatial createAndGetModel() {
		model = assetManager.loadModel("Models/Holiday/Snowman.obj");
		model.setShadowMode(ShadowMode.CastAndReceive);
		JMEModelFunctions.scaleModelToHeight(model, MODEL_HEIGHT);
		JMEModelFunctions.moveYOriginTo(model, 0f);
		JMEAngleFunctions.rotateToWorldDirection(model, new Vector3f(-1, 0, 0)); // Point model fwds

		origPos = model.getLocalTranslation().clone();

		return model;
	}


	@Override
	public float getCameraHeight() {
		return MODEL_HEIGHT - 0.2f;
	}


	@Override
	public float getBulletStartHeight() {
		return MODEL_HEIGHT - 0.3f;
	}


	public void showDied(float tpf_secs) {
		if (model != null) {
			this.model.move(0, -tpf_secs/7, 0);
		}
		showingDied = true;

	}


	public void showAlive(float tpf_secs) {
		if (!showingDied) {
			return;
		} 
		this.model.setLocalTranslation(origPos);
		this.model.updateGeometricState();
		showingDied = false;

	}


	@Override
	public Vector3f getCollisionBoxSize() {
		return new Vector3f(MODEL_WIDTH, MODEL_HEIGHT, MODEL_DEPTH);
	}


	@Override
	public Spatial getModel() {
		return model;
	}


	@Override
	public void setAnim(int anim) {
		// Do nothing

	}

}
