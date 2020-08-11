package com.ms3.contactapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms3.contactapi.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

}
