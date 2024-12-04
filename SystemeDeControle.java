public class SystemeDeControle {

            // États du Système
            private enum Etat {
                S0, S1, S2, S3, S4, S5
            }

            private Etat etatActuel = Etat.S0;
            private boolean cartePresente;
            private boolean carteValide;
            private boolean codeCorrect;
            private int nombreTentatives;
            private boolean alarmeActive;

            // Simulation des transitions d'état
            public void insererCarte(boolean carteValide) {
                this.cartePresente = true;
                this.carteValide = carteValide;
                transition();
            }

            public void entrerCode(boolean codeCorrect) {
                this.codeCorrect = codeCorrect;
                transition();
            }

            private void transition() {
                switch (etatActuel) {
                    case S0:
                        if (cartePresente) {
                            etatActuel = Etat.S1;
                        }
                        break;
                    case S1:
                        if (cartePresente && carteValide) {
                            etatActuel = Etat.S2;
                        }
                        break;
                    case S2:
                        if (carteValide && codeCorrect && nombreTentatives <= 3) {
                            etatActuel = Etat.S3;
                        } else if (carteValide && !codeCorrect && nombreTentatives <= 3) {
                            etatActuel = Etat.S4;
                            nombreTentatives++;
                        } else if (nombreTentatives > 3 || (cartePresente && !carteValide)) {
                            etatActuel = Etat.S5;
                        }
                        break;
                    case S5:
                        if (!cartePresente || !carteValide) {
                            alarmeActive = true;
                        }
                        break;
                    default:
                        etatActuel = Etat.S0;
                }
                verifierInvariants();
            }

            // Vérification des invariants
            private void verifierInvariants() {
                assert !(etatActuel == Etat.S3 && !carteValide) : "Erreur : S3 nécessite une carte valide.";
                assert !(etatActuel == Etat.S4 && nombreTentatives > 3) : "Erreur : Trop de tentatives dans S4.";
            }

            public static void main(String[] args) {
                SystemeDeControle systeme = new SystemeDeControle();

                // Exemple de scénarios
                System.out.println("Scénario 1 : Carte valide, code correct");
                systeme.insererCarte(true);   // Carte présente et valide
                systeme.entrerCode(true);    // Code correct
                System.out.println("État actuel : " + systeme.etatActuel);

                System.out.println("Scénario 2 : Carte invalide");
                systeme.insererCarte(false);  // Carte invalide
                systeme.entrerCode(false);   // Code incorrect
                System.out.println("État actuel : " + systeme.etatActuel);

                System.out.println("Scénario 3 : Carte valide, code incorrect (3 tentatives)");
                systeme.insererCarte(true);   // Carte présente et valide
                for (int i = 0; i < 3; i++) {
                    systeme.entrerCode(false); // Code incorrect
                    System.out.println("Tentative " + (i + 1) + " : État actuel : " + systeme.etatActuel);
                }

                System.out.println("Simulation terminée avec succès.");

            }
        }
