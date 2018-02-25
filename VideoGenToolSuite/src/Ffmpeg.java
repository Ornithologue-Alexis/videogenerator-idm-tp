import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq;
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq;
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq;
import org.xtext.example.mydsl.videoGen.Text;
import org.xtext.example.mydsl.videoGen.VideoDescription;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;
import org.xtext.example.mydsl.videoGen.VideoSeq;

public class Ffmpeg {
	
	private String pathToVlc = "C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe";
	
	/**
	 * Ouverture de VLC pour lire une vidéo (TP2 - Question 1)
	 * 
	 * @param pathToVlc
	 * @param pathToVideo
	 */
	public static void startVLC(String pathToVlc, String pathToVideo) {
		String[] args = new String[] { pathToVlc, pathToVideo };
		try {
			Process process = Runtime.getRuntime().exec(args);
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				System.out.println("Erreur du process.waitFor()");
			}
		} catch (IOException e) {
			System.out.println("Erreur survenue lors du lancement de VLC");
		}
	}
	
	/**
	 * Utilisation de ffmpeg pour compiler des vidéos (TP2 - Question 2)
	 * 
	 * @param ffmpegPlaylist
	 * @param fichierDeSortie
	 */
	public static void startFfmpeg(String ffmpegPlaylist, String fichierDeSortie) {
		String command = "ffmpeg -y -f concat -safe 0 -i " + ffmpegPlaylist + " -c copy assets\\videosCreated\\" + fichierDeSortie;
		try {
			Process process = Runtime.getRuntime().exec(command);
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				System.out.println("Erreur du process.waitFor()");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erreur survenue lors du lancement de la compilation ffmpeg");
		}
	}


	/**
	 * Durée d'une vidéo (TP2 - Question 3)
	 * 
	 * @param pathToVideo
	 * @return la durée de la vidéo
	 */
	public static Double dureeVideo(String pathToVideo) {
		String command = "ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 " + pathToVideo;
		Double duree = 0.0;
		try {
			Process process = Runtime.getRuntime().exec(command);
			// On récupère la durée de la vidéo à l'aide d'un BufferedReader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			duree = Double.parseDouble(bufferedReader.readLine());
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				System.out.println("Erreur du process.waitFor()");
			}
			// On n'oublie pas de fermer le BufferedReader une fois utilisé
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return duree;
	}
	

	/**
	 * Création des miniatures de vidéos (Thumbnail) (TP2 - Question 4)
	 * 
	 * @param pathToVideo
	 * @param miniatureNom
	 * @return
	 */
	public static void creerMiniature(String pathToVideo, String miniatureNom) {
		String commande = "ffmpeg -i " + pathToVideo + " -ss 00:00:05 -vframes 1  assets\\thumbnails\\" + miniatureNom;
		try {
			Runtime.getRuntime().exec(commande);
		} catch (IOException e) {
			System.out.println("Erreur lors de la génération de la miniature");
		}
	}

	/**
	 * Création d'un gif à partir de la vidéo (TP3 - Question 3)
	 */
	public static void generateGif(String pathToVideo, String gifNom) {
		String command = "ffmpeg -v warning -ss 1 -t 2 -i  " + pathToVideo + " -vf scale=200:-1 -gifflags -transdiff -y assets\\gifsCreated\\" + gifNom;
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erreur lors de la création du GIF");
		}
	}
	
	/**
	 * Réaliser une implémentation à l’aide des filtres ffmpeg (TP4 - Question 7) 
	 * @param pathToVideo
	 */
	public static void calculateSignatureOfAnInputVideo(String pathToVideo) {
		String command = "ffmpeg -i "+ pathToVideo +" -vf signature=filename=signature.bin -map 0:v -f null -";
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error ffmpeg filter");
		}
	}
	
	/**
	 * Créer un fichier texte à partir d'une string
	 * 
	 * @param texte
	 * @param nomDuFichier
	 * @throws IOException
	 */
	public static void createTextFile(String texte, String nomDuFichier) throws IOException {
		File fichier = new File(nomDuFichier);
		FileWriter fichierTexte = new FileWriter(fichier.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fichierTexte);
		bw.write(texte);
		bw.close();
	}
	
	/**
	 * Create a videoFile 
	 * @param description
	 * @return
	 */
	public static Video creationVideo(VideoDescription description) {
		File fichier = new File(description.getLocation());
		String id = description.getVideoid();
		return new Video(id, fichier);
	}
	
	/**
	 * Return if the name is contain in a List<Video> 
	 * @param list
	 * @param name
	 * @return Boolean
	 */
	public static boolean containsName(final List<Video> list, final String name) {
		return list.stream().filter(o -> o.getId().equals(name)).findFirst().isPresent();
	}

	
	/**
	 * Get the line number of csv
	 * @param CSVpathName
	 * @return
	 */
	public static int getCsvLineNumber(String CSVpathName) {
		int lineNumber = 0;
	    try (BufferedReader br = new BufferedReader(new FileReader(CSVpathName))) {
	    	while ((br.readLine()) != null) {
	    		lineNumber++;
	    	}
	     } catch (IOException e) {
	    	 e.printStackTrace();
	     }
	     return lineNumber;
	}
}
