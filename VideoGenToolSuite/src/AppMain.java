import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.xtext.example.mydsl.videoGen.VideoDescription;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;
import org.xtext.example.mydsl.videoGen.VideoSeq;

public class AppMain{
 
    public static void main(String[] args) throws IOException {
    	VideoGenToolSuite videoGenToolSuite = new VideoGenToolSuite();
    	VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("videogen.videogen"));
    	List<Variant> variantes = VideoGenToolSuite.getListeDesVariantes(videoGen);
    	String playlist = "";
    	String commande = args[0];
    	if(args.length > 0){
            if(commande.equals("vignette")) {
            	videoGenToolSuite.thumbnailsFromAModel(videoGen);
            }
            if(commande.equals("variante")) {
            	int cpt = (int) Math.floor(Math.random() * variantes.size());
            	Variant variante = variantes.get(cpt);
            	List<Video> videoseq = variante.getListeVideos();
            	playlist = videoGenToolSuite.getFileForFfmpegVariante(videoseq, "random");
            	Ffmpeg.startFfmpeg(playlist, "videoRandom.mp4");
            }
        }
    }
 
}