package histoire;

import personnages.Gaulois;
import villagegaulois.Etal;
import villagegaulois.Village;

public class ScenarioCasDegrade {
    public static void main(String[] args) {

        try {
            Etal etal = new Etal();
            etal.libererEtal();
            System.out.println("Fin du test de libération d'un étal non occupé.");
        } catch (IllegalStateException e) {
            System.err.println("Erreur lors de la libération de l'étal : " + e.getMessage());
        }
        
        try {
            Etal etal = new Etal();
            Gaulois vendeur = new Gaulois("Bonemine", 7);
            etal.occuperEtal(vendeur, "fleurs", 20);
            etal.acheterProduit(10, null);
        } catch (NullPointerException e) {
            System.err.println("Erreur : L'acheteur ne peut pas être null.");
        }
        
        try {
            Etal etal = new Etal();
            Gaulois vendeur = new Gaulois("Assurancetourix", 2);
            Gaulois acheteur = new Gaulois("Abraracourcix", 10);
            etal.occuperEtal(vendeur, "lyres", 5);
            etal.acheterProduit(-3, acheteur);
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur : La quantité achetée doit être positive.");
        }
        
        try {
            Etal etal = new Etal();
            Gaulois acheteur = new Gaulois("Obélix", 25);
            etal.acheterProduit(5, acheteur);
        } catch (IllegalStateException e) {
            System.err.println("Erreur : Impossible d'acheter dans un étal vide.");
        }
        
        try {
            Village village = new Village("Village sans chef", 10, 5);
            System.out.println(village.afficherVillageois());
        } catch (Village.VillageSansChefException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}