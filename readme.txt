Contenu de l'archive
 - ponts : code source du jeu, organisé en plusieurs sous dossiers
 - res : ressources du jeu, comprenant les images et les fichiers des niveaux
 - lib : librairies utilisées pour le jeu, à savoir jbox2d (physique) et flatlaf (pour rendre Swing plus joli)
 - trois scripts bash pour compiler et exécuter le jeu, ainsi que générer la javadoc

Instructions de compilation et d'exécution
 - sous Linux, ouvrir un terminal dans le dossier du projet
 - la première fois, il faut donner les droits d'exécution des scripts bash avec la commande : chmod a+x compiler.sh executer.sh 
 - compiler le jeu en exécutant le script "compiler.sh" avec la commande : ./compiler.sh
 - exécuter le jeu en exécutant le script "executer.sh" avec la commande : ./executer.sh
 - (alternativement, il est possible de copier coller dans le terminal le contenu de ces deux scripts pour le même résultat)

Instructions alternatives
Nous avons codé le jeu sur l'IDE Visual Studio Code (VS Code), qui gère automatiquement la compilation et l'execution.
 - Ouvrir le dossier avec VS Code (et éventuellement "trust workspace")
 - Dans l'arboresence des fichiers, ouvrir Main.java
 - VS Code reconnait alors qu'il s'agit d'un projet java et propose d'installer le pack d'extensions Java : l'installer puis relancer VS Code
 - Cliquer sur le bouton Run Java en haut à droite de l'écran

Enfin, si l'archive n'est pas bonne, vous pouvez trouver une version fonctionnelle du code sur le dépôt github :
https://github.com/Lysquid/ProjetAlgo