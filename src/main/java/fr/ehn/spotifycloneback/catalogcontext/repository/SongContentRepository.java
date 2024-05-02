package fr.ehn.spotifycloneback.catalogcontext.repository;

import fr.ehn.spotifycloneback.catalogcontext.domain.SongContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongContentRepository extends JpaRepository<SongContent, Long> {
}
