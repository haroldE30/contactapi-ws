package com.ms3.contactapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ms3.contactapi.dto.AddressDTO;
import com.ms3.contactapi.dto.CommunicationDTO;
import com.ms3.contactapi.dto.ContactDTO;
import com.ms3.contactapi.entity.Address;
import com.ms3.contactapi.entity.Communication;
import com.ms3.contactapi.entity.Contact;
import com.ms3.contactapi.exception.ResourceNotFoundException;
import com.ms3.contactapi.exception.TransactionProcessException;
import com.ms3.contactapi.repository.ContactRepository;
import com.ms3.contactapi.util.CopyUtil;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

	private ContactRepository repository;

	public ContactServiceImpl(ContactRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<ContactDTO> getAllDtos() {
		List<Contact> list = repository.findAll();
		return list.stream().map(i -> {
			ContactDTO dto = new ContactDTO();
			dto.setId(i.getId());
			dto.setFirstName(i.getFirstName());
			dto.setLastName(i.getLastName());
			dto.setDob(i.getDob());
			dto.setGender(i.getGender());
			dto.setTitle(i.getTitle());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public Contact getById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contact not found for id: " + id));
	}

	@Override
	public ContactDTO getDtoById(Long id) {
		Contact contact = getById(id);
		ContactDTO dto = new ContactDTO();
		CopyUtil.copyProperties(contact, dto);

		return dto;
	}

	@Override
	public Contact create(ContactDTO params) {
		try {
			Contact contact = new Contact();
			contact.setFirstName(params.getFirstName());
			contact.setLastName(params.getLastName());
			contact.setDob(params.getDob());
			contact.setGender(params.getGender());
			contact.setTitle(params.getTitle());

			params.getAddresses().stream().map(a -> {
				Address add = new Address();
				CopyUtil.copyNonNullProperties(a, add);
				return add;
			}).collect(Collectors.toList()).forEach(contact::addAddress);

			params.getCommunications().stream().map(c -> {
				Communication communication = new Communication();
				CopyUtil.copyNonNullProperties(c, communication);
				return communication;
			}).collect(Collectors.toList()).forEach(contact::addCommunication);

			return repository.save(contact);
		} catch (Exception e) {
			throw new TransactionProcessException("Failed to save contact. " + e.getMessage());
		}
	}

	@Override
	public Contact update(Long id, ContactDTO params) {
		try {
			Contact contact = getById(id);
			contact.setFirstName(params.getFirstName());
			contact.setLastName(params.getLastName());
			contact.setDob(params.getDob());
			contact.setGender(params.getGender());
			contact.setTitle(params.getTitle());

			updateAddressList(contact, params.getAddresses());

			updateCommunicationList(contact, params.getCommunications());

			return repository.save(contact);
		} catch (Exception e) {
			throw new TransactionProcessException(
					"Failed to update contact for id: " + id + ". " + e.getMessage());
		}
	}

	/**
	 * This method will find the existing entities that match the ids from given {@link AddressDTO} parameter. 
	 * Only these entities will be updated and the rest will be deleted. This way the list of {@link Address}
	 * belongs to {@link Contact} won't grow and to avoid duplicates. This will only save what comes
	 * from the client.
	 */
	private void updateAddressList(Contact contact, List<AddressDTO> dtos) {
		List<Address> existing = new ArrayList<>();
		
		// search for existing entities and add to the list
		dtos.forEach(dto -> {
			Optional<Address> optional = contact.getAddresses().stream()
					.filter(a -> dto.getId() != null && a.getId().equals(dto.getId())).findFirst();
			if (optional.isPresent()) {
				Address address = optional.get();
				CopyUtil.copyNonNullProperties(dto, address);
				existing.add(address);
			}
		});
		
		// remove entities that won't be updated
		contact.getAddresses().retainAll(existing);

		// update field values
		dtos.forEach(dto -> {
			if (dto.getId() == null) {
				Address address = new Address();
				CopyUtil.copyNonNullProperties(dto, address);
				contact.addAddress(address);
			} 
		});
	}

	/**
	 * This method will find the existing entities that match the ids from given {@link CommunicationDTO} parameter. 
	 * Only these entities will be updated and the rest will be deleted. This way the list of {@link Communication}
	 * belongs to {@link Contact} won't grow and to avoid duplicates. This will only save what comes
	 * from the client.
	 */
	private void updateCommunicationList(Contact contact, List<CommunicationDTO> dtos) {
		List<Communication> existing = new ArrayList<>();
		
		// search for existing entities and add to the list
		dtos.forEach(dto -> {
			Optional<Communication> optional = contact.getCommunications().stream()
					.filter(c -> dto.getId() != null && c.getId().equals(dto.getId())).findFirst();
			if (optional.isPresent()) {
				Communication communication = optional.get();
				CopyUtil.copyNonNullProperties(dto, communication);
				existing.add(communication);
			}
		});
		
		// remove entities that won't be updated
		contact.getCommunications().retainAll(existing);

		// update field values
		dtos.forEach(dto -> {
			if (dto.getId() == null) {
				Communication communication = new Communication();
				CopyUtil.copyNonNullProperties(dto, communication);
				contact.addCommunication(communication);
			} 
		});
	}

	@Override
	public void delete(Long id) {
		try {
			Contact contact = getById(id);

			repository.delete(contact);
		} catch (Exception e) {
			throw new TransactionProcessException(
					"Failed to delete contact for id: " + id + ". " + e.getMessage());
		}
	}

}
