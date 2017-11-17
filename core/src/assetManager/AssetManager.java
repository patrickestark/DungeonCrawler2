package assetManager;

import java.util.HashMap;
import java.util.Map;

import ecu.se.Utils;

public class AssetManager {	
	public static HashMap<String, Asset> assets = new HashMap<String, Asset>();
	
	public static FontAsset getFont(String name, int size) {
		String key =name + size + "g";
		if(assets.containsKey(key)) 
			return (FontAsset)(assets.get(key));
		
		FontAsset temp = new FontAsset(name, size);
		if (temp.loadedSuccessfully()) {
			Utils.println(AssetManager.class, "Adding a new font for: " + name);
			assets.put(key, temp);
			
			
			return temp.getAsset();
		}
		System.err.println("Failed to load font.");
		return null;
	}

	public static TextureAsset getTexture(String name) {
		if(assets.containsKey(name))
			return (TextureAsset)(assets.get(name));
		

		TextureAsset temp = new TextureAsset(name);

		if (temp.loadedSuccessfully()) {
			Utils.println(AssetManager.class, "Adding a new texture for: " + name);

			assets.put(name, temp);
			return temp.getAsset();
		}
		System.err.println("Failed to load texture.");
		return null;
	}

	public static SpriteAsset getSpriteSheet(String name) {
		if(assets.containsKey(name))
			return (SpriteAsset)(assets.get(name));

		SpriteAsset temp = new SpriteAsset(name);
		if (temp.loadedSuccessfully()) {
			Utils.println(AssetManager.class, "Adding a new sprite for: " + name);

			assets.put(name, temp);
			return temp.getAsset();
		}
		System.err.println("Failed to load sprite.");
		return null;
	}

	public static void dispose() {
		for(Map.Entry<String, Asset> entry : assets.entrySet()) {
			Asset a = entry.getValue();
			a.dispose();
		}
	}
}
