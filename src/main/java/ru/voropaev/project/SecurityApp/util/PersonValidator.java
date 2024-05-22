package ru.voropaev.project.SecurityApp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.voropaev.project.SecurityApp.models.Person;
import ru.voropaev.project.SecurityApp.services.PersonDetailsService;

@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //кинули исключение из персондетайлссервис -> человек не найден, но это костыльненько поэтому лучше делать через отдельный пиплсёрвис и там реализовать поиск в БД тип лоад и тд и там какой-нибуд опшинал юзать
        // и тип через опшинал смотрите есть такой человек или нет
        Person person = (Person) target;

        try {
            personDetailsService.loadUserByUsername(person.getUsername());

        } catch (UsernameNotFoundException e) {
            return; //все ок, пользователь с таким именем не найден
        }
        //не зашли в трай кетч значит кидаем ошибку что такой человек уже есть
        errors.rejectValue("username", "", "Человек с таким именем уже существует");
    }
}
