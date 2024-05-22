package ru.voropaev.project.SecurityApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.voropaev.project.SecurityApp.models.Person;
import ru.voropaev.project.SecurityApp.repositories.PeopleRepository;

@Service
public class RegistrationService {

    private final PeopleRepository personRepository;
    private final PasswordEncoder passwordEncoder;//для шифрования

    @Autowired
    public RegistrationService(PeopleRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //метод заригистрируй
    @Transactional //так как происходят изменения в БД
    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));//шифруем пароль который получили с формы
        person.setRole("ROLE_USER");//по умолчанию назначаем роль юзер
        personRepository.save(person);
    }
}
