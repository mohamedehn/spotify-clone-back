package fr.ehn.spotifycloneback.catalogcontext.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "favorite_song")
@IdClass(FavoriteId.class)
public class Favorite implements Serializable {

    @Id
    private UUID songPublicId;

    @Id
    @Column(name = "user_email")
    private String userEmail;

    public UUID getSongPublicId() {
        return songPublicId;
    }

}
