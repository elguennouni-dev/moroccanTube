package com.moroccantube.app.repository;

import com.moroccantube.app.model.Like;
import com.moroccantube.app.model.User;
import com.moroccantube.app.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUser(User user);

    Optional<Like> findByUserAndVideo(User user, Video video);

}
