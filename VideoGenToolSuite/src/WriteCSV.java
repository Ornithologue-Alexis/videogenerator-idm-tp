import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;

public class WriteCSV {
	

	public static void GenerateCSV(String filename, VideoGeneratorModel videoGen) {
		
		Boolean idEstUnique = VideoGenToolSuite.verifyUniqueIds(videoGen);
		// On récupère l'ensemble des id des vidéos
		String listeDesId = String.join(";", VideoGenToolSuite.getAllVideosId(videoGen));
		// Les libellés des colonnes
		String colonnes = "Id;"+ listeDesId +";Taille";
		FileWriter fileWriter = null;
		try {	
			// Création du fichier texte, avec son nom en paramètre
			fileWriter = new FileWriter(filename);
			// On ajoute la ligne des libellés des colonnes
			fileWriter.append(colonnes);
			fileWriter.append("\n");
			// On boucle sur l'ensemble des variantes disponibles
			for (Variant variant : VideoGenToolSuite.getListeDesVariantes(videoGen)) {
				fileWriter.append(Integer.toString(variant.getId()));
				fileWriter.append( ";");
				// On regarde quelles vidéos sont contenues par la variante
				for (String videoId : VideoGenToolSuite.getAllVideosId(videoGen)) {
					boolean b = Ffmpeg.containsName(variant.getListeVideos(), videoId);
					fileWriter.append(Boolean.toString(b));
					fileWriter.append( ";");
				}
				// On créé un fichier qui répertorie les chemins des vidéos de la variante
				String listeVideosVariante = "";
				for (Video v : variant.getListeVideos()) {
					listeVideosVariante = listeVideosVariante + "file '" + v.getFile().getCanonicalPath() +"' \n";
				}

				// On créé une playlist qui permet d'aller chercher les vidéos de la variante
				String playlistPath = "assets\\playlists\\list-csv-variant" + variant.getId() + ".txt";
				Ffmpeg.createTextFile(listeVideosVariante, playlistPath);
		
				// On créé la variante en concatenant les vidéos
				String outputffmpeg = "output-csv-variant" + variant.getId() + ".mp4";
				Ffmpeg.startFfmpeg(playlistPath, outputffmpeg);
			
				// On récupère la taille de la variante créé
				File realFile = new File("medias\\outputs\\" + outputffmpeg);
				fileWriter.append(Long.toString(realFile.length()));
				fileWriter.append("\n");
	        }
			
			try {
	        	fileWriter.flush();
	            fileWriter.close();
			} catch (Exception e) {
	        e.printStackTrace();
			}
		} catch (Exception e) {
	        e.printStackTrace();
	    }	

	}
}
