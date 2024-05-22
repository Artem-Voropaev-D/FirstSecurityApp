package ru.voropaev.project.SecurityApp.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.voropaev.project.SecurityApp.models.Person;

import java.util.Collection;
import java.util.Collections;

public class PersonDetails implements UserDetails {
    private final Person person;

    public PersonDetails(Person person) {
        this.person = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //если бы мы реализовывали через действия афоритис то создавали бы лист из этих объектов new SimpleGrantedAuthority и возвращали бы его
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole())); //будем получать роли которые есть у пользователя и будем возваращть коллекцию тех прав, что имеет пользователь
    }

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {//просрочен ли аккаунт то есть действителен он или нет
        return true; // true - все норм не просрсочен
    }

    @Override
    public boolean isAccountNonLocked() { // заблокирован или нет
        return true; // true - не забанен
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // true - пароль не просрочен
    }

    @Override
    public boolean isEnabled() {
        return true; // true - аккаунт включен и он работает
    }

    //нужно чтобы получать данные аутентифицированного пользователя
    public Person getPerson() {
        return this.person;
    }
}
