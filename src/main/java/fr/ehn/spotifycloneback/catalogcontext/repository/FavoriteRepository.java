package fr.ehn.spotifycloneback.catalogcontext.repository;

import fr.ehn.spotifycloneback.catalogcontext.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
}
