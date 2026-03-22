package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.LinkedList;
import view.Gomme;

/**
 * class used to represent plan. It will provide for a given set of results an action to perform in each result
 */
class Plans{
    ArrayList<Result> results;
    ArrayList<ArrayList<String>> actions;
   
    /**
     * construct an empty plan
     */
    public Plans() {
        this.results = new ArrayList<Result>();
        this.actions = new ArrayList<ArrayList<String>>();
    }
   
    /**
     * add a new pair of belief-state and corresponding (equivalent) actions
     * @param beliefBeliefState the belief state to add
     * @param action a list of alternative actions to perform. Only one of them is chosen but their results should be similar
     */
    public void addPlan(Result beliefBeliefState, ArrayList<String> action) {
        this.results.add(beliefBeliefState);
        this.actions.add(action);
    }
   
    /**
     * return the number of belief-states/actions pairs
     * @return the number of belief-states/actions pairs
     */
    public int size() {
        return this.results.size();
    }
   
    /**
     * return one of the belief-state of the plan
     * @param index index of the belief-state
     * @return the belief-state corresponding to the index
     */
    public Result getResult(int index) {
        return this.results.get(index);
    }
   
    /**
     * return the list of actions performed for a given belief-state
     * @param index index of the belief-state
     * @return the set of actions to perform for the belief-state corresponding to the index
     */
    public ArrayList<String> getAction(int index){
        return this.actions.get(index);
    }
}

/**
 * class used to represent a transition function i.e., a set of possible belief states the agent may be in after performing an action
 */
class Result{
    private ArrayList<BeliefState> beliefStates;

    /**
     * construct a new result
     * @param states the set of states corresponding to the new belief state
     */
    public Result(ArrayList<BeliefState> states) {
        this.beliefStates = states;
    }

    /**
     * returns the number of belief states
     * @return the number of belief states
     */
    public int size() {
        return this.beliefStates.size();
    }

    /**
     * return one of the belief state
     * @param index the index of the belief state to return
     * @return the belief state to return
     */
    public BeliefState getBeliefState(int index) {
        return this.beliefStates.get(index);
    }
   
    /**
     * return the list of belief-states
     * @return the list of belief-states
     */
    public ArrayList<BeliefState> getBeliefStates(){
        return this.beliefStates;
    }
}


/**
 * class implement the AI to choose the next move of the Pacman
 */
public class AI {

    // profondeur maximale de recherche
    private static final int MAX_DEPTH = 3;
   
    // derniere action effectuee
    private static String lastAction = "";

    /**
     * calcule la prochaine action a effectuer
     * @param beliefState l'etat de croyance courant
     * @return l'action a effectuer
     */
    public static String findNextMove(BeliefState beliefState) {
       
        // obtenir les plans possibles depuis l'etat courant
        Plans plans = beliefState.extendsBeliefState();
        if (plans.size() == 0) return PacManLauncher.UP;
       
        String bestAction = plans.getAction(0).get(0);
        double bestValue = Double.NEGATIVE_INFINITY;
       
        int gommesInitiales = beliefState.getNbrOfGommes();
        int distInitiale = beliefState.distanceMinToGum();
       
        // position de pacman
        Position pacmanPos = beliefState.getPacmanPosition();
        int pacRow = pacmanPos.getRow();
        int pacCol = pacmanPos.getColumn();
       
        // trouver le fantome bleu le plus proche
        int[] fantomeBleuPos = trouverFantomeBleuPlusProche(beliefState, pacRow, pacCol);
        int distInitialeFantomeBleu = fantomeBleuPos[2];
       
        // distance du fantome normal le plus proche
        int distFantomeNormal = Integer.MAX_VALUE;
        for (int g = 0; g < beliefState.getNbrOfGhost(); g++) {
            // ignorer les fantomes bleus
            if (beliefState.getCompteurPeur(g) > 0) continue;
            for (Object obj : beliefState.getGhostPositions(g)) {
                Position ghostPos = (Position) obj;
                int dist = Math.abs(ghostPos.getRow() - pacRow)
                         + Math.abs(ghostPos.getColumn() - pacCol);
                if (dist < distFantomeNormal) {
                    distFantomeNormal = dist;
                }
            }
        }

        // evaluer chaque action possible
        for (int i = 0; i < plans.size(); i++) {
            ArrayList<String> actions = plans.getAction(i);
            Result result = plans.getResult(i);
            String currentAction = actions.get(0);
           
            // evaluation avec l'algorithme AND-OR
            double value = andSearch(result, MAX_DEPTH - 1, gommesInitiales);
           
            // penalite pour les mouvements opposes
            if (isOpposite(currentAction, lastAction)) {
                value -= 2000;
            }
           
            // bonus pour les fantomes bleus
            // seulement si les fantomes normaux sont assez loin
            if (distInitialeFantomeBleu <= 2 && distInitialeFantomeBleu > 0 && distFantomeNormal >= 4) {
               
                int newPacRow = pacRow;
                int newPacCol = pacCol;
                if (currentAction.equals("UP")) newPacRow--;
                else if (currentAction.equals("DOWN")) newPacRow++;
                else if (currentAction.equals("LEFT")) newPacCol--;
                else if (currentAction.equals("RIGHT")) newPacCol++;
               
                int newDistFantomeBleu = Math.abs(newPacRow - fantomeBleuPos[0])
                                       + Math.abs(newPacCol - fantomeBleuPos[1]);
               
                // bonus si on se rapproche du fantome bleu
                if (newDistFantomeBleu < distInitialeFantomeBleu) {
                    if (newDistFantomeBleu == 0) {
                        value += 8000;
                    } else if (newDistFantomeBleu == 1) {
                        value += 5000;
                    } else if (newDistFantomeBleu == 2) {
                        value += 3000;
                    }
                }
                // penalite si on s'eloigne
                else if (newDistFantomeBleu > distInitialeFantomeBleu) {
                    value -= 2000;
                }
            }
            // cas special : fantome bleu juste a cote
            else if (distInitialeFantomeBleu == 1) {
                int newPacRow = pacRow;
                int newPacCol = pacCol;
                if (currentAction.equals("UP")) newPacRow--;
                else if (currentAction.equals("DOWN")) newPacRow++;
                else if (currentAction.equals("LEFT")) newPacCol--;
                else if (currentAction.equals("RIGHT")) newPacCol++;
               
                int newDistFantomeBleu = Math.abs(newPacRow - fantomeBleuPos[0])
                                       + Math.abs(newPacCol - fantomeBleuPos[1]);
               
                if (newDistFantomeBleu == 0) {
                    value += 3000;
                }
            }
           
            // bonus pour manger des gommes immediatement
            double progressBonus = evaluerProgressionImmediate(result, gommesInitiales, distInitiale);
            value += progressBonus;
           
            // petit bruit aleatoire pour departager les egalites
            value += Math.random() * 3.0;

            // mise a jour de la meilleure action
            if (value > bestValue) {
                bestValue = value;
                bestAction = currentAction;
            }
        }
        lastAction = bestAction;
        return bestAction;
    }
   
    /**
     * trouve la position du fantome bleu le plus proche
     * @param state l'etat de croyance
     * @param pacRow la ligne de pacman
     * @param pacCol la colonne de pacman
     * @return tableau [row, col, distance]
     */
    private static int[] trouverFantomeBleuPlusProche(BeliefState state, int pacRow, int pacCol) {
        int[] result = {0, 0, Integer.MAX_VALUE};
       
        for (int i = 0; i < state.getNbrOfGhost(); i++) {
            // verifier si le fantome est bleu
            if (state.getCompteurPeur(i) > 0) {
                for (Object obj : state.getGhostPositions(i)) {
                    Position ghostPos = (Position) obj;
                    int dist = Math.abs(ghostPos.getRow() - pacRow)
                             + Math.abs(ghostPos.getColumn() - pacCol);
                    if (dist < result[2]) {
                        result[0] = ghostPos.getRow();
                        result[1] = ghostPos.getColumn();
                        result[2] = dist;
                    }
                }
            }
        }
        return result;
    }
   
    /**
     * verifie si deux actions sont opposees
     * @param a1 premiere action
     * @param a2 deuxieme action
     * @return true si les actions sont opposees
     */
    private static boolean isOpposite(String a1, String a2) {
        if (a1 == null || a2 == null) return false;
        return (a1.equals("LEFT")  && a2.equals("RIGHT")) ||
               (a1.equals("RIGHT") && a2.equals("LEFT"))  ||
               (a1.equals("UP")    && a2.equals("DOWN"))  ||
               (a1.equals("DOWN")  && a2.equals("UP"));
    }
   
    /**
     * evalue la progression immediate d'une action
     * @param result le resultat de l'action
     * @param gommesInitiales nombre de gommes avant l'action
     * @param distInitiale distance a la gomme avant l'action
     * @return le bonus de progression
     */
    private static double evaluerProgressionImmediate(Result result, int gommesInitiales, int distInitiale) {
        double bonus = 0;
       
        if (result.size() > 0) {
            BeliefState firstState = result.getBeliefState(0);
           
            // bonus si on mange une gomme
            int gommesResultat = firstState.getNbrOfGommes();
            if (gommesResultat < gommesInitiales) {
                int gommesMangees = gommesInitiales - gommesResultat;
                bonus += gommesMangees * 4000;
            }
           
            // bonus si on se rapproche de la gomme la plus proche
            int distResultat = firstState.distanceMinToGum();
            if (distResultat < distInitiale) {
                bonus += (distInitiale - distResultat) * 100;
            }
        }
       
        return bonus;
    }

    /**
     * implementation de AND-SEARCH
     * calcule la moyenne des valeurs des etats resultants
     * @param result les etats de croyance resultants
     * @param depth profondeur restante
     * @param gommesInitiales nombre initial de gommes
     * @return la valeur moyenne
     */
    private static double andSearch(Result result, int depth, int gommesInitiales) {
        if (result.size() == 0) return Double.NEGATIVE_INFINITY;

        double totalScore = 0;
        int countValid = 0;
       
        for (BeliefState bs : result.getBeliefStates()) {
            double val = orSearch(bs, depth, gommesInitiales);
           
            // traitement different selon la valeur
            if (val > -50000) {
                totalScore += val;
                countValid++;
            } else {
                totalScore += val * 0.3;
                countValid++;
            }
        }
       
        return countValid > 0 ? totalScore / countValid : Double.NEGATIVE_INFINITY;
    }

    /**
     * implementation de OR-SEARCH
     * retourne la meilleure valeur parmi les actions possibles
     * @param state l'etat de croyance courant
     * @param depth profondeur restante
     * @param gommesInitiales nombre initial de gommes
     * @return la meilleure valeur trouvee
     */
    private static double orSearch(BeliefState state, int depth, int gommesInitiales) {
        // conditions d'arret
        if (depth == 0 || state.getNbrOfGommes() == 0 || state.getLife() <= 0) {
            return eval(state, gommesInitiales);
        }

        Plans plans = state.extendsBeliefState();
        if (plans.size() == 0) return eval(state, gommesInitiales);

        double maxVal = Double.NEGATIVE_INFINITY;

        // recherche de la meilleure action
        for (int i = 0; i < plans.size(); i++) {
            Result res = plans.getResult(i);
            double val = andSearch(res, depth - 1, gommesInitiales);
            if (val > maxVal) {
                maxVal = val;
            }
        }
       
        return (maxVal == Double.NEGATIVE_INFINITY) ? -100000.0 : maxVal;
    }

    /**
     * fonction d'evaluation d'un etat
     * @param state l'etat a evaluer
     * @param gommesInitiales nombre initial de gommes
     * @return la valeur de l'etat
     */
    private static double eval(BeliefState state, int gommesInitiales) {
       
        // etat de mort
        if (state.getLife() <= 0) return -100000.0;
       
        double score = state.getScore();
       
        int nbGommes = state.getNbrOfGommes();
        int distGomme = state.distanceMinToGum();
       
        // bonus pour les gommes deja mangees
        int gommesMangees = gommesInitiales - nbGommes;
        score += gommesMangees * 500;
       
        // penalite pour les gommes restantes
        score -= nbGommes * 100;
       
        // calcul du poids de la distance
        int poidsDistance = 50 + (1500 / (nbGommes + 1));
       
        // penalite pour la distance a la gomme la plus proche
        if (distGomme < Integer.MAX_VALUE) {
            score -= distGomme * poidsDistance;
        }
       
        // soustraction du danger des fantomes
        score -= evaluerDangerFantomes(state);

        return score;
    }

    /**
     * evalue le danger represente par les fantomes
     * @param state l'etat de croyance
     * @return le niveau de danger
     */
    private static double evaluerDangerFantomes(BeliefState state) {
        double danger = 0;
       
        Position pacman = state.getPacmanPosition();
        int pacRow = pacman.getRow();
        int pacCol = pacman.getColumn();
       
        int distFantomeLePlusProche = Integer.MAX_VALUE;
        // compteur pour savoir combien de fantomes sont a distance 1
        int nombreFantomesProches = 0;
       
        for (int i = 0; i < state.getNbrOfGhost(); i++) {
           
            // fantome bleu : ignore (déjà traité en haut)
            if (state.getCompteurPeur(i) > 0) {    
                continue;
            }
           
            // fantome normal
            for (Object obj : state.getGhostPositions(i)) {
                Position ghostPos = (Position) obj;
                int dist = Math.abs(ghostPos.getRow() - pacRow)
                         + Math.abs(ghostPos.getColumn() - pacCol);
                if (dist < distFantomeLePlusProche) {
                    distFantomeLePlusProche = dist;
                }
               
                // on compte combien sont colles a pacman
                if (dist == 1) {
                    nombreFantomesProches++;
                }
            }
        }
       
        // attribution du danger selon la distance
        if (distFantomeLePlusProche == 0) {
            danger += 100000;
        } else if (distFantomeLePlusProche == 1) {
            // si plusieurs fantomes sont a distance 1
            // on reduit le danger pour pouvoir fuir
            if (nombreFantomesProches >= 2) {
                danger += 200;
            } else {
                danger += 1000;
            }
        } else if (distFantomeLePlusProche == 2) {
            danger += 300;
        } else if (distFantomeLePlusProche == 3) {
            danger += 100;
        } else if (distFantomeLePlusProche == 4) {
            danger += 10;
        }
       
        return danger;
    }
}