package fr.ehn.spotifycloneback.usercontext.repository;

import fr.ehn.spotifycloneback.usercontext.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
}
