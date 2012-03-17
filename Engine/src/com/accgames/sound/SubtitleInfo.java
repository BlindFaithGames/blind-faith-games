package com.accgames.sound;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.Gravity;
import android.widget.Toast;

public class SubtitleInfo implements Parcelable {
	
	private boolean enabled;
	
	private int resourceId;
	private int viewGroupRoot;
	private int id_text;
	private int xOffset, yOffset;
	private int duration;
	private int gravity;
	private Map<Integer,String> onomatopeias;
	
	public static final Parcelable.Creator<SubtitleInfo> CREATOR = new Parcelable.Creator<SubtitleInfo>() {
		public SubtitleInfo createFromParcel(Parcel in) {
			return new SubtitleInfo(in);
		}

		public SubtitleInfo[] newArray(int size) {
			return new SubtitleInfo[size];
		}
	};
	
	public SubtitleInfo() {
		this.resourceId = -1;
		this.viewGroupRoot = -1;
		this.id_text = -1;
		this.xOffset = 0;
		this.yOffset = 0;
		this.duration = Toast.LENGTH_LONG;
		this.gravity = Gravity.CENTER;
		this.enabled = true;
		this.onomatopeias = new HashMap<Integer, String>();
	}

	public SubtitleInfo(int resourceId, int viewGroupRoot, int id_text,
			int xOffset, int yOffset, int duration, int gravity, Map<Integer, String> onomatopeias) {
		super();
		this.resourceId = resourceId;
		this.viewGroupRoot = viewGroupRoot;
		this.id_text = id_text;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.duration = duration;
		this.gravity = gravity;
		this.enabled = true;
		this.onomatopeias = onomatopeias;
	}
	public SubtitleInfo(Parcel in) {
		this.resourceId = in.readInt();
		this.viewGroupRoot = in.readInt();
		this.id_text = in.readInt();
		this.xOffset = in.readInt();
		this.yOffset = in.readInt();
		this.duration = in.readInt();
		this.gravity = in.readInt();
		this.enabled = in.readInt() == 1;
		this.onomatopeias = in.readHashMap(HashMap.class.getClassLoader());
	}

	public int getResourceId() {
		return resourceId;
	}
	
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
	
	public int getViewGroupRoot() {
		return viewGroupRoot;
	}
	
	public void setViewGroupRoot(int viewGroupRoot) {
		this.viewGroupRoot = viewGroupRoot;
	}
	
	public int getId_text() {
		return id_text;
	}
	
	public void setId_text(int id_text) {
		this.id_text = id_text;
	}
	
	public int getxOffset() {
		return xOffset;
	}
	
	public int getyOffset() {
		return yOffset;
	}
	
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public int getGravity() {
		return gravity;
	}
	
	public void setGravity(int gravity) {
		this.gravity = gravity;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getOnomatopeia(int resource) {
		if(onomatopeias != null)
			return onomatopeias.get(resource);
		else
			return null;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(resourceId);
		dest.writeInt(viewGroupRoot);
		dest.writeInt(id_text);
		dest.writeInt(xOffset);
		dest.writeInt(yOffset);
		dest.writeInt(duration);
		dest.writeInt(gravity);
		if(enabled)
			dest.writeInt(1);
		else
			dest.writeInt(0);
		dest.writeMap(onomatopeias);
	}

}
