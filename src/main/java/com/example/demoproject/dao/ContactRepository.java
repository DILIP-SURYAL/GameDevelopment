package com.example.demoproject.dao;

import com.example.demoproject.entities.Contact;
import com.example.demoproject.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    //pegination..
    @Query("select c from Contact c where c.user.id =:userId")
    public Page<Contact> findContactByUser(@Param("userId") int id, Pageable pageable);

    public List<Contact> findByNameContainingAndUser(String KeyWords, User user);
}
