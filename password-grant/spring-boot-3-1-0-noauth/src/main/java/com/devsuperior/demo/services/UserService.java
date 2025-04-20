package com.devsuperior.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devsuperior.demo.entities.Role;
import com.devsuperior.demo.entities.User;
import com.devsuperior.demo.projections.UserDetailsProjection;
import com.devsuperior.demo.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService{

	@Autowired
	private UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
		
		if(result.size() == 0) {
			throw new UsernameNotFoundException("User not found");
		}

		
		//criação da entidade, montagem do usuário com o métodos SET.
		//Passa na lista de result pegando as roles, montando o obj delas com o construtor, e atribuindo na role do usuário criado.
		
		User user = new User();
		user.setEmail(username);
		user.setPassword(result.get(0).getPassword());
		for(UserDetailsProjection projection: result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}
		
		return user;
	
	}
	
	

}
