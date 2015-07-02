package cn.o.app.ui.photo;

import java.util.ArrayList;

import cn.o.app.io.Extra;

@SuppressWarnings("serial")
public class PhotoExtra extends Extra {

	protected int displayedIndex;

	protected ArrayList<String> photos;

	public ArrayList<String> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<String> photos) {
		this.photos = photos;
	}

	public int getDisplayedIndex() {
		return displayedIndex;
	}

	public void setDisplayedIndex(int displayedIndex) {
		this.displayedIndex = displayedIndex;
	}

}
