package com.ms3.contactapi.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddressDTO {
	
	private Long id;
	
	@NotNull
	private String type;
	
	@NotNull
	private String number;
	
	@NotNull
	private String street;
	
	@NotNull
	private String unit;
	
	@NotNull
	private String city;
	
	@NotNull
	private String state;
	
	@NotNull
	private String zipCode;

}
