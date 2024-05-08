package fr.ehn.spotifycloneback.catalogcontext.application.mapper;

import fr.ehn.spotifycloneback.catalogcontext.application.dto.SaveSongDTO;
import fr.ehn.spotifycloneback.catalogcontext.application.dto.SongContentDTO;
import fr.ehn.spotifycloneback.catalogcontext.domain.SongContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongContentMapper {


    @Mapping(source = "song.publicId", target = "publicId")
    SongContentDTO songContentToSongContentDTO(SongContent songContent);

    SongContent saveSongDTOToSong(SaveSongDTO songDTO);
}
