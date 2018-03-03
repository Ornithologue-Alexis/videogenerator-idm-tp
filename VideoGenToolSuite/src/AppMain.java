import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq;
import org.xtext.example.mydsl.videoGen.VideoDescription;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;
import org.xtext.example.mydsl.videoGen.VideoSeq;

public class AppMain{
 
    public static void main(String[] args) throws IOException {
    	VideoGenToolSuite videoGenToolSuite = new VideoGenToolSuite();
    	VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("server/ressources/videogen.videogen"));
    	List<Variant> variantes = VideoGenToolSuite.getListeDesVariantes(videoGen);
    	String playlist = "";
    	String commande = args[0];
    	Variant varianteCreer = new Variant(666);
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
            if(commande.equals("creervideo")) {
            	String playlistCreer = "";
            	outerloop:
            	for(int i = 0; variantes.size() > i ; i++ ) {
            		if((!args[2].equals("fonte") || !args[2].equals("guinea_pig")) && variantes.get(i).getListeVideos().size() == 3) {
            			for(int j = 0; variantes.get(i).getListeVideos().size() > j ; j++ ) {
            				if (args[3].equals("fonte") && variantes.get(i).getListeVideos().get(2).toString().equals("v31")) {
            					varianteCreer = variantes.get(i);
            					break outerloop;
            				} else if (args[3].equals("guinea_pig") && variantes.get(i).getListeVideos().get(2).toString().equals("v32")) {
            					varianteCreer = variantes.get(i);
            					break outerloop;
            				}
            			}
            		} else {
            			for(int j = 0; variantes.get(i).getListeVideos().size() > j ; j++ ) {
            				if (args[2].equals("fonte") && variantes.get(i).getListeVideos().get(1).toString().equals("v31")) {
            					varianteCreer = variantes.get(i);
            					break outerloop;
            				} else if (args[2].equals("guinea_pig") && variantes.get(i).getListeVideos().get(1).toString().equals("v32")) {
            					varianteCreer = variantes.get(i);
            					break outerloop;
            				}
            			}
            		}
            	}
            	
            	List<Video> videoseq = varianteCreer.getListeVideos();
            	playlist = videoGenToolSuite.getFileForFfmpegVariante(videoseq, "create");
            	Ffmpeg.startFfmpeg(playlist, "videoCreate.mp4");
            }
        }
    }
 
}