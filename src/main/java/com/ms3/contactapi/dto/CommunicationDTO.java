package com.ms3.contactapi.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommunicationDTO {

	private Long id;
	
	@NotNull
	private String type;
	
	@NotNull
	private String value;
	
	private boolean preferred;
}
