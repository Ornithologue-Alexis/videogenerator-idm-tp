import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.eclipse.emf.common.util.URI;
import org.junit.Test;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;



public class VideoGenTest {
	
	
	/**
	 * Test transformation model-to-text (TP2 - Question 2)
	 * @throws IOException 
	 */
	@Test
	public void testGetFileForFfmpeg() throws IOException {

		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("./videoGen/videogen.videogen"));
		String playlist = VideoGenToolSuite.getFileForFfmpeg(videoGen, "testQuestion2");
		
		// On cr�� une concat�nation entre la playlist a la vid�o
		String ffmpegVideo = "testQuestion2.mp4";
		Ffmpeg.startFfmpeg(playlist, ffmpegVideo);
		
		// True si pas d'erreurs
		assertTrue("Test question 2", true);
	}
	

	/**
	 * Test de la cr�ation de gifs (TP3 - Question 3)
	 * @throws InterruptedException
	 */
	@Test
	public void testCreationGifs() throws InterruptedException {
		
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("./videoGen/videoGen.videogen"));
		VideoGenToolSuite.gifFromAModel(videoGen);
		
		// On r�cup�re les ids des vid�os du mod�le
		List<String> ids =  VideoGenToolSuite.getAllVideosId(videoGen);
		
		// On regarde si tous les gifs ont �t� cr��s dans le fichier
		int countGifs = new File ("assets/gifsCreated/").list().length;
		
		assertEquals("Test gifs", ids.size(), countGifs);
	}
	
	/**
	 * Test de nombre de variantes sur un �chantillon de test (TP4 - Question 2)
	 */
	@Test
	public void testVariants() {
		
		// Une vid�o mandatory
		VideoGeneratorModel videoGeneratorModel1 = new VideoGenHelper().loadVideoGenerator(URI.createURI("./videoGen/oneMandatory.videogen"));
		List<Variant> listeVariantes1 = VideoGenToolSuite.getListeDesVariantes(videoGeneratorModel1);
		assertEquals("Une vid�o mandatory", listeVariantes1.size(), VideoGenToolSuite.nbVariantes(videoGeneratorModel1));
		
		// Une vid�o optionnelle
		VideoGeneratorModel videoGeneratorModel2 = new VideoGenHelper().loadVideoGenerator(URI.createURI("./videoGen/oneOptional.videogen"));
		List<Variant> listeVariantes2 = VideoGenToolSuite.getListeDesVariantes(videoGeneratorModel2);
		assertEquals("Une vid�o optionnelle", listeVariantes2.size(), VideoGenToolSuite.nbVariantes(videoGeneratorModel2));
		
		// Une vid�o alternative
		VideoGeneratorModel videoGeneratorModel3 = new VideoGenHelper().loadVideoGenerator(URI.createURI("./videoGen/oneAlternative.videogen"));
		List<Variant> listeVariantes3 = VideoGenToolSuite.getListeDesVariantes(videoGeneratorModel3);
		assertEquals("Une vid�o alternative", listeVariantes3.size(), VideoGenToolSuite.nbVariantes(videoGeneratorModel3));
		
		// Seulement des vid�os mandatory
		VideoGeneratorModel videoGeneratorModel4 = new VideoGenHelper().loadVideoGenerator(URI.createURI("./videoGen/mandatory.videogen"));
		List<Variant> listeVariantes4 = VideoGenToolSuite.getListeDesVariantes(videoGeneratorModel4);
		assertEquals("Seulement des vid�os mandatory", listeVariantes4.size(), VideoGenToolSuite.nbVariantes(videoGeneratorModel4));
		
		// Seulement des vid�os optionnelles
		VideoGeneratorModel videoGeneratorModel5 = new VideoGenHelper().loadVideoGenerator(URI.createURI("./videoGen/optional.videogen"));
		List<Variant> listeVariantes5 = VideoGenToolSuite.getListeDesVariantes(videoGeneratorModel5);
		assertEquals("Seulement des vid�os optionnelles", listeVariantes5.size(), VideoGenToolSuite.nbVariantes(videoGeneratorModel5));
		
		// Seulement des vid�os alternatives
		VideoGeneratorModel videoGeneratorModel6 = new VideoGenHelper().loadVideoGenerator(URI.createURI("./videoGen/alternative.videogen"));
		List<Variant> listeVariantes6 = VideoGenToolSuite.getListeDesVariantes(videoGeneratorModel6);
		assertEquals("Seulement des vid�os alternatives", listeVariantes6.size(), VideoGenToolSuite.nbVariantes(videoGeneratorModel6));
		
		// Vid�os mandatory et optionnelles
		VideoGeneratorModel videoGeneratorModel7 = new VideoGenHelper().loadVideoGenerator(URI.createURI("./videoGen/mandatoryOptional.videogen"));
		List<Variant> listeVariantes7 = VideoGenToolSuite.getListeDesVariantes(videoGeneratorModel7);
		assertEquals("Vid�os mandatory et optionnelles", listeVariantes7.size(), VideoGenToolSuite.nbVariantes(videoGeneratorModel7));

		// Vid�os mandatory et alternatives
		VideoGeneratorModel videoGeneratorModel8 = new VideoGenHelper().loadVideoGenerator(URI.createURI("./videoGen/mandatoryAlternative.videogen"));
		List<Variant> listeVariantes8 = VideoGenToolSuite.getListeDesVariantes(videoGeneratorModel8);
		assertEquals("Vid�os mandatory et alternatives", listeVariantes8.size(), VideoGenToolSuite.nbVariantes(videoGeneratorModel8));
		
		// Vid�os alternatives et optionnelles
		VideoGeneratorModel videoGeneratorModel9 = new VideoGenHelper().loadVideoGenerator(URI.createURI("./videoGen/optionalAlternative.videogen"));
		List<Variant> listeVariantes9 = VideoGenToolSuite.getListeDesVariantes(videoGeneratorModel9);
		assertEquals("Vid�os alternatives et optionnelles", listeVariantes9.size(), VideoGenToolSuite.nbVariantes(videoGeneratorModel9));
		
		// Les trois types de vid�os
		VideoGeneratorModel videoGeneratorModel10 = new VideoGenHelper().loadVideoGenerator(URI.createURI("./videoGen/videogen.videogen"));
		List<Variant> listeVariantes10 = VideoGenToolSuite.getListeDesVariantes(videoGeneratorModel10);
		assertEquals("Les trois types de vid�os", listeVariantes10.size(), VideoGenToolSuite.nbVariantes(videoGeneratorModel10));
	}
	
	/**
	 * Cas de test pour v�rifier que le nombre de vignettes produites (fin du TP2) est bien �gal au nombre de �medias�. (TP4 - Question 3)
	 * @throws InterruptedException 
	 */
	@Test
	public void testNbLignesCSV() throws InterruptedException {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("./videoGen/videogen.videogen"));
		// On g�n�re le CSV de test
		WriteCSV.GenerateCSV("assets\\test_csv.csv", videoGen);
		
		// On r�cup�re le nombre de lignes de notre CSV
		int nbLignes = Ffmpeg.lineNumberCSV("assets\\test_csv.csv");
		List<Variant> listeVariante = VideoGenToolSuite.getListeDesVariantes(videoGen);
		
		// +1 car la fonction size commence � 0
		assertEquals("test nombre de lignes CSV",  nbLignes, listeVariante.size() + 1);
	}
	
	/**
	 * test pour v�rifier que le nombre de vignettes produites (fin du TP2) (TP4 - Question )
	 * @throws InterruptedException
	 */
	@Test
	public void testThumbnails() throws InterruptedException {
		
		// M�me principe que le test des gifs. On g�n�re les thumbnails � partir de la m�me grammaire, on check la taille
		// du folder, et on compare avec les id
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("./videoGen/videogen.videogen"));
		VideoGenToolSuite.thumbnailsFromAModel(videoGen);
		
		// On r�cup�re les ids des vid�os
		List<String> ids =  VideoGenToolSuite.getAllVideosId(videoGen);
		
		// On compte les nombre de miniatures cr��s
		int countThumbnails = new File ("assets/thumbnails/").list().length;
		
		assertEquals("Test nombre de thumbnails cr��s", countThumbnails, ids.size());
	}
	
}
