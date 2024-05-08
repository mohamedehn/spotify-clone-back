package fr.ehn.spotifycloneback.catalogcontext.application.mapper;

import fr.ehn.spotifycloneback.catalogcontext.application.dto.ReadSongInfoDTO;
import fr.ehn.spotifycloneback.catalogcontext.application.vo.SongAuthorVO;
import fr.ehn.spotifycloneback.catalogcontext.application.vo.SongTitleVO;
import fr.ehn.spotifycloneback.catalogcontext.application.dto.SaveSongDTO;
import fr.ehn.spotifycloneback.catalogcontext.domain.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    Song saveSongDTOtoSong(SaveSongDTO saveSongDTO);

    @Mapping(target = "favorite", ignore = true)
    ReadSongInfoDTO songToReadSongInfoDTO(Song song);

    default SongTitleVO stringToSongTitleVO(String title) {
        return new SongTitleVO(title);
    }

    default SongAuthorVO stringToSongAuthorVO(String author) {
        return new SongAuthorVO(author);
    }

    default String songTitleVOToString(SongTitleVO title) {
        return title.value();
    }

    default String songAuthorVOToString(SongAuthorVO author) {
        return author.value();
    }

}
