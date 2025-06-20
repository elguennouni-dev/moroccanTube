package com.moroccantube.app.repository;

import com.moroccantube.app.model.Video;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {

    List<Video> findAllByUserId(UUID userId);
    List<Video> findByTitleContaining(String title);

    @Query("SELECT v FROM Video v JOIN FETCH v.user")
    List<Video> findAllWithUser();

//    @Query("SELECT v FROM Video v JOIN FETCH v.user WHERE v.id = ?1")
    @EntityGraph(attributePaths = "user")
    Optional<Video> findById(UUID id);

//    @EntityGraph(attributePaths = "user")
//    List<Video> findAllByTitle(String title);


    // After update...
    @EntityGraph(attributePaths = {"user", "comments.user", "likes"})
    Optional<Video> findDetailedVideoById(UUID id);

    @EntityGraph(attributePaths = {"user", "comments.user", "likes"})
    List<Video> findAllByTitle(String title);

}
