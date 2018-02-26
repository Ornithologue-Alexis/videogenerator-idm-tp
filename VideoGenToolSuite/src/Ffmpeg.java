import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.xtext.example.mydsl.videoGen.VideoDescription;


class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    
    StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }
    
    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
                System.out.println(type + ">" + line);    
            } catch (IOException ioe)
              {
                ioe.printStackTrace();  
              }
    }
}

public class Ffmpeg {
	
	/**
	 * Ouverture de VLC pour lire une vidéo (TP2 - Question 1)
	 * 
	 * @param pathToVlc
	 * pathToVlc = "C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe";
	 * @param pathToVideo
	 */
	public static void startVLC(String pathToVlc, String pathToVideo) {
		String[] args = new String[] { pathToVlc, pathToVideo };
		try {
			Process process = Runtime.getRuntime().exec(args);
			try {
				StreamGobbler errorGobbler = new 
		                StreamGobbler(process.getErrorStream(), "ERROR");            
		            
		            // any output?
		            StreamGobbler outputGobbler = new 
		                StreamGobbler(process.getInputStream(), "OUTPUT");
		                
		            // kick them off
		            errorGobbler.start();
		            outputGobbler.start();
		                                    
		            // any error???
		            int exitVal = process.waitFor();
		            System.out.println("ExitValue: " + exitVal);   
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
				StreamGobbler errorGobbler = new 
		                StreamGobbler(process.getErrorStream(), "ERROR");            
		            
		            // any output?
		            StreamGobbler outputGobbler = new 
		                StreamGobbler(process.getInputStream(), "OUTPUT");
		                
		            // kick them off
		            errorGobbler.start();
		            outputGobbler.start();
		                                    
		            // any error???
		            int exitVal = process.waitFor();
		            System.out.println("ExitValue: " + exitVal);        
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
				StreamGobbler errorGobbler = new 
		                StreamGobbler(process.getErrorStream(), "ERROR");            
		            
		            // any output?
		            StreamGobbler outputGobbler = new 
		                StreamGobbler(process.getInputStream(), "OUTPUT");
		                
		            // kick them off
		            errorGobbler.start();
		            outputGobbler.start();
		                                    
		            // any error???
		            int exitVal = process.waitFor();
		            System.out.println("ExitValue: " + exitVal);   
					
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
		// Commande de création d'un gif
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
		// Commande de création d'une signature
		String command = "ffmpeg -i "+ pathToVideo +" -vf signature=filename=signature.bin -map 0:v -f null -";
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error ffmpeg filter");
		}
	}
	
	/**
	 * Return true s'il est contenu
	 * @param liste
	 * @param nom
	 * @return
	 */
	public static boolean nameIsContained(final List<Video> liste, final String nom) {
		// Sur la liste, on applique un filtre pour regarder si l'id est présent
		return liste.stream().filter(video -> video.getId().equals(nom)).findFirst().isPresent();
	}

	
	/**
	 * Nombre de lignes d'un fichier CSV
	 * @param CSVpathName
	 * @return
	 */
	public static int lineNumberCSV(String pathCSV) {
		int nbLignes = 0;
		// On boucle grâce à un BufferedReader
	    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathCSV))) {
	    	while ((bufferedReader.readLine()) != null) {
	    		// Tant que l'on atteint pas la fin, on boucle et on incrémente
	    		nbLignes++;
	    	}
	     } catch (IOException e) {
	    	 e.printStackTrace();
	     }
	     return nbLignes;
	}
	
	/**
	 * Créer un fichier texte à partir d'une string
	 * 
	 * @param texte
	 * @param nomDuFichier
	 * @throws IOException
	 */
	public static void createTextFile(String texte, String nomDuFichier) throws IOException {
		// On créé un fichier texte
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
		// On créé une vidéo avec un id
		File fichier = new File(description.getLocation());
		String id = description.getVideoid();
		return new Video(id, fichier);
	}
	
}
