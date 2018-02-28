package miage.fr.binome;
import java.io.File;

public class Video {

	private String id;
	private File file;
	
	public Video(String id, File file) {
		this.id = id; 
		this.file = file;
	}
	
	public String getId() {
		return this.id;
	}
	
	public File getFile() {
		return this.file;
	}
	
	public String toString() {
		return id;
	}
}
