package com.scs.moonbaseassault.client;

import java.awt.Color;
import java.awt.Graphics2D;

import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.scs.stevetech1.jme.PaintableImage;

/**
 * Rows:
 * 0 - Skin tone
 * 1 - Eyes and eyebrows
 * 2 - Hair
 * 3 - Shirt
 * 4 - Trousers
 *
 */
public class SoldierTexture {
	
	private static final int SIZE = 32;

	public SoldierTexture() {
		super();
	}
	
	
	public static Texture getTexture(int side) {
		PaintableImage pi = new PaintableImage(SIZE, SIZE) {
			
			@Override
			public void paint(Graphics2D g) {
				for (int row=0 ; row<5 ; row++) {
					switch (row) {
					case 0: // Skin
						g.setColor(getRandomSkinColour());//Color.black);
						break;
					case 1: // Eyes and brows
						g.setColor(Color.BLACK);
						break;
					case 2: // Hair
						g.setColor(getRandomHairColour());//Color.black);
						break;
					case 3: // Shirt
						switch (side) {
						case 1:
							g.setColor(Color.yellow);
							break;
						case 2:
							g.setColor(Color.green);
							break;
						default:
							throw new RuntimeException("Todo");
						}
						break;
					case 4: // Trousers
						switch (side) {
						case 1:
							g.setColor(Color.yellow.darker());
							break;
						case 2:
							g.setColor(Color.green.darker());
							break;
						default:
							throw new RuntimeException("Todo");
						}
						break;
					}
					g.fillRect(0, row*(SIZE/5), SIZE, (row+1)*(SIZE/5));
				}
			}
			
		};
		
		pi.refreshImage();
		return new Texture2D(pi);
	}
	
	
	private static Color getRandomHairColour() {
		return Color.black;
	}
	

	private static Color getRandomSkinColour() {
		return Color.PINK;
	}
	
}

