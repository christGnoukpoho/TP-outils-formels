import java.util.HashMap;
import java.util.Map;

    public class AutomateControleAcces {

        // États du système
        enum Etat {
            INIT, VERIFICATION_CARTE, VERIFICATION_CODE, ACCES_ACCORDE, ACCES_REFUSE, ALARME
        }

        // Événements du système
        enum Evenement {
            INSERER_CARTE, CARTE_VALIDE, CARTE_INVALIDE, CODE_VALIDE, CODE_INVALIDE
        }

        private Etat etatCourant;
        private final Map<Etat, Map<Evenement, Etat>> tableTransitions;

        public AutomateControleAcces() {
            etatCourant = Etat.INIT;
            tableTransitions = new HashMap<>();
            initialiserTransitions();
        }

        private void initialiserTransitions() {
            // Transitions depuis l'état INIT
            Map<Evenement, Etat> transitionsInit = new HashMap<>();
            transitionsInit.put(Evenement.INSERER_CARTE, Etat.VERIFICATION_CARTE);
            tableTransitions.put(Etat.INIT, transitionsInit);

            // Transitions depuis l'état VERIFICATION_CARTE
            Map<Evenement, Etat> transitionsVerificationCarte = new HashMap<>();
            transitionsVerificationCarte.put(Evenement.CARTE_VALIDE, Etat.VERIFICATION_CODE);
            transitionsVerificationCarte.put(Evenement.CARTE_INVALIDE, Etat.ACCES_REFUSE);
            tableTransitions.put(Etat.VERIFICATION_CARTE, transitionsVerificationCarte);

            // Transitions depuis l'état VERIFICATION_CODE
            Map<Evenement, Etat> transitionsVerificationCode = new HashMap<>();
            transitionsVerificationCode.put(Evenement.CODE_VALIDE, Etat.ACCES_ACCORDE);
            transitionsVerificationCode.put(Evenement.CODE_INVALIDE, Etat.ALARME);
            tableTransitions.put(Etat.VERIFICATION_CODE, transitionsVerificationCode);
        }

        public void gererEvenement(Evenement evenement) {
            Map<Evenement, Etat> transitionsPossibles = tableTransitions.get(etatCourant);
            if (transitionsPossibles != null && transitionsPossibles.containsKey(evenement)) {
                Etat prochainEtat = transitionsPossibles.get(evenement);
                System.out.println("Transition : " + etatCourant + " -> " + prochainEtat + " via " + evenement);
                etatCourant = prochainEtat;
                validerTransition(prochainEtat, evenement); // Vérification des règles
            } else {
                System.out.println("Aucune transition valide pour l'événement " + evenement + " depuis l'état " + etatCourant);
            }
        }

        private void validerTransition(Etat prochainEtat, Evenement evenement) {
            switch (prochainEtat) {
                case ACCES_ACCORDE:
                    assert etatCourant == Etat.VERIFICATION_CODE : "Erreur : Accès accordé sans vérification complète.";
                    break;
                case ACCES_REFUSE:
                    assert etatCourant == Etat.VERIFICATION_CARTE : "Erreur : Accès refusé sans vérification de la carte.";
                    break;
                case ALARME:
                    assert etatCourant == Etat.VERIFICATION_CODE : "Erreur : Alarme déclenchée sans vérification du code.";
                    break;
            }
        }

        public static void main(String[] args) {
            AutomateControleAcces automate = new AutomateControleAcces();

            // Scénario de test : valide
            automate.gererEvenement(Evenement.INSERER_CARTE);   // INIT -> VERIFICATION_CARTE
            automate.gererEvenement(Evenement.CARTE_VALIDE);   // VERIFICATION_CARTE -> VERIFICATION_CODE
            automate.gererEvenement(Evenement.CODE_VALIDE);   // VERIFICATION_CODE -> ACCES_ACCORDE

            // Scénario de test : invalide (déclenche assertion)
            automate.gererEvenement(Evenement.INSERER_CARTE);   // INIT -> VERIFICATION_CARTE
            automate.gererEvenement(Evenement.CODE_INVALIDE); // ALARME incorrecte
        }
    }


