package com.example.demo.tags;

import com.example.demo.posts.Post;
import com.example.demo.uploadFile.FileUpload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40 , unique = true)
    private String name;

    @Lob
    @Column(length = 1000000)
    private String description;

    @CreationTimestamp
    @Column(unique=false)
    private Date create_at;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Post> posts;


}
