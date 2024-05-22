package ru.voropaev.project.SecurityApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.voropaev.project.SecurityApp.models.Person;
import ru.voropaev.project.SecurityApp.repositories.PeopleRepository;
import ru.voropaev.project.SecurityApp.security.PersonDetails;

import java.util.Optional;

@Service
//implements UserDetailsService - дадим понять спрингу что мы загружаем пользователя по его имени (это для секьюрити)
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUsername(username);
        //ищем как раз таким пользователя с таким именем и если его нет, то бросаем исключение
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new PersonDetails(person.get());
    }
}
