package com.scs.simplephysics.tests;

import java.util.Collection;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.asset.TextureKey;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.simplephysics.ICollisionListener;
import com.scs.simplephysics.SimpleCharacterControl;
import com.scs.simplephysics.SimplePhysicsController;
import com.scs.simplephysics.SimpleRigidBody;

/*
 * An example of using Simple Physics.  
 * Walk around with WASD.  
 * Left mouse button launches a normal bouncing ball, right mouse button launches a "floating" ball unaffected by gravity or friction.
 * T to turn physics on/off
 * B to blow a wind.
 * 
 */
public class HelloSimplePhysics extends SimpleApplication implements ActionListener, ICollisionListener<Spatial> {

	private SimplePhysicsController<Spatial> physicsController;
	private SimpleCharacterControl<Spatial> player;
	private final Vector3f walkDirection = new Vector3f();

	private boolean left = false, right = false, up = false, down = false;
	private Geometry playerModel;
	private final float playerSpeed = 8f;
	private final float headHeight = 1f;
	private DirectionalLight sun;

	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f();

	public static void main(String[] args) {
		AppSettings settings = new AppSettings(true);
		settings.setSettingsDialogImage(null);
		HelloSimplePhysics app = new HelloSimplePhysics();
		app.settings = settings;
		app.start();
	}


	public void simpleInitApp() {
		cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, 1000f);
		viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));

		physicsController = new SimplePhysicsController<Spatial>(this);
		setUpKeys();


		Box playerBox = new Box(.3f, .9f, .3f);
		playerModel = new Geometry("Player", playerBox);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Blue);
		playerModel.setMaterial(mat);
		playerModel.setLocalTranslation(new Vector3f(0,6,0));
		playerModel.setCullHint(CullHint.Always);
		rootNode.attachChild(playerModel);

		// Setup the scene
		setUpLight();

		player = new SimpleCharacterControl<Spatial>(playerModel, this.physicsController, this.playerModel);
		playerModel.setLocalTranslation(new Vector3f(0,4,0)); 

		this.addFloor();
		this.addWall();
		this.addBox(2f, 8f, 7f, 1f, 1f, .1f);
		this.addBox(2f, 6f, 7f, 1f, 1f, .3f);

		// Add boxes with various states of bounciness
		for (int i=0 ; i<10 ; i++) {
			this.addBox(4f+(i*2), 7f, 9f, 1f, 1f, (i/10f));
		}
		//this.addBall(10, 6, 10, .2f, new Vector3f(-3f, 0f, 0f), SimpleRigidBody.DEF_GRAVITY, SimpleRigidBody.DEF_AIR_FRICTION, 0.2f); // Bouncing ball
		//this.addBall(12, 6, 12, .2f, new Vector3f(0, -6f, -6f), 0, 1, 1); // Plasma ball

		// Add shadows
		final int SHADOWMAP_SIZE = 1024;
		DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(getAssetManager(), SHADOWMAP_SIZE, 2);
		//dlsr.setShadowIntensity(1f);
		//dlsr.setShadowZFadeLength(10f);
		dlsr.setLight(sun);
		this.viewPort.addProcessor(dlsr);
/*
		p("Recording video");
		VideoRecorderAppState video_recorder = new VideoRecorderAppState();
		stateManager.attach(video_recorder);
		*/
	}


	public void addFloor() {
		Box floor = new Box(30f, 0.1f, 30f);
		floor.scaleTextureCoordinates(new Vector2f(3, 6));

		Material floorMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		TextureKey key3 = new TextureKey("Textures/grass.jpg");
		key3.setGenerateMips(true);
		Texture tex3 = assetManager.loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);
		floorMaterial.setTexture("ColorMap", tex3);

		Geometry floorGeometry = new Geometry("Floor", floor);
		floorGeometry.setMaterial(floorMaterial);
		floorGeometry.setLocalTranslation(0, -0.1f, 0);
		floorGeometry.setShadowMode(ShadowMode.Receive);
		this.rootNode.attachChild(floorGeometry);

		SimpleRigidBody<Spatial> srb = new SimpleRigidBody<Spatial>(floorGeometry, physicsController, floorGeometry);
		srb.setMovable(false);
	}


	public void addWall() {
		Box floor = new Box(5f, 5f, .1f);

		Material wallMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		TextureKey key3 = new TextureKey("Textures/bricks.jpg");
		key3.setGenerateMips(true);
		Texture tex3 = assetManager.loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);
		wallMaterial.setTexture("ColorMap", tex3);

		Geometry wallGeometry = new Geometry("Wall", floor);
		wallGeometry.setMaterial(wallMaterial);
		wallGeometry.setLocalTranslation(3, 2.5f, 20);
		wallGeometry.setShadowMode(ShadowMode.CastAndReceive);
		this.rootNode.attachChild(wallGeometry);

		SimpleRigidBody<Spatial> srb = new SimpleRigidBody<Spatial>(wallGeometry, physicsController, wallGeometry);
		srb.setMovable(false);
	}


	public void addBox(float x, float y, float z, float w, float h, float bounciness) {
		Box box = new Box(w/2, h/2, w/2);
		//box.scaleTextureCoordinates(new Vector2f(3, 6));

		Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		TextureKey key3 = new TextureKey("Textures/crate.png");
		//floor_mat.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
		key3.setGenerateMips(true);
		Texture tex3 = assetManager.loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);
		material.setTexture("ColorMap", tex3);

		Geometry boxGeometry = new Geometry("Box", box);
		boxGeometry.setMaterial(material);
		boxGeometry.setLocalTranslation(x, y, z);
		boxGeometry.setShadowMode(ShadowMode.CastAndReceive);
		this.rootNode.attachChild(boxGeometry);

		SimpleRigidBody<Spatial> srb = new SimpleRigidBody<Spatial>(boxGeometry, physicsController, boxGeometry);
		srb.setBounciness(bounciness);
	}


	public void addBall(float x, float y, float z, float rad, Vector3f dir, float grav, float airRes, float bounce) {
		Sphere sphere = new Sphere(16, 16, rad);

		Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		TextureKey key3 = new TextureKey("Textures/football.jpg");
		key3.setGenerateMips(true);
		Texture tex3 = assetManager.loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);
		material.setTexture("ColorMap", tex3);

		Geometry ballGeometry = new Geometry("Sphere", sphere);
		ballGeometry.setMaterial(material);
		ballGeometry.setLocalTranslation(x, y, z);
		ballGeometry.setShadowMode(ShadowMode.Cast);
		this.rootNode.attachChild(ballGeometry);

		SimpleRigidBody<Spatial> srb = new SimpleRigidBody<Spatial>(ballGeometry, physicsController, ballGeometry);
		srb.setLinearVelocity(dir);
		srb.setGravity(grav);
		srb.setAerodynamicness(airRes);
		srb.setBounciness(bounce);
	}


	private void setUpLight() {
		// Remove existing lights
		getRootNode().getWorldLightList().clear();
		getRootNode().getLocalLightList().clear();

		// We add light so we see the scene
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(.2f));
		rootNode.addLight(al);

		sun = new DirectionalLight();
		sun.setColor(ColorRGBA.Yellow);
		sun.setDirection(new Vector3f(.5f, -1f, .5f).normalizeLocal());
		rootNode.addLight(sun);
	}


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

		inputManager.addMapping("Shoot0", new MouseButtonTrigger(0));
		inputManager.addListener(this, "Shoot0");
		inputManager.addMapping("Shoot1", new MouseButtonTrigger(1));
		inputManager.addListener(this, "Shoot1");

		inputManager.addMapping("Blow", new KeyTrigger(KeyInput.KEY_B));
		inputManager.addListener(this, "Blow");

		inputManager.addMapping("TogglePhysics", new KeyTrigger(KeyInput.KEY_T));
		inputManager.addListener(this, "TogglePhysics");
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
			if (isPressed) { 
				player.jump(); 
			}
		} else if (binding.equals("Shoot0")) {
			if (isPressed) { 
				Vector3f startPos = new Vector3f(cam.getLocation());
				startPos.addLocal(cam.getDirection().mult(2));
				this.addBall(startPos.x, startPos.y, startPos.z, .2f, this.cam.getDirection().mult(25f), SimpleRigidBody.DEFAULT_GRAVITY, SimpleRigidBody.DEFAULT_AERODYNAMICNESS, 0.4f); // Bouncing ball
			}
		} else if (binding.equals("Shoot1")) {
			if (isPressed) { 
				Vector3f startPos = new Vector3f(cam.getLocation());
				startPos.addLocal(cam.getDirection().mult(4));
				this.addBall(startPos.x, startPos.y, startPos.z, .2f, this.cam.getDirection().mult(35f), 0, 1, 0.2f); // "Laser" ball
			}
		} else if (binding.equals("TogglePhysics")) {
			if (isPressed) { 
				// Toggle enabled
				physicsController.setEnabled(!physicsController.getEnabled());
			}
		} else if (binding.equals("Blow")) {
			if (isPressed) {
				blowEntities();
			}
		}
	}


	private void blowEntities() {
		Collection<SimpleRigidBody<Spatial>> entities = physicsController.getEntities();
		synchronized (entities) {
			// Loop through the entities
			for(SimpleRigidBody<Spatial> e : entities) {
				e.getLinearVelocity().addLocal(4, .5f, 0);
			}
		}
	}


	@Override
	public void simpleUpdate(float tpf_secs) {
		camDir.set(cam.getDirection()).multLocal(playerSpeed, 0.0f, playerSpeed);
		camLeft.set(cam.getLeft()).multLocal(playerSpeed);
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
		walkDirection.y = 0; // Prevent us walking up or down
		//scs todo? player.getAdditionalForce().set(walkDirection);
		player.setAdditionalForce(this.walkDirection);

		this.physicsController.update(tpf_secs);

		cam.setLocation(new Vector3f(playerModel.getLocalTranslation().x, playerModel.getLocalTranslation().y + headHeight, playerModel.getLocalTranslation().z));

		try {
			Thread.sleep(5); // If the FPS is wayyy to high (> 1000 FPS), things get a bit crazy
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void collisionOccurred(SimpleRigidBody<Spatial> a, SimpleRigidBody<Spatial> b, Vector3f point) {
		p("Collision between " + a.userObject + " and " + b.userObject);

	}


	public boolean canCollide(SimpleRigidBody<Spatial> a, SimpleRigidBody<Spatial> b) {
		return true;
	}


	public static void p(String s) {
		System.out.println(s);
	}


}