package com.ms3.contactapi.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms3.contactapi.dto.AddressDTO;
import com.ms3.contactapi.dto.CommunicationDTO;
import com.ms3.contactapi.dto.ContactDTO;
import com.ms3.contactapi.exception.ResourceNotFoundException;
import com.ms3.contactapi.exception.TransactionProcessException;
import com.ms3.contactapi.service.ContactService;

@RunWith(SpringRunner.class)
@WebMvcTest(ContactController.class)
public class ContactControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockBean
	private ContactService service;
	
	private static final String END_POINT = "/contacts";
	
	private ContactDTO contact = new ContactDTO();
	
	private Date date = new Date();
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	@Before
	public void setup() {
		contact.setId(1L);
		contact.setFirstName("test");
		contact.setLastName("test");
		contact.setDob(date);
		contact.setGender("M");
		contact.setTitle("tester");
		
		AddressDTO address = new AddressDTO();
		address.setId(1L);
		address.setType("home");
		address.setNumber("123");
		address.setUnit("3A");
		address.setStreet("saint street");
		address.setCity("city of angels");
		address.setZipCode("1234");
		address.setState("CA");
		
		CommunicationDTO communication = new CommunicationDTO();
		communication.setId(1L);
		communication.setType("home");
		communication.setValue("123-456-7890");
		communication.setPreferred(true);
		
		contact.setAddresses(Lists.newArrayList(address));
		contact.setCommunications(Lists.newArrayList(communication));
	}
	
	@Test
	public void whenGeContacts_thenReturnJson() throws Exception {
		given(service.getAllDtos()).willReturn(Lists.newArrayList(contact));
		
		mvc.perform(get(END_POINT)
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(jsonPath("$", hasSize(1)));
	}
	
	@Test
	public void whenGetContactById_thenReturnJson() throws Exception {
		given(service.getDtoById(1L)).willReturn(contact);
		
		MvcResult mvcResult = mvc.perform(get(END_POINT + "/1")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(jsonPath("$.firstName", is(contact.getFirstName())))
			      .andExpect(jsonPath("$.lastName", is(contact.getLastName())))
			      .andExpect(jsonPath("$.dob", is(sdf.format(contact.getDob()))))
			      .andExpect(jsonPath("$.gender", is(contact.getGender())))
			      .andExpect(jsonPath("$.title", is(contact.getTitle())))
			      .andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		ContactDTO readValue = mapper.readValue(content, ContactDTO.class);
		
		assertThat(contact.getId(), equalTo(readValue.getId()));
		assertThat(contact.getFirstName(), equalTo(readValue.getFirstName()));
		assertThat(contact.getLastName(), equalTo(readValue.getLastName()));
		assertThat(sdf.format(contact.getDob()), equalTo(sdf.format(readValue.getDob())));
		assertThat(contact.getGender(), equalTo(readValue.getGender()));
		assertThat(contact.getTitle(), equalTo(readValue.getTitle()));
		
		List<AddressDTO> addresses = contact.getAddresses();
		List<AddressDTO> adtos = readValue.getAddresses();
		assertThat(addresses.get(0).getId(), equalTo(adtos.get(0).getId()));
		assertThat(addresses.get(0).getType(), equalTo(adtos.get(0).getType()));
		assertThat(addresses.get(0).getNumber(), equalTo(adtos.get(0).getNumber()));
		assertThat(addresses.get(0).getUnit(), equalTo(adtos.get(0).getUnit()));
		assertThat(addresses.get(0).getStreet(), equalTo(adtos.get(0).getStreet()));
		assertThat(addresses.get(0).getCity(), equalTo(adtos.get(0).getCity()));
		assertThat(addresses.get(0).getZipCode(), equalTo(adtos.get(0).getZipCode()));
		assertThat(addresses.get(0).getState(), equalTo(adtos.get(0).getState()));
		
		List<CommunicationDTO> communications = contact.getCommunications();
		List<CommunicationDTO> cdtos = readValue.getCommunications();
		assertThat(communications.get(0).getId(), equalTo(cdtos.get(0).getId()));
		assertThat(communications.get(0).getType(), equalTo(cdtos.get(0).getType()));
		assertThat(communications.get(0).getValue(), equalTo(cdtos.get(0).getValue()));
		assertThat(communications.get(0).isPreferred(), equalTo(cdtos.get(0).isPreferred()));
	}
	
	@Test
	public void whenPostContact_thenCorrectResponse() throws Exception {
		mvc.perform(post(END_POINT)
				.content(mapper.writeValueAsString(contact))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void whenPutContact_thenCorrectResponse() throws Exception {
		
		contact.setFirstName("first");
		contact.setLastName("last");
		contact.setTitle("developer");
		
		mvc.perform(put(END_POINT + "/1")
				.content(mapper.writeValueAsString(contact))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
	}
	
	@Test
	public void whenDeleteContact_thenCorrectResponse() throws Exception {
		mvc.perform(delete(END_POINT + "/1")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
	}
	
	@Test
	public void whenGetContactById_recordNotFound_thenThrowResourceNotFoundException() throws Exception {
		given(service.getDtoById(2L)).willThrow(new ResourceNotFoundException("Record not found."));
		
		mvc.perform(get(END_POINT + "/2")
			      .contentType(MediaType.APPLICATION_JSON))
				  .andExpect(jsonPath("$.message", is("Record not found.")))
			      .andExpect(status().isNotFound());
	}
	
	@Test
	public void whenPostContact_withNullParameter_thenThrowException() throws Exception {
		mvc.perform(post(END_POINT)
				.content(mapper.writeValueAsString(null))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void whenPutContact_andTriggerException_thenThrowTransactionProcessException() throws Exception {
		given(service.update(anyLong(), any(ContactDTO.class))).willThrow(new TransactionProcessException("Failed to update."));
		
		mvc.perform(put(END_POINT + "/1")
				.content(mapper.writeValueAsString(contact))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.message", is("Failed to update.")))
				.andExpect(status().isInternalServerError());
	}
}
