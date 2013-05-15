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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "simple_name"))
public class Province implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Size(min = 1, max = 45)
	@Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
	@Column(name = "simple_name")
    private String simpleName;
	
	@NotNull
	@Size(min = 1, max = 45)
	@Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
	@Column(name = "full_name")
    private String fullName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Override
	public String toString() {
		String str = "Province [";
		str += "simpleName : '" + simpleName + "', ";
		str += "fullName : '" + fullName + "', ";
		str += "]";
		
		return str;	
	}
	
}