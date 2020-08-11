package com.ms3.contactapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@Table(name = "address")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@NotNull
	@Column(length = 10)
	private String type;
	
	@NotNull
	@Column(length = 10)
	private String number;
	
	@NotNull
	private String street;
	
	@Column(length = 10)
	private String unit;
	
	@NotNull
	@Column(length = 100)
	private String city;
	
	@NotNull
	@Column(length = 2)
	private String state;
	
	@NotNull
	@Column(name = "zip_code", length = 6)
	private String zipCode;
	
	@JsonIgnore
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "contact_id")
	private Contact contact;

}
