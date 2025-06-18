package com.moroccantube.elguennouni_dev.repository;

import com.moroccantube.elguennouni_dev.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findAllByUserId(Long userId);

    List<Video> findByTitleContaining(String title);

}
