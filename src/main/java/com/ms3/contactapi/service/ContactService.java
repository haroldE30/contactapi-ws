package com.ms3.contactapi.service;

import java.util.List;

import com.ms3.contactapi.dto.ContactDTO;
import com.ms3.contactapi.entity.Contact;

public interface ContactService {

	List<ContactDTO> getAllDtos();
	Contact getById(Long id);
	ContactDTO getDtoById(Long id);
	Contact create(ContactDTO params);
	Contact update(Long id, ContactDTO params);
	void delete(Long id);
}
