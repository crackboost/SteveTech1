package com.scs.stetech1.client.entities;

import java.util.HashMap;

import com.scs.stetech1.components.IAffectedByPhysics;
import com.scs.stetech1.components.ICollideable;
import com.scs.stetech1.server.Settings;
import com.scs.stetech1.shared.EntityTypes;
import com.scs.stetech1.shared.IEntityController;

public class EnemyPlayersAvatar extends PhysicalEntity implements IAffectedByPhysics, ICollideable {// Need ICollideable so lasers don't bounce off it

	public EnemyPlayersAvatar(IEntityController _game, float x, float y, float z, float w, float h, float d, String tex, float rotDegrees) {
		super(_game, EntityTypes.AVATAR, "EnemyAvatar");

		Box box1 = new Box(w/2, h/2, d/2);
		//box1.scaleTextureCoordinates(new Vector2f(WIDTH, HEIGHT));
		Geometry geometry = new Geometry("Crate", box1);
		//int i = NumberFunctions.rnd(1, 10);
		if (!_game.isServer()) { // Not running in server
			TextureKey key3 = new TextureKey(tex);//Settings.getCrateTex());//"Textures/boxes and crates/" + i + ".png");
			key3.setGenerateMips(true);
			Texture tex3 = module.getAssetManager().loadTexture(key3);
			tex3.setWrap(WrapMode.Repeat);

			Material floor_mat = null;
			if (Settings.LIGHTING) {
				floor_mat = new Material(module.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
				floor_mat.setTexture("DiffuseMap", tex3);
			} else {
				floor_mat = new Material(module.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
				floor_mat.setTexture("ColorMap", tex3);
			}

			geometry.setMaterial(floor_mat);
			floor_mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			geometry.setQueueBucket(Bucket.Transparent);
		}

		this.main_node.attachChild(geometry);
		float rads = (float)Math.toRadians(rotDegrees);
		main_node.rotate(0, rads, 0);
		//main_node.setLocalTranslation(x+(w/2), h/2, z+0.5f);
		main_node.setLocalTranslation(x+(w/2), y+(h/2), z+(d/2));

		rigidBodyControl = new RigidBodyControl(1f);
		main_node.addControl(rigidBodyControl);
		
		module.getBulletAppState().getPhysicsSpace().add(rigidBodyControl);
		module.getRootNode().attachChild(this.main_node);

		geometry.setUserData(Settings.ENTITY, this);
		main_node.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);

		module.addEntity(this);

	}


	@Override
	public void process(float tpf) {
		//Settings.p("Pos: " + this.getLocation());
	}


	@Override
	public void collidedWith(ICollideable other) {
		// Do nothing
	}


	@Override
	public HashMap<String, Object> getCreationData() {
		return null;
	}


}
