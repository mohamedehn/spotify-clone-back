package fr.ehn.spotifycloneback.catalogcontext.application;

import fr.ehn.spotifycloneback.catalogcontext.application.dto.FavoriteSongDTO;
import fr.ehn.spotifycloneback.catalogcontext.application.dto.ReadSongInfoDTO;
import fr.ehn.spotifycloneback.catalogcontext.application.dto.SaveSongDTO;
import fr.ehn.spotifycloneback.catalogcontext.application.dto.SongContentDTO;
import fr.ehn.spotifycloneback.catalogcontext.application.mapper.SongContentMapper;
import fr.ehn.spotifycloneback.catalogcontext.application.mapper.SongMapper;
import fr.ehn.spotifycloneback.catalogcontext.domain.Favorite;
import fr.ehn.spotifycloneback.catalogcontext.domain.FavoriteId;
import fr.ehn.spotifycloneback.catalogcontext.domain.Song;
import fr.ehn.spotifycloneback.catalogcontext.domain.SongContent;
import fr.ehn.spotifycloneback.catalogcontext.repository.FavoriteRepository;
import fr.ehn.spotifycloneback.catalogcontext.repository.SongContentRepository;
import fr.ehn.spotifycloneback.catalogcontext.repository.SongRepository;
import fr.ehn.spotifycloneback.infrastructure.service.dto.State;
import fr.ehn.spotifycloneback.infrastructure.service.dto.StateBuilder;
import fr.ehn.spotifycloneback.usercontext.ReadUserDTO;
import fr.ehn.spotifycloneback.usercontext.application.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SongService {

    private final SongMapper songMapper;

    private final SongRepository songRepository;

    private final SongContentMapper songContentMapper;

    private final SongContentRepository songContentRepository;

    private final UserService userService;

    private final FavoriteRepository favoriteRepository;

    public SongService(UserService userService, SongMapper songMapper, SongRepository songRepository, SongContentMapper songContentMapper, SongContentRepository songContentRepository, FavoriteRepository favoriteRepository) {
        this.songContentMapper = songContentMapper;
        this.songMapper = songMapper;
        this.songRepository = songRepository;
        this.songContentRepository = songContentRepository;
        this.userService = userService;
        this.favoriteRepository = favoriteRepository;
    }

    public ReadSongInfoDTO create(SaveSongDTO saveSongDTO) {
        Song song = songMapper.saveSongDTOtoSong(saveSongDTO);
        Song songSaved = songRepository.save(song);

        SongContent songContent = songContentMapper.saveSongDTOToSong(saveSongDTO);
        songContent.setSong(songSaved);

        songContentRepository.save(songContent);
        return songMapper.songToReadSongInfoDTO(songSaved);
    }

    @Transactional(readOnly = true)
    public List<ReadSongInfoDTO> getAll() {

        List<ReadSongInfoDTO> allSongs = songRepository.findAll()
                .stream()
                .map(songMapper::songToReadSongInfoDTO)
                .toList();

        if(userService.isAuthenticated()) {
            return fetchFavoritesStatusForSongs(allSongs);
        }

        return allSongs;
    }

    public Optional<SongContentDTO> getOneByPublicId(UUID publicId) {
        Optional<SongContent> songPublicById = songContentRepository.findOneBySongPublicId(publicId);
        return songPublicById.map(songContentMapper::songContentToSongContentDTO);
    }

    public List<ReadSongInfoDTO> search(String searchTerm) {
        List<ReadSongInfoDTO> searchedSongs = songRepository.findByTitleOrAuthorContaining(searchTerm)
                .stream()
                .map(songMapper::songToReadSongInfoDTO)
                .collect(Collectors.toList());

        if(userService.isAuthenticated()) {
            return fetchFavoritesStatusForSongs(searchedSongs);
        } else {
            return searchedSongs;
        }
    }

    public State<FavoriteSongDTO, String> addOrRemoveFromFavorite(FavoriteSongDTO favoriteSongDTO, String email) {
        StateBuilder<FavoriteSongDTO, String> builder = State.builder();
        Optional<Song> songToLikeOpt = songRepository.findOneByPublicId(favoriteSongDTO.publicId());
        if (songToLikeOpt.isEmpty()) {
            return builder.forError("Song public id doesn't exist").build();
        }

        Song songToLike = songToLikeOpt.get();

        ReadUserDTO userWhoLikedSong = userService.getByEmail(email).orElseThrow();

        if (favoriteSongDTO.favorite()) {
            Favorite favorite = new Favorite();
            favorite.setSongPublicId(songToLike.getPublicId());
            favorite.setUserEmail(userWhoLikedSong.email());
            favoriteRepository.save(favorite);
        } else {
            FavoriteId favoriteId = new FavoriteId(songToLike.getPublicId(), userWhoLikedSong.email());
            favoriteRepository.deleteById(favoriteId);
            favoriteSongDTO = new FavoriteSongDTO(false, songToLike.getPublicId());
        }

        return builder.forSuccess(favoriteSongDTO).build();
    }

    public List<ReadSongInfoDTO> fetchFavoriteSongs(String email) {
        return songRepository.findAllFavoriteByUserEmail(email)
                .stream()
                .map(songMapper::songToReadSongInfoDTO)
                .toList();
    }

    private List<ReadSongInfoDTO> fetchFavoritesStatusForSongs(List<ReadSongInfoDTO> songs) {
        ReadUserDTO authenticatedUser = userService.getAuthenticatedUserFromSecurityContext();

        List<UUID> songPublicIds = songs.stream().map(ReadSongInfoDTO::getPublicId).toList();

        List<UUID> userFavoriteSongs = favoriteRepository.findAllByUserEmailAndSongPublicIdIn(authenticatedUser.email(), songPublicIds)
                .stream().map(Favorite::getSongPublicId).toList();

        return songs.stream().peek(song -> {
            if (userFavoriteSongs.contains(song.getPublicId())) {
                song.setFavorite(true);
            }
        }).toList();
    }
}
