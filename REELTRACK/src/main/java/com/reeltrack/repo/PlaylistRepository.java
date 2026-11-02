package com.reeltrack.repo;
import com.reeltrack.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface PlaylistRepository extends JpaRepository<Playlist,Long> {
    List<Playlist> findByOwnerId(Long ownerId);
    List<Playlist> findByIsPublicTrue();
    Optional<Playlist> findByIdAndIsPublicTrue(Long id);
}
