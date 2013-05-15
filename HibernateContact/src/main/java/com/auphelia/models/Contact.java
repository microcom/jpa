package com.auphelia.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Contact implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Size(min = 1, max = 45)
	@Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String prenom;
	
	@NotNull
	@Size(min = 1, max = 45)
	@Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String nom;
	
	@Size(min = 1, max = 45)
	private String rue;
	
	@Size(min = 1, max = 45)
	private String ville;
	
	@Size(min = 1, max = 45)
	private String province;
	
	@Size(min = 1, max = 6)
	@Column(name = "code_postal")
	private String codePostal;
	
	private String email;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		String str = "Contact [";
		str += "email : '" + email + "', ";
		str += "prenom : '" + prenom + "', ";
		str += "nom : '" + nom + "', ";
		str += "rue : '" + rue + "', ";
		str += "ville : '" + ville + "', ";
		str += "province : '" + province + "', ";
		str += "codePostal : '" + codePostal + "'";
		str += "]";
		
		return str;	
	}
}
