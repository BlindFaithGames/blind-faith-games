package es.eucm.blindfaithgames.engine.feedback;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import android.content.Context;

public class Id {
	
	private Context ctxt;
	
	private UUID id;
	
	public Id(Context ctxt){
		this.ctxt = ctxt;
	}
	
	public String checkId(String name) {
		id = null;
		FileInputStream fis;
		try { 
			fis = ctxt.openFileInput(name);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object f = ois.readObject();
			id = (UUID) f;
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(id == null){
			id = UUID.randomUUID();
			
			save(name,id);
		}
		return id.toString();
	}
	
	private void save(String name, UUID id) {
		FileOutputStream fos;
		try { 
			fos = ctxt.openFileOutput(name, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(id); 
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
