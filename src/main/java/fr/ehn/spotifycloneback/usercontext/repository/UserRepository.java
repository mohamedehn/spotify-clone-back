package fr.ehn.spotifycloneback.usercontext.repository;

import fr.ehn.spotifycloneback.usercontext.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findOneByEmail(String email);
}
