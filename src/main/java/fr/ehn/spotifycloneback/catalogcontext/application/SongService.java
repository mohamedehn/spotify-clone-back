package fr.ehn.spotifycloneback.catalogcontext.application;

import fr.ehn.spotifycloneback.catalogcontext.application.dto.ReadSongInfoDTO;
import fr.ehn.spotifycloneback.catalogcontext.application.dto.SaveSongDTO;
import fr.ehn.spotifycloneback.catalogcontext.application.mapper.SongContentMapper;
import fr.ehn.spotifycloneback.catalogcontext.application.mapper.SongMapper;
import fr.ehn.spotifycloneback.catalogcontext.domain.Song;
import fr.ehn.spotifycloneback.catalogcontext.domain.SongContent;
import fr.ehn.spotifycloneback.catalogcontext.repository.SongContentRepository;
import fr.ehn.spotifycloneback.catalogcontext.repository.SongRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SongService {

    private final SongMapper songMapper;

    private final SongRepository songRepository;

    private final SongContentMapper songContentMapper;

    private final SongContentRepository songContentRepository;

    public SongService(SongMapper songMapper, SongRepository songRepository, SongContentMapper songContentMapper, SongContentRepository songContentRepository) {
        this.songContentMapper = songContentMapper;
        this.songMapper = songMapper;
        this.songRepository = songRepository;
        this.songContentRepository = songContentRepository;
    }

    public ReadSongInfoDTO create(SaveSongDTO saveSongDTO) {
        Song song = songMapper.saveSongDTOtoSong(saveSongDTO);
        Song songSaved = songRepository.save(song);

        SongContent songContent = songContentMapper.saveSongDTOToSong(saveSongDTO);
        songContent.setSong(songSaved);

        songContentRepository.save(songContent);
        return songMapper.songToReadSongInfoDTO(songSaved);
    }

    @Transactional
    public List<ReadSongInfoDTO> getAll() {
        return songRepository.findAll()
                .stream()
                .map(songMapper::songToReadSongInfoDTO)
                .toList();
    }
}
