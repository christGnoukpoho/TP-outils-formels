public class VerificationControleAcces {

    private boolean carteValide;
    private boolean codePINCorrect;
    private int tentatives;
    private final int MAX_TENTATIVES = 3;
    private boolean alarmeActive;

    // Méthode pour vérifier l'accès
    public boolean verifierAcces() {
        // L'accès est accordé si la carte est valide et le code est correct
        return carteValide && codePINCorrect;
    }

    // Méthode pour gérer les tentatives de code PIN
    public boolean tenterCodePIN(String codePINEntree, String codePINAttendu) {
        // Si le code PIN est incorrect, incrémenter les tentatives
        if (!codePINEntree.equals(codePINAttendu)) {
            tentatives++;
            if (tentatives >= MAX_TENTATIVES) {
                alarmeActive = true;  // Déclencher l'alarme après trop de tentatives
            }
            return false;
        }
        return true;  // Code PIN correct
    }

    // Méthode pour réinitialiser les tentatives
    public void resetTentatives() {
        tentatives = 0;
        alarmeActive = false;
    }

    // Vérification de la sécurité du système (propriétés)
    public void verifierProprietes() {
        // Propriété 1 : Accès refusé avec une carte invalide
        assert !verifierAcces() : "Erreur : L'accès est accordé avec une carte invalide.";

        // Propriété 2 : Accès refusé après un nombre maximal de tentatives infructueuses
        if (tentatives >= MAX_TENTATIVES) {
            assert !verifierAcces() : "Erreur : L'accès est accordé après plusieurs tentatives infructueuses.";
        }
    }

    public static void main(String[] args) {
        VerificationControleAcces controle = new VerificationControleAcces();

        // Initialisation des variables
        controle.resetTentatives();
        String carteEntree = "carte_invalide";  // Carte invalide pour le test
        String carteValideAttendue = "carte_valide";  // Carte attendue
        String codePINEntree = "1234";  // Code PIN erroné pour tester la sécurité
        String codePINAttendu = "1001";  // Code PIN attendu

        // Vérification de l'accès avec carte invalide
        controle.carteValide = carteEntree.equals(carteValideAttendue);
        controle.codePINCorrect = controle.tenterCodePIN(codePINEntree, codePINAttendu);

        // Vérification des propriétés
        controle.verifierProprietes();

        // Tentatives supplémentaires pour tester les limites
        System.out.println("Tentatives supplémentaires...");
        controle.tenterCodePIN("0000", codePINAttendu);  // Code incorrect
        controle.tenterCodePIN("9999", codePINAttendu);  // Code incorrect

        // Vérification après plusieurs tentatives infructueuses
        controle.verifierProprietes();

        // Résultat
        if (!controle.alarmeActive) {
            System.out.println("Test passé : L'alarme ne devrait pas être activée si les conditions sont respectées.");
        } else {
            System.out.println("Test échoué : L'alarme a été déclenchée.");
        }
    }
}
