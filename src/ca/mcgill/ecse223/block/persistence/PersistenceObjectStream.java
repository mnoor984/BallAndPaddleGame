package ca.mcgill.ecse223.block.persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PersistenceObjectStream {

	private static String filename = "output.txt";

	public static void serialize(Object object) {
		FileOutputStream fileout;
		try {
			fileout = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(fileout);
			out.writeObject(object);
			out.close();
			fileout.close();
		} 
		catch (Exception e){
			throw new RuntimeException("Could not save the game to file ' " + filename + "'.");
		}

	}
	public static Object deserialize() {
		Object o = null;
		ObjectInputStream in;
		try {
			FileInputStream fileIn = new FileInputStream(filename);
			in = new ObjectInputStream(fileIn);
			o = in.readObject();
			in.close();
			fileIn.close();
		} catch (Exception e) {
			o = null;
		}
		return o;
	}

	public static void setFilename(String newFilename) {
		filename = newFilename;
	}

}
