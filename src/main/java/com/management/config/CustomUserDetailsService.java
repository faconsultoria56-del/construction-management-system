package com.management.config;

import com.management.person.model.UserAccount;
import com.management.person.repository.UserAccountRepository;
import com.management.role.model.PersonRole;
import com.management.role.repository.PersonRoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;
    private final PersonRoleRepository personRoleRepository;

    public CustomUserDetailsService(UserAccountRepository userAccountRepository, PersonRoleRepository personRoleRepository) {
        this.userAccountRepository = userAccountRepository;
        this.personRoleRepository = personRoleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        List<PersonRole> personRoles = personRoleRepository.findByPersonId(userAccount.getPerson().getId());
        List<GrantedAuthority> authorities = personRoles.stream()
                .map(personRole -> new SimpleGrantedAuthority("ROLE_" + personRole.getRole().getName().toUpperCase()))
                .collect(Collectors.toList());

        return new UserPrincipal(userAccount, authorities);
    }
}
