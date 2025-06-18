package com.moroccantube.app.repository;

import com.moroccantube.app.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findAllByUserId(Long userId);

    List<Video> findByTitleContaining(String title);

}
