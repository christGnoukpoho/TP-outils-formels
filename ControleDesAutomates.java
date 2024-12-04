public class ControleDesAutomates {
    // États de l'automate
    private enum Etat {
        q0, q1, q2, q3, q4, q5, q6, q7
    }

    private Etat etatActuel = Etat.q0;

    // Fonction de transition
    public void transition(char entree) {
        switch (etatActuel) {
            case q0:
                if (entree == 'c')
                    etatActuel = Etat.q1;
                break;
            case q1:
                if (entree == 'v')
                    etatActuel = Etat.q3;
                else if (entree == 'i')
                    etatActuel = Etat.q6;
                break;
            case q3:
                if (entree == 'p')
                    etatActuel = Etat.q4;
                else if (entree == 't')
                    etatActuel = Etat.q6;
                break;
            case q4:
                if (entree == 'k')
                    etatActuel = Etat.q5;
                else if (entree == 'e')
                    etatActuel = Etat.q6;
                else if (entree == 't')
                    etatActuel = Etat.q6;
                break;
            case q5:
            case q6:
                if (entree == 't')
                    etatActuel = Etat.q0;
                break;
            default:
                if (entree == 'r')
                    etatActuel = Etat.q0;
        }
        verifierInvariants();
    }

    // Méthode pour simuler les entrées
    public void simulerEntree(String sequence) {
        for (char entree : sequence.toCharArray()) {
            transition(entree);
        }
    }

    // Afficher l'état actuel
    public Etat getEtatActuel() {
        return etatActuel;
    }

    // Vérification des invariants
    private void verifierInvariants() {
        assert !(etatActuel == Etat.q3 && etatActuel == Etat.q6) : "Conflit d'état détecté";
    }

    public static void main(String[] args) {
        ControleDesAutomates automate = new ControleDesAutomates();
        String sequenceEntrees = "cvpe";
        automate.simulerEntree(sequenceEntrees);
        System.out.println("Etat actuel: " + automate.getEtatActuel());
}
}

