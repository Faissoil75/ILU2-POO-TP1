package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;


public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;
	
	public Village(String nom, int nbVillageoisMaximum,int nbEtal) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		this.marche= new Marche(nbEtal);
	}
	
	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) { 	
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
        if (chef != null && nomGaulois.equals(chef.getNom())) {
            return chef;
        }
        for (int i = 0; i < nbVillageois; i++) {
            Gaulois gaulois = villageois[i];
            if (gaulois.getNom().equals(nomGaulois)) {
                return gaulois;
            }
        }
        return null;
    }

	public String afficherVillageois() throws VillageSansChefException {
        if (chef == null) {
            throw new VillageSansChefException("Le village " + nom + " n'a pas encore de chef.");
        }
        StringBuilder chaine = new StringBuilder();
        if (nbVillageois < 1) {
            chaine.append("Il n'y a encore aucun habitant au village du chef " + chef.getNom() + ".\n");
        } else {
            chaine.append("Au village du chef " + chef.getNom() + " vivent les légendaires gaulois :\n");
            for (int i = 0; i < nbVillageois; i++) {
                chaine.append("- " + villageois[i].getNom() + "\n");
            }
        }
        return chaine.toString();
    }
	
	public String installerVendeur(Gaulois vendeur, String produit,int nbProduit) {
		StringBuilder chaine = new StringBuilder();
		int numeroEtal;
		chaine.append(vendeur.getNom() + " cherche un endroit pour vendre "+nbProduit +" "+ produit + ".\n");
		numeroEtal = marche.trouverEtalLibre();
		numeroEtal=numeroEtal+1;
		if(numeroEtal==0) {
			chaine.append("Malheureusement "+ vendeur.getNom() + "n'a pas trouvé d'étal libre.\n");
		}else {
			marche.utiliserEtal(numeroEtal-1,vendeur,produit,nbProduit);
			chaine.append("Le vendeur "+ vendeur.getNom() + " vend des " + produit + "à l'étal n°"+ numeroEtal +".\n");
		}
		return chaine.toString();
	}
	
	public String rechercherVendeursProduit(String produit) {
		StringBuilder chaine = new StringBuilder();
		Etal[] etalProd = marche.trouverEtals(produit);
		if(etalProd[0] == null) {
			chaine.append("Il n'y a pas de vendeur qui propose des "+ produit +" au marché.");
		} else if (etalProd[0]!= null && etalProd[1] == null) {
			chaine.append("Seul le vendeur "+ etalProd[0].getVendeur().getNom() + "propose des "+produit+" au marché.");
		} else {
			chaine.append("Les vendeurs qui proposent des "+produit+" sont :\n");
			for(int i = 0;i<etalProd.length;i++) {
				if(etalProd[i] != null) {
					chaine.append("- "+ etalProd[i].getVendeur().getNom()+ "\n");
				}
			}
		}
		return chaine.toString();
	}
	
	public Etal rechercherEtal(Gaulois vendeur) {
	    return marche.trouverVendeur(vendeur);
	}

	public String partirVendeur(Gaulois vendeur) {
	    StringBuilder chaine = new StringBuilder();
	    Etal etal = rechercherEtal(vendeur);
	    if (etal != null) {
	    	int quantiteVendue = etal.getQuantiteDebutMarche() - etal.getQuantite();
	    	int quantiteInitiale = etal.getQuantiteDebutMarche();
	        etal.libererEtal();
	        chaine.append("Le vendeur " + vendeur.getNom() + " quitte son étal, il a vendu " 
	                + quantiteVendue + " " + etal.getProduit() + " parmi les " + quantiteInitiale + " qu'il voulait vendre.\n");
	    } else {
	        chaine.append("Le vendeur " + vendeur.getNom() + " n'a pas d'étal à quitter.\n");
	    }
	    return chaine.toString();
	}

	public String afficherMarche() {
	    StringBuilder chaine = new StringBuilder();
	    chaine.append("Le marché du village \"" + nom + "\" possède plusieurs étals :\n");
	    
	    int nbEtalVide = 0;
	    for (Etal etal : marche.tabEtal) {
	        if (etal.isEtalOccupe()) {
	            chaine.append(etal.getVendeur().getNom() + " vend " + etal.getQuantite() + " " + etal.getProduit() + "\n");
	        } else {
	            nbEtalVide++;
	        }
	    }
	    
	    if (nbEtalVide > 0) {
	        chaine.append("Il reste " + nbEtalVide + " étals non utilisés dans le marché.\n");
	    }
	    
	    return chaine.toString();
	}
	
	private static class Marche{
		private Etal[] tabEtal;
		
		private Marche(int nbEtal) {
			super();
			this.tabEtal = new Etal[nbEtal];
			for(int i=0;i<nbEtal;i++) {
				Etal etal = new Etal();
				tabEtal[i]=etal;
			}
		}
		
		private void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			tabEtal[indiceEtal].occuperEtal(vendeur,produit,nbProduit);
		}
		
		private int trouverEtalLibre() {
			for(int i=0;i<tabEtal.length;i++) {
				if(!tabEtal[i].isEtalOccupe()) {
					return i;
				}
			}
			return -1;
		}
		
		private Etal[] trouverEtals(String produit) {
			Etal[] etalProd = new Etal[tabEtal.length];
			int j=0;
			for(int i=0;i<tabEtal.length;i++) {
				if(tabEtal[i].contientProduit(produit)) {
					etalProd[j]=tabEtal[i];
					j++;
				}
			}
			return etalProd;
		}
		
		private Etal trouverVendeur(Gaulois gaulois) {
			for(int i=0;i<tabEtal.length;i++) {
				if(tabEtal[i].getVendeur()==gaulois) {
					return tabEtal[i];
				}
			}
			return null;
		}
		
	}
	public class VillageSansChefException extends Exception {
		private static final long serialVersionUID = 1L;
		public VillageSansChefException(String message) {
			super(message);
	    }
	}
	
}