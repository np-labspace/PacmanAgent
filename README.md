# Agent IA Pac-Man

## 📌 Description

Ce projet consiste à développer un agent autonome capable de jouer à Pac-Man dans un environnement incertain et partiellement observable.

L’objectif est de concevoir un système de prise de décision basé sur des informations incomplètes, capable de maximiser le score tout en évitant les menaces.

---

## 🎯 Objectifs

* Modéliser l’environnement de Pac-Man comme un problème de décision
* Gérer l’incertitude et l’observabilité partielle
* Implémenter des stratégies intelligentes pour la prise de décision en temps réel
* Évaluer différentes heuristiques pour améliorer les performances de l’agent

---

## 🧠 Approche

L’agent repose sur des méthodes de recherche heuristique et d’évaluation d’états.

### Composants principaux

* **Belief State (état de croyance)** : représentation des états possibles du jeu en situation d’information incomplète
* **Fonctions heuristiques** : évaluation des états selon :

  * la distance à la nourriture
  * la proximité des fantômes
  * le niveau de risque
* **Stratégie de décision** : sélection des actions maximisant une utilité estimée

---

## ⚙️ Technologies

* Java
* Programmation orientée objet
* Algorithmes de recherche heuristique

---

## 🚀 Fonctionnalités

* Agent Pac-Man autonome
* Prise de décision en environnement partiellement observable
* Sélection d’actions en temps réel
* Architecture modulaire

---

## 📊 Résultats

L’agent est capable de :

* Se déplacer efficacement dans le labyrinthe
* Trouver un compromis entre exploration et sécurité
* Adapter son comportement selon la proximité des fantômes

---

## 💡 Apports du projet

* Compréhension de la prise de décision sous incertitude
* Implémentation d’une IA basée sur des heuristiques
* Modélisation d’un environnement dynamique

---

## 📂 Structure du projet

```
PacmanAgent/
├── src/
│   ├── data/      # Gestion des données du jeu (cartes, score)
│   ├── logic/     # Logique du jeu, IA, états de croyance et entités
│   └── view/      # Affichage graphique et rendu visuel
├── doc/           # Fichiers de cartes (.map) et score
├── bin/           # Fichiers compilés
├── README.md
```

---

## 🚀 Lancer le projet

### Prérequis

* Java (JDK 8 ou supérieur)
* Un environnement de développement (IntelliJ, Eclipse ou VS Code)

---

### ▶️ Exécution

1. Cloner le dépôt :
```
   git clone https://github.com/np-labspace/PacManAgent.git
   cd PacManAgent
```

2. Compiler les fichiers Java :
```
   javac -d bin src/**/*.java
```

3. Lancer le programme :
```
   java -cp bin logic.PacManLauncher
```

4. ⚠️ En cas de problème lors de la compilation, exécuter cette ligne de code à la place puis lancer le programme (étape 3) :

```
javac -d bin src/data/*.java src/logic/*.java src/view/*.java  
```

---

## 🔗 Contexte

Projet académique réalisé en groupe dans le cadre du Master 1 Informatique, Décision, Données pour l’UE *Artificial Intelligence*.
