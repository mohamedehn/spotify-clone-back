package fr.ehn.spotifycloneback.catalogcontext.repository;

import fr.ehn.spotifycloneback.catalogcontext.domain.Favorite;
import fr.ehn.spotifycloneback.catalogcontext.domain.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

    List<Favorite> findAllByUserEmailAndSongPublicIdIn(String email, List<UUID> songPublicIds);
}
