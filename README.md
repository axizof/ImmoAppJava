# ImmoAppJava



# Installation

Dans le fichier ``Dep`` ce trouve les dépendance à installer pour le projet dans project structure ensuite il faut set les information de connexion à la base de donnée pour ceci il faut ce rendre dans le fichier ressource un fichier config.ini sera présent il vous faudra mettre les informations de connexion dedans
``src/main/resources/com/example/immoapp`` le fichier config.ini devrais être ici
jdbcUrl=jdbc:mysql://localhost:3306/immoapp
username=root
password=
par exemple
ensuite il faut importer les deux base de donnée qui sont dans le fichier Dep immoapp.sql et immoapperr.sql

pour le mode keyclaok ou mysql l'utilisateur est
user:test
pass:test

# présentation
![mainpage](https://i.postimg.cc/VN66w7NJ/Cfzefzefzefzefapture.png)
voici à quoi ressemble l'application on peut rechercher par code postal on n'a un button qui permet de actualiser les différente tableview si besoin après certaine modification on peut supprimer un logement ajouter une pièce , on peut ajouter plusieurs pièce par logement et pour chaque formulaire on peut ajouter plusieurs images d'un coup exemple si on n'a plusieurs image du logement on peut directement ajouter toute les images du logement  on peut éditer un logement également il faut sélectionner un logement dans le table view pour pouvoir l'éditer à droite on na les informations du logement avec les pièce et les Equipement ainsi que leur photo on na également un Button qui ouvre une page pour afficher l'emplacement du bien si une pièce possède plusieurs image il suffit d'utiliser les Button gauche droite de même pour les Equipements
