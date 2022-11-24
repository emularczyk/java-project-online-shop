package com.candyshop.islodycze.registration;

import com.candyshop.islodycze.model.Enum.Role;
import com.candyshop.islodycze.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepo.findByEmail(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("UserEntity not found");
        }

        return User.withUsername(userEntity.getEmail())
                    .password(userEntity.getUserPassword())
                    .disabled(false)
                    .authorities(getAuthorities(userEntity))
                    .build();
    }

    private Collection<GrantedAuthority> getAuthorities(UserEntity user){
        Role userGroups = user.getRole();
        Collection<GrantedAuthority> authorities = new ArrayList<>(1);
        authorities.add(new SimpleGrantedAuthority(userGroups.name().toUpperCase()));
        return authorities;
    }
}
