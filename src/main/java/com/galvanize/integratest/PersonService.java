package com.galvanize.integratest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PersonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository repository;
    private final RestTemplate restTemplate;

    @Autowired
    public PersonService(PersonRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    Iterable<Person> findAll() {
        LOGGER.info("find all Person in repository and add email from external resource.");
        Iterable<Person> people = repository.findAll();
        people.forEach(this::setEmail);
        return people;
    }

    private void setEmail(Person person) {
        LOGGER.info("get email from ext. res.");
        person.setEmail(restTemplate
                .getForEntity("https://haskell-zuul.herokuapp.com/amqp.url", String.class).getBody());
    }

}
