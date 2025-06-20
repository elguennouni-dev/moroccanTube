package com.moroccantube.app.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "videos")
@Data
public class Video {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String contentType;

    private Long size;

    private LocalDateTime uploadDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Like> likes;

    @Column(nullable = false)
    private Long duration;

}