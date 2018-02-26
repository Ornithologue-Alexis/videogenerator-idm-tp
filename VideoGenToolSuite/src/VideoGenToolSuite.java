import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq;
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq;
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq;
import org.xtext.example.mydsl.videoGen.VideoDescription;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;
import org.xtext.example.mydsl.videoGen.VideoSeq;

public class VideoGenToolSuite {

	/**
	 * Transformation model-to-text (TP2 - Question 2)
	 * @param videoGen
	 * @param nomfichier
	 * @throws IOException 
	 * @return le fichier contenant la playlist
	 */
	public static String getFileForFfmpeg(VideoGeneratorModel videoGen, String nomfichier) throws IOException {
		String playlist = "";
		// On r�cup�re l'ensemble des vid�os
		List<VideoSeq> videos = videoGen.getVideoseqs();
		for(VideoSeq video : videos) {
			// Video mandatory
			if(video instanceof MandatoryVideoSeq) {
				playlist = playlist + "file '" + ((MandatoryVideoSeq)video).getDescription().getLocation() +"' \n";
			}
			// Video optionnelle
			if(video instanceof OptionalVideoSeq) {
				// Cr�ation d'un nombre al�atoire, compris entre 0 et 1
				int number = (int)(Math.random() * 2);
				if(number == 1) {	
					playlist = playlist + "file '" + ((OptionalVideoSeq)video).getDescription().getLocation() +"' \n";
				}	
			}
			// Video alternative
			if(video instanceof AlternativeVideoSeq) {
				List<VideoDescription> listeDescription = ((AlternativeVideoSeq)video).getVideodescs();
				// On cr�� un nombre al�atoire � partir de la variable listeDescription
				int alternative =  (int)(Math.random() * listeDescription.size());
				playlist = playlist + "file '" + listeDescription.get(alternative).getLocation() +"' \n";				
			}			
		}
		
		// On �crit la playlist obtenue dans un fichier
		String playlistFichier = "assets\\playlists\\" + nomfichier + ".txt";
		Ffmpeg.createTextFile(playlist, playlistFichier);
		return playlistFichier;
	}
	
	/**
	 * Calcul de la dur�e maximale d�une variante de vid�o (TP2 - Question 3)
	 * @param videoGen
	 * @return dur�e
	 */
	public static Double getMaxDuration(VideoGeneratorModel videoGen) {
		Double dureeVariante = 0.0;
		// On r�cup�re l'ensemble des vid�os
		List<VideoSeq> videos = videoGen.getVideoseqs();
		for(VideoSeq video : videos) {
			// Video mandatory
			if(video instanceof MandatoryVideoSeq) {
				VideoDescription description = ((MandatoryVideoSeq)video).getDescription();
				dureeVariante = dureeVariante + Ffmpeg.dureeVideo(description.getLocation());
			}
			// Video optionnelle
			if(video instanceof OptionalVideoSeq) {
				VideoDescription description = ((OptionalVideoSeq)video).getDescription();
				dureeVariante = dureeVariante + Ffmpeg.dureeVideo(description.getLocation());
			}
			// Video alternative
			if(video instanceof AlternativeVideoSeq) {
				List<VideoDescription> listDescription = ((AlternativeVideoSeq)video).getVideodescs();
				Double dureeMax = 0.0;
				for(VideoDescription description : listDescription){
					// On r�cup�re la dur�e d'une vid�o alternative
					Double duree = Ffmpeg.dureeVideo(description.getLocation());
					// On utilise ce if pour r�cup�rer la vid�o alternative ayant la plus longue dur�e
					if(duree > dureeMax) {
						dureeMax = duree;
					}
				}
				dureeVariante = dureeVariante + dureeMax;
			}
		}
		return dureeVariante;
	}
	
	/**
	 * Transformation qui prend en entr�e une sp�cification VideoGen et qui g�n�re en sortie un ensemble de vignettes (TP2 - Question 4)
	 * @param videoGen
	 * @return
	 */
	public static void thumbnailsFromAModel(VideoGeneratorModel videoGen) {
		List<VideoSeq> videos = videoGen.getVideoseqs();
		for(VideoSeq video : videos) {
			// Video mandatory
			if(video instanceof MandatoryVideoSeq) {
				VideoDescription videoDescription = ((MandatoryVideoSeq)video).getDescription();
				Ffmpeg.creerMiniature(videoDescription.getLocation(), "mandatory-thumbnail" + videoDescription.getVideoid() + ".jpg");
			}
			// Video optionnelle
			if(video instanceof OptionalVideoSeq) {
				VideoDescription videoDescription = ((OptionalVideoSeq)video).getDescription();
				Ffmpeg.creerMiniature(videoDescription.getLocation(), "optional-thumbnail" + videoDescription.getVideoid() + ".jpg");
			}
			// Video alternative
			if(video instanceof AlternativeVideoSeq) {
				List<VideoDescription> listeDescription = ((AlternativeVideoSeq)video).getVideodescs();
				for(VideoDescription videoDescription : listeDescription) {
					Ffmpeg.creerMiniature(videoDescription.getLocation(), "alternative-thumbnail" + videoDescription.getVideoid() + ".jpg");
				}
			}
		}
	}
	
	
	/**
	 * Transformation qui prend en entr�e une sp�cification VideoGen et qui g�n�re en sortie une page Web affichant les vignettes (TP2 - Question )
	 * 
	 * @return
	 */
	public static void createWebPage(VideoGeneratorModel videoGen) {
		// Pas fait
	}
	
	
	/**
	 * Fournis l'ensemble des variantes pour un mod�le donn� (TP3 - Question 1)
	 * @param videoGen
	 * Utilis� dans WriteCSV
	 */
	public static List<Variant> getListeDesVariantes(VideoGeneratorModel videoGen) {

		// Liste de toutes les variantes possibles
		List<Variant> variantes = new ArrayList<>();

		// On boucle sur l'ensemble des vid�os du mod�le
		List<VideoSeq> videos = videoGen.getVideoseqs();
		for (VideoSeq video : videos) {
			// Video mandatory
			if (video instanceof MandatoryVideoSeq) {
				VideoDescription videoDescription = ((MandatoryVideoSeq)video).getDescription();
				Video videoMandatory = Ffmpeg.creationVideo(videoDescription);
				// variantes vide
				if (variantes.isEmpty()) {
					// On cr�� une nouvelle variante
					Variant variante = new Variant(variantes.size());
					// On ajoute la vid�o mandatory
					variante.addVideo(videoMandatory);
					variantes.add(variante);
				}
				// S'il y a d�j� des variantes de cr��, on ajoute la vid�o mandatory dans TOUTES les variantes
				else {
					for (Variant variante : variantes) {
						variante.addVideo(videoMandatory);
					}
				}
			}

			// Video optionnelle
			if (video instanceof OptionalVideoSeq) {
				VideoDescription videoDescription = ((OptionalVideoSeq) video).getDescription();
				Video videoOptionnelle = Ffmpeg.creationVideo(videoDescription);
				// variantes vide
				if (variantes.isEmpty()) {
					// On cr�� une variante vide
					variantes.add(new Variant(variantes.size()));

					// On ajoute une variante avec la vid�o optionnelle
					Variant variant = new Variant(variantes.size());
					variant.addVideo(videoOptionnelle);
					variantes.add(variant);
				}
				// S'il y a des d�j� des variantes cr��es, on va boucler sur une copie de la liste, et ajouter la vid�o dans l'ensemble des variantes
				else {
					// La copie va nous permettre de boucler 
					List<Variant> copieDeVariantes = new ArrayList<>(variantes);
					for (Variant variante : copieDeVariantes) {
						// On rajoute la vid�o � la variante, puis la variante � la liste
						Variant varianteMAJ = new Variant(variantes.size(), variante.getListeVideos());
						varianteMAJ.addVideo(videoOptionnelle);
						variantes.add(varianteMAJ);
					}
				}
			}

			// Video alternative
			if (video instanceof AlternativeVideoSeq) {
				// On r�cup�re la liste des vid�os alternatives
				List<VideoDescription> listVideoDescription = ((AlternativeVideoSeq)video).getVideodescs();

				// variantes est vide
				if (variantes.isEmpty()) {
					// On boucle sur les vid�os alternatives
					for (VideoDescription videoDescription : listVideoDescription) {
						// On cr�� la vid�o
						Video videoAlternative = Ffmpeg.creationVideo(videoDescription);
						// On cr�� la variante
						Variant variantAlternative = new Variant(variantes.size());
						// On y ajoute la vid�o
						variantAlternative.addVideo(videoAlternative);
						// On ajoute la variante � la liste des variantes
						variantes.add(variantAlternative);
					}
				} else {
					// S'il y a des d�j� des variantes cr��es, on va boucler sur une copie de la liste, et ajouter la vid�o dans l'ensemble des variantes
					List<Variant> copieDeVariantes = new ArrayList<>(variantes);
					// On vide la liste des variantes
					variantes = new ArrayList<>();

					// On boucle sur la copie
					for (Variant variant : copieDeVariantes) {
						// On boucle sur les vid�os alternatives
						for (VideoDescription videoDescription : listVideoDescription) {
							// On cr�� la variante � partir des vid�os de la variante sur laquelle on boucle
							Variant copieVariante = new Variant(variantes.size(), variant.getListeVideos());
							Video videoFile = Ffmpeg.creationVideo(videoDescription);
							copieVariante.addVideo(videoFile);
							// On ajoute la vid�o
							variantes.add(copieVariante);
						}
					}
				}
			}
		}
		return variantes;
	}
	
	
	
	/**
	 * Transformation d'une variante de vid�os en GIF (TP3 - Question 3)
	 * @param videoGen
	 * @return
	 */
	public static void gifFromAModel(VideoGeneratorModel videoGen) {
		List<VideoSeq> videos = videoGen.getVideoseqs();
		for(VideoSeq video : videos) {
			// Video mandatory
			if(video instanceof MandatoryVideoSeq) {
				VideoDescription videoDescription = ((MandatoryVideoSeq)video).getDescription();
				Ffmpeg.generateGif(videoDescription.getLocation(), "mandatory-gif" + videoDescription.getVideoid() + ".gif");
			}
			// Video optionnelle
			if(video instanceof OptionalVideoSeq) {
				VideoDescription videoDescription = ((OptionalVideoSeq)video).getDescription();
				Ffmpeg.generateGif(videoDescription.getLocation(), "optional-gif" + videoDescription.getVideoid() + ".gif");
			}
			// Video alternative
			if(video instanceof AlternativeVideoSeq) {
				List<VideoDescription> listDescription = ((AlternativeVideoSeq)video).getVideodescs();
				for(VideoDescription videoDescription : listDescription) {
					Ffmpeg.generateGif(videoDescription.getLocation(), "alternative-gif" + videoDescription.getVideoid() + ".gif");
				}
			}
		}
	}
		

	/**
	 * Get all videos IDs 
	 * @param videoGen
	 * @return List of Strings
	 */
	public static List<String> getAllVideosId(VideoGeneratorModel videoGen) {
		List<VideoSeq> videos = videoGen.getVideoseqs();
		List<String> idVideos = new ArrayList<>();
		for (VideoSeq video : videos) {		
			// Video mandatory
			if (video instanceof MandatoryVideoSeq) {
				VideoDescription description = ((MandatoryVideoSeq)video).getDescription();
				idVideos.add(description.getVideoid());
			}
			
			// Video optionnelle
			if (video instanceof OptionalVideoSeq) {
				VideoDescription description = ((OptionalVideoSeq)video).getDescription();
				idVideos.add(description.getVideoid());
			}
			
			// Video alternative
			if (video instanceof AlternativeVideoSeq) {
				List<VideoDescription> listeVideoDescription = ((AlternativeVideoSeq)video).getVideodescs();
				for (VideoDescription description : listeVideoDescription) {
					idVideos.add(description.getVideoid());
				}
			}	
		}
		return idVideos;
	}
	
	
	
	/**
	 * Fonction qui compte le nombre de variantes possibles sans �num�rer les solutions (TP4 - Question 1)
	 * @param videoGen
	 * @return nbVariantes
	 */
	public static int nbVariantes(VideoGeneratorModel videoGen) {
		int nbVariantes = 0;
		// Si le mod�le n'est pas vide il contient au moins une variante
		if (videoGen.getVideoseqs().size() > 0) 
		{ 
			nbVariantes = 1; 
		}

		List<VideoSeq> videos = videoGen.getVideoseqs();
		// On parcourt ensuite les vid�os pr�sentes dans le mod�le
		// Quand la vid�o est mandatory, le nombre de variantes ne change pas
		for (VideoSeq video : videos) {
			// Video optionnel
			if (video instanceof OptionalVideoSeq) {
				// on double le nombre de variantes
				nbVariantes = nbVariantes * 2;
			}
			// Video alternative
			if (video instanceof AlternativeVideoSeq) {
				// nombre de variantes est multipli� par le nombre d�alternatives possibles
				nbVariantes = nbVariantes * ((AlternativeVideoSeq)video).getVideodescs().size();
			}
		}
		return nbVariantes;
	}
	
	
	/**
	 * T4 - Q6 Transformation qui prend en entr�e une sp�cification VideoGen et qui v�rifie que les identifieurs doivent �tre uniques
	 * @param videoGen
	 * @return boolean
	 */
	public static boolean verifyUniqueIds(VideoGeneratorModel videoGen) {
		List<String> ids = new ArrayList<>();
		List<VideoSeq> videos = videoGen.getVideoseqs();
		
		// On boucle sur les vid�os
		for (VideoSeq video : videos) {
			// Video mandatory
			if (video instanceof MandatoryVideoSeq) {
				VideoDescription videoDescription = ((MandatoryVideoSeq)video).getDescription();
				// Si l'id de la vid�o existe d�j�, on retourne false
				if(ids.contains(videoDescription.getVideoid())) 
				{ 
					return false; 
				}
				// S'il n'existe pas, on l'ajoute
				ids.add(videoDescription.getVideoid());
			}
			
			// Video optionnelle
			if (video instanceof OptionalVideoSeq) {
				VideoDescription videoDescription = ((OptionalVideoSeq) video).getDescription();
				// Si l'id de la vid�o existe d�j�, on retourne false
				if(ids.contains(videoDescription.getVideoid())) 
				{ 
					return false; 
				}
				// S'il n'existe pas, on l'ajoute
				ids.add(videoDescription.getVideoid());
			}
			
			// Video alternative
			if (video instanceof AlternativeVideoSeq) {
				List<VideoDescription> listeVideoDescription = ((AlternativeVideoSeq) video).getVideodescs();
				// Si l'id de la vid�o alternative existe d�j�, on retourne false
				if(ids.contains(((AlternativeVideoSeq)video).getVideoid())) 
				{ 
					return false; 
				}
				// S'il n'existe pas, on l'ajoute
				ids.add(((AlternativeVideoSeq) video).getVideoid());
				
				// On boucle sur la liste de vid�os alternative
				for (VideoDescription videoDescription : listeVideoDescription) {
					// Si l'id de la vid�o alternative existe d�j�, on retourne false
					if(ids.contains(videoDescription.getVideoid())) 
					{ 
						return false; 
					}
					// S'il n'existe pas, on l'ajoute
					ids.add(videoDescription.getVideoid());
				}
			}	
		}
		return true;
	}
	
}
