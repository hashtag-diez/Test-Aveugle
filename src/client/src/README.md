Bienvenue dans l'interface client du Test Aveugle

Pour lancer l'application, s'assurer d'installer le [SDK de JavaFX](https://gluonhq.com/products/javafx/). 
Installer la library sur le projet sous VSCode en ajoutant tous les fichiers *.jar* trouvés dans javafx-sdk-17.0.1/lib nouvellement installé en tant que librairies de référence (onglet JAVA PROJECTS, Reference Librairies, +).
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

Lancer l'exécution via le main de la classe **App** situé dans *client/src/ihm*