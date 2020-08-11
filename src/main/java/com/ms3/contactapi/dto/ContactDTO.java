package com.ms3.contactapi.dto;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Valid
public class ContactDTO {

	private Long id;
	
	@NotNull
	private String firstName;
	
	@NotNull
	private String lastName;
	
	@JsonFormat(pattern = "MM/dd/yyyy", timezone = "Asia/Manila")
	@NotNull
	private Date dob;
	
	@NotNull
	private String gender;
	
	@NotNull
	private String title;
	
	private List<AddressDTO> addresses;
	
	private List<CommunicationDTO> communications;
}
