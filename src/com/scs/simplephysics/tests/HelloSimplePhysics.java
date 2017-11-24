package com.scs.simplephysics.tests;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.simplephysics.ICollisionListener;
import com.scs.simplephysics.SimplePhysicsController;
import com.scs.simplephysics.SimpleRigidBody;
import com.scs.stetech1.jme.MySimpleCharacterControl;

public class HelloSimplePhysics extends SimpleApplication implements ActionListener, ICollisionListener {

	private SimpleRigidBody player;
	private boolean left = false, right = false, up = false, down = false;
	private Geometry playerModel;
	//Temporary vectors used on each frame.
	//They here to avoid instanciating new vectors on each frame
	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f();
	// Our movement speed
	private Vector3f walkDirection = new Vector3f();
	private SimplePhysicsController physicsController;
	
	private float speed;
	private float strafeSpeed;
	private float headHeight;

	public static void main(String[] args) {
		AppSettings settings = new AppSettings(true);
		settings.setSettingsDialogImage(null);
		HelloSimplePhysics app = new HelloSimplePhysics();
		app.settings = settings;
		app.start();
	}

	public void simpleInitApp() {
		physicsController = new SimplePhysicsController(this);
		
		// set player speed
		speed = 6f;
		strafeSpeed = 4f;
		headHeight = 3f;

		/** Create a box to use as our player model */
		Box box1 = new Box(1,1,1);
		playerModel = new Geometry("Box", box1);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
		mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
		playerModel.setMaterial(mat);    
		playerModel.setLocalTranslation(new Vector3f(0,6,0));
		playerModel.setLocalTranslation(new Vector3f(0,6,0));
		playerModel.setCullHint(CullHint.Always);
		rootNode.attachChild(playerModel);

		cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, 1000f);
		cam.setLocation(new Vector3f(0,6,0));
		/** Set up Physics */

		// We re-use the flyby camera for rotation, while positioning is handled by physics
		viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));

		setUpKeys();
		setUpLight();

		player = new SimpleRigidBody(playerModel, this.physicsController, null);

		// set basic physical properties:
		// todo player.setJumpForce(new Vector3f(0,5f,0));
		//player.setGravity(new Vector3f(0,1f,0));
		playerModel.setLocalTranslation(new Vector3f(0,6,0)); 

		// We attach the scene and the player to the rootnode and the physics space,
		// to make them appear in the game world.
		//rootNode.attachChild(sceneModel);
		//bulletAppState.getPhysicsSpace().add(landscape);

		this.initFloor();
		this.addBox(.1f, 3f, 5f, 5f);
		this.addBox(2f, 1f, 7f, 7f);

	}


	/** Make a solid floor and add it to the scene. */
	public void initFloor() {
		Box floor = new Box(30f, 0.1f, 15f);
		floor.scaleTextureCoordinates(new Vector2f(3, 6));

		Material floor_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		TextureKey key3 = new TextureKey("Textures/neon1.jpg");
		key3.setGenerateMips(true);
		Texture tex3 = assetManager.loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);
		floor_mat.setTexture("ColorMap", tex3);

		Geometry floor_geo = new Geometry("Floor", floor);
		floor_geo.setMaterial(floor_mat);
		floor_geo.setLocalTranslation(0, -0.1f, 0);
		this.rootNode.attachChild(floor_geo);
		
		SimpleRigidBody srb = new SimpleRigidBody(floor_geo, physicsController, null);
		physicsController.addSimpleRigidBody(srb);
	}


	public void addBox(float w, float h, float x, float z) {
		Box floor = new Box(w, h, w);
		floor.scaleTextureCoordinates(new Vector2f(3, 6));

		Material floor_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		//TextureKey key3 = new TextureKey("Textures/Terrain/Pond/Pond.jpg");
		TextureKey key3 = new TextureKey("Textures/floor015.png");
		key3.setGenerateMips(true);
		Texture tex3 = assetManager.loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);
		floor_mat.setTexture("ColorMap", tex3);

		Geometry floor_geo = new Geometry("Box", floor);
		floor_geo.setMaterial(floor_mat);
		floor_geo.setLocalTranslation(x, h*2, z);
		this.rootNode.attachChild(floor_geo);

		SimpleRigidBody srb = new SimpleRigidBody(floor_geo, physicsController, null);
		physicsController.addSimpleRigidBody(srb);
	}


	private void setUpLight() {
		// We add light so we see the scene
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(1.3f));
		rootNode.addLight(al);

		DirectionalLight dl = new DirectionalLight();
		dl.setColor(ColorRGBA.White);
		dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
		rootNode.addLight(dl);
	}

	/** We over-write some navigational key mappings here, so we can
	 * add physics-controlled walking and jumping: */
	private void setUpKeys() {
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(this, "Left");
		inputManager.addListener(this, "Right");
		inputManager.addListener(this, "Up");
		inputManager.addListener(this, "Down");
		inputManager.addListener(this, "Jump");

		inputManager.addMapping("Test", new KeyTrigger(KeyInput.KEY_T));
		inputManager.addListener(this, "Test");
	}

	/** These are our custom actions triggered by key presses.
	 * We do not walk yet, we just keep track of the direction the user pressed. */
	public void onAction(String binding, boolean isPressed, float tpf) {
		if (binding.equals("Left")) {
			left = isPressed;
		} else if (binding.equals("Right")) {
			right= isPressed;
		} else if (binding.equals("Up")) {
			up = isPressed;
		} else if (binding.equals("Down")) {
			down = isPressed;
		} else if (binding.equals("Jump")) {
			if (isPressed) { player.jump(); }
		} else if (binding.equals("Test")) {
			if (isPressed) { 
				playerModel.setLocalTranslation(new Vector3f(2, 10, 2));
			}
		}
	}

	/**
	 * This is the main event loop--walking happens here.
	 * We check in which direction the player is walking by interpreting
	 * the camera direction forward (camDir) and to the side (camLeft).
	 * The setWalkDirection() command is what lets a physics-controlled player walk.
	 * We also make sure here that the camera moves with player.
	 */
	@Override
	public void simpleUpdate(float tpf) {
		this.physicsController.update(tpf);
		
		/*
		 * The direction of character is determined by the camera angle
		 * the Y direction is set to zero to keep our character from
		 * lifting of terrain. For free flying games simply ad speed 
		 * to Y axis
		 */
		camDir.set(cam.getDirection()).multLocal(speed, 0.0f, speed);
		camLeft.set(cam.getLeft()).multLocal(strafeSpeed);
		walkDirection.set(0, 0, 0);
		if (left) {
			walkDirection.addLocal(camLeft);
		}
		if (right) {
			walkDirection.addLocal(camLeft.negate());
		}
		if (up) {
			walkDirection.addLocal(camDir);
		}
		if (down) {
			walkDirection.addLocal(camDir.negate());
		}
		player.setLinearVelocity(walkDirection);

		/*
		 * By default the location of the box is on the bottom of the terrain
		 * we make a slight offset to adjust for head height.
		 */
		cam.setLocation(new Vector3f(playerModel.getLocalTranslation().x,playerModel.getLocalTranslation().y + headHeight,playerModel.getLocalTranslation().z));

	}

	@Override
	public void collisionOccurred(SimpleRigidBody a, SimpleRigidBody b, Vector3f point) {
		
		
	}
}
