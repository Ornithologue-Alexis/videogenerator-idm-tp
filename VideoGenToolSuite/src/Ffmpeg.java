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
	 * Ouverture de VLC pour lire une vid�o (TP2 - Question 1)
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
	 * Utilisation de ffmpeg pour compiler des vid�os (TP2 - Question 2)
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
	 * Dur�e d'une vid�o (TP2 - Question 3)
	 * 
	 * @param pathToVideo
	 * @return la dur�e de la vid�o
	 */
	public static Double dureeVideo(String pathToVideo) {
		String command = "ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 " + pathToVideo;
		Double duree = 0.0;
		try {
			Process process = Runtime.getRuntime().exec(command);
			// On r�cup�re la dur�e de la vid�o � l'aide d'un BufferedReader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			duree = Double.parseDouble(bufferedReader.readLine());
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				System.out.println("Erreur du process.waitFor()");
			}
			// On n'oublie pas de fermer le BufferedReader une fois utilis�
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return duree;
	}
	

	/**
	 * Cr�ation des miniatures de vid�os (Thumbnail) (TP2 - Question 4)
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
			System.out.println("Erreur lors de la g�n�ration de la miniature");
		}
	}

	/**
	 * Cr�ation d'un gif � partir de la vid�o (TP3 - Question 3)
	 */
	public static void generateGif(String pathToVideo, String gifNom) {
		String command = "ffmpeg -v warning -ss 1 -t 2 -i  " + pathToVideo + " -vf scale=200:-1 -gifflags -transdiff -y assets\\gifsCreated\\" + gifNom;
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erreur lors de la cr�ation du GIF");
		}
	}
	
	/**
	 * R�aliser une impl�mentation � l�aide des filtres ffmpeg (TP4 - Question 7) 
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
	 * Cr�er un fichier texte � partir d'une string
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
