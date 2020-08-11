package com.ms3.contactapi.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

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

@RunWith(SpringRunner.class)
public class ContactServiceTest {

	@InjectMocks
	private ContactServiceImpl service;
	
	@Mock
	private ContactRepository repository;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private ContactDTO contactDto;
	private Contact contact;
	
	@Before
	public void setup() {
		contactDto = new ContactDTO();
		contactDto.setId(1L);
		contactDto.setFirstName("test");
		contactDto.setLastName("test");
		contactDto.setDob(new Date());
		contactDto.setGender("M");
		contactDto.setTitle("tester");
		
		AddressDTO addressDto = new AddressDTO();
		addressDto.setId(1L);
		addressDto.setType("home");
		addressDto.setNumber("123");
		addressDto.setUnit("3A");
		addressDto.setStreet("saint street");
		addressDto.setCity("city of angels");
		addressDto.setZipCode("1234");
		addressDto.setState("CA");
		
		CommunicationDTO communicationDto = new CommunicationDTO();
		communicationDto.setId(1L);
		communicationDto.setType("home");
		communicationDto.setValue("123-456-7890");
		communicationDto.setPreferred(true);
		
		contactDto.setAddresses(Lists.newArrayList(addressDto));
		contactDto.setCommunications(Lists.newArrayList(communicationDto));
		
		contact = new Contact();
		contact.setId(contactDto.getId());
		contact.setFirstName(contactDto.getFirstName());
		contact.setLastName(contactDto.getLastName());
		contact.setDob(contactDto.getDob());
		contact.setGender(contactDto.getGender());
		contact.setTitle(contactDto.getTitle());
		
		Address address = new Address();
		CopyUtil.copyProperties(addressDto, address);
		contact.addAddress(address);
		
		Communication communication = new Communication();
		CopyUtil.copyProperties(communicationDto, communication);
		contact.addCommunication(communication);
	}
	
	@Test
	public void whenGettingContacts_thenReturnContactList() throws Exception {
		List<Contact> list = Lists.newArrayList(contact);
		when(repository.findAll()).thenReturn(list);
		List<ContactDTO> dtos = service.getAllDtos();
		assertThat(dtos.size()==1, equalTo(true));
	}
	
	@Test
	public void whenGettingContactById_thenReturnContact() throws Exception {
		Optional<Contact> optional = Optional.of(contact);
		when(repository.findById(anyLong())).thenReturn(optional);
		Contact retrieved = service.getById(1L);
		assertThat(contactDto.getId(), equalTo(retrieved.getId()));
		assertThat(contactDto.getFirstName(), equalTo(retrieved.getFirstName()));
		assertThat(contactDto.getLastName(), equalTo(retrieved.getLastName()));
		assertThat(sdf.format(contactDto.getDob()), equalTo(sdf.format(retrieved.getDob())));
		assertThat(contactDto.getGender(), equalTo(retrieved.getGender()));
		assertThat(contactDto.getTitle(), equalTo(retrieved.getTitle()));
	}
	
	@Test
	public void whenValidContact_shouldBeSaved() throws Exception {
		
		when(repository.save(any(Contact.class))).thenReturn(contact);
		
		Contact savedContact = service.create(contactDto);
		assertThat(contactDto.getId(), equalTo(savedContact.getId()));
		assertThat(contactDto.getFirstName(), equalTo(savedContact.getFirstName()));
		assertThat(contactDto.getLastName(), equalTo(savedContact.getLastName()));
		assertThat(sdf.format(contactDto.getDob()), equalTo(sdf.format(savedContact.getDob())));
		assertThat(contactDto.getGender(), equalTo(savedContact.getGender()));
		assertThat(contactDto.getTitle(), equalTo(savedContact.getTitle()));
		
		List<Address> addresses = savedContact.getAddresses();
		List<AddressDTO> adtos = contactDto.getAddresses();
		assertThat(adtos.get(0).getId(), equalTo(addresses.get(0).getId()));
		assertThat(adtos.get(0).getType(), equalTo(addresses.get(0).getType()));
		assertThat(adtos.get(0).getNumber(), equalTo(addresses.get(0).getNumber()));
		assertThat(adtos.get(0).getUnit(), equalTo(addresses.get(0).getUnit()));
		assertThat(adtos.get(0).getStreet(), equalTo(addresses.get(0).getStreet()));
		assertThat(adtos.get(0).getCity(), equalTo(addresses.get(0).getCity()));
		assertThat(adtos.get(0).getZipCode(), equalTo(addresses.get(0).getZipCode()));
		assertThat(adtos.get(0).getState(), equalTo(addresses.get(0).getState()));
		
		List<Communication> communications = savedContact.getCommunications();
		List<CommunicationDTO> cdtos = contactDto.getCommunications();
		assertThat(cdtos.get(0).getId(), equalTo(communications.get(0).getId()));
		assertThat(cdtos.get(0).getType(), equalTo(communications.get(0).getType()));
		assertThat(cdtos.get(0).getValue(), equalTo(communications.get(0).getValue()));
		assertThat(cdtos.get(0).isPreferred(), equalTo(communications.get(0).isPreferred()));
	}
	
	public void whenValidContact_shouldBeUpdated() throws Exception{
		Optional<Contact> optional = Optional.of(contact);
		when(repository.findById(anyLong())).thenReturn(optional);
		when(repository.save(any(Contact.class))).thenReturn(contact);
		
		Contact updated = service.update(1L, contactDto);
		assertThat(updated, is(notNullValue()));
	}
	
	@Test(expected = TransactionProcessException.class)
	public void whenSaveContact_thenThrowsException() throws Exception {
		when(repository.save(any(Contact.class))).thenThrow(TransactionProcessException.class);
		service.create(contactDto);
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void whenGetContact_thenThrowsException() throws Exception {
		when(repository.findById(anyLong())).thenThrow(ResourceNotFoundException.class);
		service.getById(1L);
	}
}
