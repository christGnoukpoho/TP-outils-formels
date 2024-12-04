import java.util.Scanner;
public class ControleDuCircuit {
    // Variables d'entrée
    private boolean cartePresente;
    private boolean carteValide;
    private boolean codePINEntre;
    private boolean codePINCorrect;
    private int compteurTentatives; // Représenté en bits N1 et N2
    private boolean signalTemporisation;
    private boolean alarmeActive;
    private final int MAX_TENTATIVES = 3;

    // Méthode pour vérifier la validité de la carte
    public boolean verifierCarte(String carteEntree, String carteValideAttendue) {
        return carteEntree.equals(carteValideAttendue);
    }

    // Méthode pour vérifier le code PIN
    public boolean verifierCodePIN(String codePINEntree, String codePINAttendu) {
        return codePINEntree.equals(codePINAttendu);
    }

    // Expressions Logiques Initiales
    public boolean calculerAcces() {
        return carteValide && codePINCorrect && compteurTentatives < MAX_TENTATIVES;
    }

    public boolean declencherAlarme() {
        return (cartePresente && !carteValide) || compteurTentatives >= MAX_TENTATIVES;
    }

    // Mise à jour de l'état de l'alarme
    public void miseAJourAlarme() {
        alarmeActive = declencherAlarme();
    }

    // Méthodes pour les opérations de base
    public void incrementerCompteur() {
        if (codePINEntre && !codePINCorrect) {
            compteurTentatives++;
        }
    }

    public void resetCompteur() {
        if (signalTemporisation || (carteValide && codePINCorrect)) {
            compteurTentatives = 0;
        }
        miseAJourAlarme();
    }

    // Exemple d'utilisation et de tests
    public static void main(String[] args) {
        ControleDuCircuit circuit = new ControleDuCircuit();
        Scanner scanner = new Scanner(System.in);

        // Initialisation des variables
        circuit.signalTemporisation = false;

        // Carte valide attendue
        String carteValideAttendue = "koffi toh yann";

        // Demande de saisie de la carte
        System.out.println("Insérez votre carte:");
        String carteEntree = scanner.nextLine();

        circuit.cartePresente = true;
        circuit.carteValide = circuit.verifierCarte(carteEntree, carteValideAttendue);

        if (circuit.carteValide) {
            System.out.println("Carte valide. Veuillez entrer votre code PIN.");

            // Code PIN attendu
            String codePINAttendu = "1001";

            // Simulation des tentatives de code PIN
            for (int i = 0; i < 3; i++) {
                System.out.println("Entrez le code PIN:");
                String codePINEntree = scanner.nextLine();

                circuit.codePINEntre = true;
                circuit.codePINCorrect = circuit.verifierCodePIN(codePINEntree, codePINAttendu);
                circuit.incrementerCompteur();
                circuit.miseAJourAlarme();

                if (circuit.calculerAcces()) {
                    System.out.println("Accès autorisé");
                    break;
                } else if (circuit.declencherAlarme() && i == 2) {
                    System.out.println("Code bloqué. Alarme déclenchée.");
                    break;
                } else {
                    System.out.println("Accès refusé");
                }
            }
        } else {
            System.out.println("Carte invalide. Alarme déclenchée.");
            circuit.miseAJourAlarme();
            System.out.println("Alarme: " + circuit.alarmeActive); // Affiche l'état de l'alarme
        }

        scanner.close();
}
}

