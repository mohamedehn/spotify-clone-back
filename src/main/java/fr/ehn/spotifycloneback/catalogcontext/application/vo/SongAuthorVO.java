package fr.ehn.spotifycloneback.catalogcontext.application.vo;

import jakarta.validation.constraints.NotBlank;

public record SongAuthorVO(@NotBlank String Value) {
}
