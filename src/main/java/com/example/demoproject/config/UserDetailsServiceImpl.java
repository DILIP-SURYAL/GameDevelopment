package com.example.demoproject.config;

import com.example.demoproject.dao.UserRepository;
import com.example.demoproject.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User u = this.userRepository.getUSerByUserName(username);
     if(u == null){

         throw new UsernameNotFoundException("Could not found the User");

     }
     return new CustomUserDetails(u);
    }
}
