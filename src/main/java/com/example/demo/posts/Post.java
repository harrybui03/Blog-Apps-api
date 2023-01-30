package com.example.demo.posts ;

import com.example.demo.comments.Comment;
import com.example.demo.tags.Tag;
import com.example.demo.uploadFile.FileUpload;
import com.example.demo.users.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable=false, unique=false , length = 1000)
    private String body;

    @Lob
    @Column( nullable=false, unique=false ,length = 100000000)
    private String title;

    @Column( nullable=false, unique=false)
    private String titleURL;

    @CreationTimestamp
    @Column( nullable=true,unique=false)
    private Date create_at;

    @OneToMany(mappedBy = "post" ,cascade = CascadeType.ALL , orphanRemoval = true)
    private List<FileUpload> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;


    @OneToMany(mappedBy = "post" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

}
