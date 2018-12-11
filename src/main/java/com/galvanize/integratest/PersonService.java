package com.galvanize.integratest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PersonService {

    private final PersonRepository repository;
    private final RestTemplate restTemplate;

    @Autowired
    public PersonService(PersonRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    Iterable<Person> findAll() {
        Iterable<Person> people = repository.findAll();
        people.forEach(this::setEmail);
        return people;
    }

    private void setEmail(Person person) {
        person.setEmail(restTemplate
                .getForEntity("https://haskell-zuul.herokuapp.com/amqp.url", String.class).getBody());
    }

}
