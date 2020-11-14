package br.com.caiqueborges.sprello.user.repository;

import br.com.caiqueborges.sprello.user.repository.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

}
