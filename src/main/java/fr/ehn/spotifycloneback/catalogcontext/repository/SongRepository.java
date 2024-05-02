package fr.ehn.spotifycloneback.catalogcontext.repository;

import fr.ehn.spotifycloneback.catalogcontext.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
}
