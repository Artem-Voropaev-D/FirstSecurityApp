package ru.voropaev.project.SecurityApp.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

//    внутри PreAuthorize можно писать и более сложные выражения, например @PreAuthorize("hasRole('ROLE_ADMIN' or или например and то есть сразу две роли hasRole('ROLE_SOME_OTHER'))")
    @PreAuthorize("hasRole('ROLE_ADMIN')")//с помощью этой аннотации показываем то условие,  чтобы пользователь мог пользоваться этим методом
    //типо этот метод должен выполняться только администратором
    public void doAdminStuff(){
        System.out.println("It's adminPage");
    }
}
