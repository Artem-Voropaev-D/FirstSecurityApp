package ru.voropaev.project.SecurityApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.voropaev.project.SecurityApp.services.PersonDetailsService;

@EnableWebSecurity // специальная аннотация которая дает понять спрингу что это конфигурационный класс для спринг секьюрити
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)//для регулировки авторизациим аннотациями
public class SecurityConfig {


    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/ignore1", "/ignore2");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // кофигурируем сам спринг секьюрити, то есть какая страница отвечает за вход, ошибки и тд, а также здесь конфигурируем авторизацию
        return httpSecurity
                .authorizeHttpRequests(auth -> auth
                                //а вот какие роли у пользователя секьюрити получает из персондетаилс гетафоритис, который уже получает их через БД
                                .requestMatchers("/auth/login", "/error", "/auth/registration").permitAll()//всех людей наэти две страницы мы пускаем
//                        .anyRequest().authenticated()//для всех дугих запросов нужно быть аутентифицирован
                                .anyRequest().hasAnyRole("USER", "ADMIN")//для остальных страниц помимо тех что указали выше там "/auth/login", "/error", "/auth/registration" и тд  нужны роли юзер и админ,
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/auth/login")//теперь секьюрити знает что для входа будет использоваться эта страница
                                .loginProcessingUrl("/process_login")//по этому адресу он будет ждать что ему придут логин и пароль (нужно на форме смотреть какой action стоит)
                                .defaultSuccessUrl("/hello", true)//ну сюда он будет перенаправлять после успешной аутентификации, true - значит в любом случае перенаправляй после аут-ции туда
                                .failureUrl("/auth/login?error=true")//если аутентификация не успешно, то перенаправь вот сюда с заданным параметром error, чтобы на форме отследить его и выдать что данные неверны(в форме там с помощью if проверяем)
                ).logout(logout -> logout.logoutUrl("/logout")//адрес по которому происходит логаут, logout - это когда из сессии удаляется пользователь, и у пользователя удаляются куки
                                .logoutSuccessUrl("/auth/login")//куда переходит пользователь при успешном логауте
                        //в хелло.html тупо ссылку оставили и секьюрити сам реализовал логику выхода из сессии
                ).build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        // кофигурируем сам спринг секьюрити, то есть какая страница отвечает за вход, ошибки и тд, а также здесь конфигурируем авторизацию
//        return httpSecurity
//        .authorizeHttpRequests(auth -> auth
//                //а вот какие роли у пользователя секьюрити получает из персондетаилс гетафоритис, который уже получает их через БД
//                        .requestMatchers("/admin").hasRole("ADMIN")//говорим что к этой странице может иметь доступ только пользователь с ролью ROLE_ADMIN, ROLE - можно не писать секьюрити сам его считывает
//                        .requestMatchers("/auth/login", "/error", "/auth/registration").permitAll()//всех людей наэти две страницы мы пускаем
////                        .anyRequest().authenticated()//для всех дугих запросов нужно быть аутентифицирован
//                        .anyRequest().hasAnyRole("USER", "ADMIN")//для остальных страниц помимо тех что указали выше там "/auth/login", "/error", "/auth/registration" и тд  нужны роли юзер и админ,
//                )
//                .formLogin(formLogin ->
//                        formLogin
//                                .loginPage("/auth/login")//теперь секьюрити знает что для входа будет использоваться эта страница
//                                .loginProcessingUrl("/process_login")//по этому адресу он будет ждать что ему придут логин и пароль (нужно на форме смотреть какой action стоит)
//                                .defaultSuccessUrl("/hello", true)//ну сюда он будет перенаправлять после успешной аутентификации, true - значит в любом случае перенаправляй после аут-ции туда
//                                .failureUrl("/auth/login?error=true")//если аутентификация не успешно, то перенаправь вот сюда с заданным параметром error, чтобы на форме отследить его и выдать что данные неверны(в форме там с помощью if проверяем)
//                ).logout(logout -> logout.logoutUrl("/logout")//адрес по которому происходит логаут, logout - это когда из сессии удаляется пользователь, и у пользователя удаляются куки
//                        .logoutSuccessUrl("/auth/login")//куда переходит пользователь при успешном логауте
//                        //в хелло.html тупо ссылку оставили и секьюрити сам реализовал логику выхода из сессии
//                ).build();
//    }


    //настраивает аутентификацию
    //спринг с помощью нашего сервиса получит человека и сам проверит у него логин и пароль
//    protected void configure(AuthenticationManagerBuilder authenticationManager) throws Exception {
//        authenticationManager.userDetailsService(personDetailsService);
//    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(personDetailsService)
//                .passwordEncoder(getPasswordEncoder());
//    }

    //указываем с помощью какого алгоритма мы шифруем пароль, возвращаем этот алгоритм шифрования
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
