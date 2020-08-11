package com.ms3.contactapi.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@Table(name = "contact")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@NotNull
	@Column(name = "first_name", length = 100)
	private String firstName;
	
	@NotNull
	@Column(name = "last_name", length = 100)
	private String lastName;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	private Date dob;
	
	@NotNull
	@Column(length = 1)
	private String gender;
	
	@NotNull
	@Column(length = 100)
	private String title;
	
	@JsonIgnore
	@OneToMany(
			mappedBy = "contact", 
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<Address> addresses = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(
			mappedBy = "contact", 
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<Communication> communications = new ArrayList<>();
	
	public void addAddress(Address address) {
		addresses.add(address);
		address.setContact(this);
	}
	
	public void removeAddress(Address address) {
		addresses.remove(address);
		address.setContact(null);
	}
	
	public void addCommunication(Communication communication) {
		communications.add(communication);
		communication.setContact(this);
	}
	
	public void removeCommunication(Communication communication) {
		communications.remove(communication);
		communication.setContact(null);
	}
	
}
