package src.model;

public enum Type {
  GET_CHANNELS, //RETOURNE les channels crées et les users présents
  // Ne prend aucun paramètre
  // Retourne un map avec les id des channels et pour chanque clé, une map contenant le nom du 
  // channel, la catégorie et les users
  // CIBLE = CLIENT
  CHANNEL_CREATE, //INFORME les users de la création d'un channel
  // Prend en paramètre le nom du channel à créer et l'id du user créateur
  // Retourne les infos du channel et du user qui l'a créé 
  // Cible = TOUT LE MONDE
  CHANNEL_START, // INFORME les users du début d'une partie, DEMARRE la partie des participants
  // Prend en paramètre le nom du channel à démarrer 
  // Retourne les infos du channel qui a lancé et l'heure de démarrage des timers 
  // Cible = TOUT LE MONDE
  CHANNEL_DELETE, // INFORME les users de la suppression d'un channel 
  // Prend en paramètre l'id du Channel à supprimer
  // Retourne les infos du channel à supprimer
  // Cible = TOUT LE MONDE
  CHANNEL_QUESTIONS, // Retourne les questions selon les params données, TRIGGER le message CHANNEL_START
  // Prend en paramètre l'id du channel, le nom de la catégorie 
  // Retourne une Map<String,String> avec des couple <Image, réponse>
  // Cible = PARTICIPANTS
  USER_CONNECT, // AJOUTE un user dans un channel
  // Prend l'id du user à connecter et l'id du channel
  // Retourne les infos du user qui s'est connecté 
  // Cible = TOUT LE MONDE
  USER_DISCONNECT, // ENLEVE un user d'un channel, TRIGGER le message CHANNEL_DELETE si plus aucun user
  // Prend l'id du user à déconnecter et l'id du channel
  // Retourne les infos du user qui s'est déconnecté 
  // Cible = TOUT LE MONDE
  USER_ANSWER, // ENVOIE le message du user, TRIGGER le message SCORE_REFRESH
  // Prend en paramètre le message du user, la réponse à la question et l'id du user qui l'a envoyé
  // Retourne le message et les infos de l'envoyeur 
  // Cible = PARTICIPANTS
  SCORE_REFRESH, //RESET des timers, INCREMENTE si réponse trouvée  
  // Prend en paramètre le nom du channel à démarrer 
  // Retourne le nouveau score et l'heure de démarrage des timers 
  // PARTICIPANTS
  EXIT,
}