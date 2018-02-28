package miage.fr.binome;
import java.util.ArrayList;

public class Variant {

	private int id;
	private ArrayList<Video> listeVideos;
	
	public Variant(int id) {
		this.id = id;
		this.listeVideos = new ArrayList<>();
	}
	
	public Variant(int id, ArrayList<Video> listeVideos) {
		this.id = id;
		this.listeVideos = listeVideos;
	}
	
	public ArrayList<Video> getListeVideos() {
		return new ArrayList<>(listeVideos);
	}	
	
	public void addVideo(Video video) {
		this.listeVideos.add(video);
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Variant [id=" + id + ", videosList=" + listeVideos + "]";
	}
	
}
