package application;

import javafx.beans.property.SimpleStringProperty;

public class Demande {
	

	private final SimpleStringProperty nom;
	private final SimpleStringProperty prenom;
	private final SimpleStringProperty date_aller;
	private final SimpleStringProperty motif;
	private final String email;
	private final String date_retour;
	private final String transport;
	private final String ville;
	private final String pays;
	private final String commentaire;
	
	public Demande(String nom, String prenom, String date_aller, String motif, String email, String date_retour,  String ville, String pays,String transport,
			String commentaire) {
		super();
		this.nom = new SimpleStringProperty(nom);
		this.prenom = new SimpleStringProperty(prenom);
		this.date_aller = new SimpleStringProperty(date_aller);
		this.motif = new SimpleStringProperty(motif);
		this.email = email;
		this.date_retour = date_retour;
		this.transport = transport;
		this.ville = ville;
		this.pays = pays;
		this.commentaire = commentaire;
	}
	
		
	public String getEmail() {
		return email;
	}


	public String getDate_retour() {
		return date_retour;
	}


	public String getTransport() {
		return transport;
	}


	public String getVille() {
		return ville;
	}


	public String getPays() {
		return pays;
	}


	public String getCommentaire() {
		return commentaire;
	}


	public String getNom() {
		return nom.get();
	}
	public String getPrenom() {
		return prenom.get();
	}
	public String getDate_aller() {
		return date_aller.get();
	}
	public String getMotif() {
		return motif.get();
	}
	
	
	
	
	

}
