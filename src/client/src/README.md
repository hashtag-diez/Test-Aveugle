Bienvenue dans l'interface client du Test Aveugle

Pour lancer l'application, s'assurer d'installer le [SDK de JavaFX](https://gluonhq.com/products/javafx/). 
Installer la library sur le projet sous VSCode en ajoutant tous les fichiers *.jar* trouvés dans javafx-sdk-17.0.1/lib nouvellement installé en tant que librairies de référence (onglet JAVA PROJECTS, Reference Librairies, +).
Créer le fichier de configuration launch.json en cliquant sur *Ajouter une configuration* dans l'onglet Exécuter, puis sur Java.
Modifier le fichier **launch.json** situé dans *.vscode* en remplaçant <path> par le chemin absolu vers le fichier du SDK dans votre système :
```
{
    "type": "java",
    "name": "Launch App",
    "request": "launch",
    "vmArgs": "--module-path \"<path>/javafx-sdk-17.0.1/lib\" --add-modules javafx.controls,javafx.fxml",
    "mainClass": "ihm.App",
    "projectName": "Test-Aveugle_628d6755"
},
```
    
/!\ l'application côté serveur doit avoir été lancée en parallèle afin de permettre un lancement correct.

Lancer l'exécution via le main de la classe **App** situé dans *client/src/ihm*
