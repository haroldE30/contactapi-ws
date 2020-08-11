package com.ms3.contactapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ms3.contactapi.dto.ContactDTO;
import com.ms3.contactapi.service.ContactService;

@CrossOrigin(origins = ContactController.CROSS_ORIGIN_URL)
@RestController
public class ContactController {

	static final String CROSS_ORIGIN_URL = "http://localhost:4200";
	
	private ContactService service;
	
	public ContactController(ContactService service) {
		this.service = service;
	}
	
	@GetMapping("/contacts")
	@ResponseStatus(HttpStatus.OK)
	public List<ContactDTO> getAll() {
		return service.getAllDtos();
	}
	
	@GetMapping("/contacts/{id}")
	@ResponseStatus(HttpStatus.OK)
	private ContactDTO getById(@PathVariable Long id) {
		return service.getDtoById(id);
	}
	
	@PostMapping("/contacts")
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@Valid @RequestBody ContactDTO params) {
		service.create(params);
	}
	
	@PutMapping("/contacts/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void update(@PathVariable Long id, @Valid  @RequestBody ContactDTO params) {
		service.update(id, params);
	}
	
	@DeleteMapping("/contacts/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
}
