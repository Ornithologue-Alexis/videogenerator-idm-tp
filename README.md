# TP IDM 

## Description
L’objectif du projet IDM est de développer un générateur ou configurateur de vidéos à partir d’une spécification textuelle (VideoGen).
Les utilisateurs pourront par exemple lire des variantes de vidéos générées aléatoirement ou avec des probabilités.

La concatenation des vidéos représente la journée d'un informaticien. 
Certains moments peuvent être optionel(Home-working)), obligatoire (Agile Board), ou alternatif(Android-scroll / iPhone-scroll))
Les videos ont été téléchargées sur le site http://coverr.co/ dans la rubrique tech.

## Pré-requis 
* Installation de ffmpeg
* Installation de VLC (Modifier dans la classe VideoGenUtils.java la variable vlcPath l.21 )

## Organisation des classes
* VideoGenTest.java : Classe de test commun à tous les TPs. 
* VideoGenTransformationUtils.java : Classe comprenant les méthodes de transformation des modèles de tous les TPs
* FfmpegUtils : Classe comprenant les méthodes d'utilisation de ffmpeg ainsi que des méthodes utils 
* VideoGenTransformationToAnalysis : Classe permettant la transformation de modèle (modèle to csv) du TP3
* Video.java
* Variant.java
Les méthodes sont annotées par le numéro du TP et de la question correspondante. 

## Organisation des ressources
* files : Contient l'ensemble des fichiers
   * analysis: répertoire contenant les fichiers CSV du TP3 
   * ffmpeg: répertoire contenant les fichiers.txt qui permettent la concatenation des vidéos
   * font: répertoire contant les polices utilisée lors de transformations
   * videoGen: répertoire contenant toutes les spécifications .videoGen définies pour les cas de test
* Medias
  * gifs: répertoire contenant les gifs générés (Attention ce répertoire doit-être vide avant de lancer les tests)
  * thumbnails: répertoire contenant les vignettes générées (Attention ce répertoire doit être vide avant de lancer les tests)
  * output: répertoire contenant les vidéos concaténées par ffmpeg (Attention ce répertoire doit-être vide avant de lancer les tests)
  * videos: répertoire contenant les vidéos utilisées (Inserer les vidéos dans ce répertoire)

