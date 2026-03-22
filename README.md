# Agent IA Pac-Man

## 📌 Description

Ce projet consiste à développer un agent autonome capable de jouer à Pac-Man dans un environnement incertain et partiellement observable.

L’objectif est de concevoir un système de prise de décision capable de choisir des actions à partir d’informations incomplètes, tout en maximisant le score et en évitant les menaces.

---

## 🎯 Objectifs

- Modéliser l’environnement de Pac-Man comme un problème de décision  
- Gérer l’incertitude et l’observabilité partielle  
- Implémenter des stratégies intelligentes pour la prise de décision en temps réel  
- Évaluer différentes heuristiques pour améliorer les performances de l’agent  

---

## 🧠 Approche

L’agent repose sur des méthodes de recherche heuristique et d’évaluation d’états.

### Composants principaux :

- **Belief State (état de croyance)** : représentation des états possibles du jeu en situation d’information incomplète  
- **Fonctions heuristiques** : évaluation des états selon :
  - la distance à la nourriture  
  - la proximité des fantômes  
  - le niveau de risque  
- **Stratégie de décision** : sélection des actions maximisant une utilité estimée  

---

## ⚙️ Technologies

- Java  
- Programmation orientée objet  
- Algorithmes de recherche heuristique  

---

## 🚀 Fonctionnalités

- Agent Pac-Man autonome  
- Prise de décision en environnement partiellement observable  
- Sélection d’actions en temps réel  
- Architecture modulaire et extensible  

---

## 📊 Résultats

L’agent est capable de :

- Se déplacer efficacement dans le labyrinthe  
- Trouver un équilibre entre exploration et sécurité  
- Adapter son comportement en fonction de la proximité des fantômes  

---

## 💡 Apports du projet

- Compréhension de la prise de décision sous incertitude  
- Implémentation d’une IA basée sur des heuristiques  
- Modélisation d’un environnement dynamique  

---

## 📂 Structure du projet
src/  
├── agent/ # Logique de l’agent et prise de décision  
├── model/ # Représentation de l’état du jeu  
├── utils/ # Fonctions utilitaires  
└── main/ # Point d’entrée  


---

## 🔗 Contexte

Projet académique réalisé dans le cadre du Master 1 Informatique, Décision, Données.

---
