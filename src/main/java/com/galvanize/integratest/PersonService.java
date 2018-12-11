package com.galvanize.integratest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository repository;

    @Autowired
    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    Iterable<Person> findAll() {
        Iterable<Person> people = repository.findAll();
        people.forEach(this::setEmail);
        return people;
    }

    private void setEmail(Person person) {
        person.setEmail("mock@email.com");
    }

}
