package com.fourtyblocksunder.abstention.editor.data;

import java.util.ArrayList;

public class GraphData {

	private ArrayList<DialogData> dialogData;

	private ArrayList<ImageData> imageData;

	private ArrayList<SpriteData> spriteData;

	private ArrayList<SoundData> soundData;

	private ArrayList<ClusterData> clusterData;
	
	private CameraData cameraData;
	
	public GraphData() {
	}

	public void addCluster(ClusterData cluster) {
		if (clusterData == null) {
			clusterData = new ArrayList<ClusterData>();
		}

		clusterData.add(cluster);
	}

	public void removeCluster(ClusterData cluster) {
		if (clusterData != null) {
			clusterData.remove(cluster);
		}
	}

	public void addSound(SoundData sound) {
		if (soundData == null) {
			soundData = new ArrayList<SoundData>();
		}

		soundData.add(sound);
	}

	public void removeSound(SoundData sound) {
		if (soundData != null) {
			soundData.remove(sound);
		}
	}

	public void addSprite(SpriteData sprite) {
		if (spriteData == null) {
			spriteData = new ArrayList<SpriteData>();
		}

		spriteData.add(sprite);
	}

	public void removeSprite(SpriteData sprite) {
		if (spriteData != null) {
			spriteData.remove(sprite);
		}
	}

	public void addImage(ImageData image) {
		if (imageData == null) {
			imageData = new ArrayList<ImageData>();
		}

		imageData.add(image);
	}

	public void removeImage(ImageData image) {
		if (imageData != null) {
			imageData.remove(image);
		}
	}

	public void addDialog(DialogData dialog) {
		if (dialogData == null) {
			dialogData = new ArrayList<DialogData>();
		}

		dialogData.add(dialog);
	}

	public void removeDialog(DialogData dialog) {
		if (dialogData != null) {
			dialogData.remove(dialog);
		}
	}

	public CameraData getCameraData() {
		return this.cameraData;
	}

	public void setCameraData(CameraData cameraData) {
		this.cameraData = cameraData;
	}
	
}
