Le but c'est de partir de l'api rest et d'écrire l'api graphql

Step 1 : Montrer comment récupérer un livre par id (en passant de rest à graphql)
Step 2 : Montrer comment rechercher des livres. En rest, on passe les paramètres de recherche en query params et montrer comment faire en graphql
Step 3 : Montrer une route authentifiée
Step 4 : Créer un auteur / livre





Ràf :

- Faire des objets book plus complexes -> PG
- Ecrire entièrement l'api REST -> Charles
  - Pour enrichir l'url, on peut ajouter un queryParam qui permet de dire : tous les livres dont "l'auteur est" ou "le genre est" ou "la date est inférieure à"
- Préparer les étapes du dessus
  - Step 1 -> PG
  - Step 2 -> Charles
  - Step 3 -> Charles
  - Step 4 -> PG


Book {
  isbn
  titre
  synopsis
  date
  auteur
  genre
}

Genre -> sealed family (sealed trait / case object)

Auteur {
  id
  nom
  prénom
}


# Explications des difficultés rencontrées

- UUID n'est pas dans Sangria,   
