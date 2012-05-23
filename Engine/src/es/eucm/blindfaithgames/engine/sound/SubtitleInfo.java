package es.eucm.blindfaithgames.engine.sound;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.Gravity;
import android.widget.Toast;

/**
 * This class encapsulates all persistent information needed to make work SubtitlesManager class.
 * 
 * @author Gloria Pozuelo & Javier Álvarez 
 * @implements Parcelable
 * 
 * */

public class SubtitleInfo implements Parcelable {
	
	private boolean enabled; // is transcription visible?
	
	private int resourceId; // custom toast id
	private int viewGroupRoot; // custom toast root
	private int id_text; // text id of the custom toast
	private int xOffset, yOffset; // values added to gravity
	private int duration; // custom toast duration can be Toast.LONG or TOAST.SHORT
	private int gravity; // custom toast gravity
	private Map<Integer,String> onomatopeias; // transcription between a sound id in the application environment and a string that represent it
	
	/**
	 * Creator for interface Parcelable. 
	 * 
	 * */
	public static final Parcelable.Creator<SubtitleInfo> CREATOR = new Parcelable.Creator<SubtitleInfo>() {
		public SubtitleInfo createFromParcel(Parcel in) {
			return new SubtitleInfo(in);
		}

		public SubtitleInfo[] newArray(int size) {
			return new SubtitleInfo[size];
		}
	};
	
	/**
	 * Default constructor.
	 * 
	 * */
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
	
	/**
	 * Unique constructor of the class.
	 * 
	 * @param resourceId custom toast id.
	 * @param viewgroupRoot custom toast root.
	 * @param id_text text id of the custom toast.
	 * @param xOffset values added to gravity.
	 * @param yOffset values added to gravity.
	 * @param duration custom toast duration can be Toast.LONG or TOAST.SHORT 
	 * @param gravity custom toast gravity
	 * @para onomatopeias transcription between a sound id in the application environment and a string that represent it
	 * 
	 * 
	 * */
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
	
	/**
	 * Parcel constructor.
	 * 
	 * */
	@SuppressWarnings("unchecked")
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

// ----------------------------------------------------------- Getters -----------------------------------------------------------
	
	public int getResourceId() {
		return resourceId;
	}
	
	public int getViewGroupRoot() {
		return viewGroupRoot;
	}
	
	public int getId_text() {
		return id_text;
	}
	
	public int getxOffset() {
		return xOffset;
	}
	
	public int getyOffset() {
		return yOffset;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public int getGravity() {
		return gravity;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public String getOnomatopeia(int resource) {
		if(onomatopeias != null)
			return onomatopeias.get(resource);
		else
			return null;
	}
// ----------------------------------------------------------- Setters -----------------------------------------------------------

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public void setViewGroupRoot(int viewGroupRoot) {
		this.viewGroupRoot = viewGroupRoot;
	}
	
	public void setId_text(int id_text) {
		this.id_text = id_text;
	}
	
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public void setGravity(int gravity) {
		this.gravity = gravity;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

// ----------------------------------------------------------- Others -----------------------------------------------------------
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
	
	public String toString() {
		String res = " enabled:" + enabled;
		res += " Rid:" + resourceId;
		res += " VGRid:" + viewGroupRoot;
		res += " Tid:" + id_text;
		res += " Offset x:" + xOffset;
		res += " Offset y:" + yOffset;
		res += " Duration:" + duration;
		res += " Gravity:" + gravity;
		if(onomatopeias != null)
			res += " Onomatopeias:" + true;
		else
			res += " Onomatopeias:" + false;
		return res;
	}
}
