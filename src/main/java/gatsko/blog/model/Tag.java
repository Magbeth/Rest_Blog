package gatsko.blog.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "articles")
@Builder
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue
    private Long Id;

    @Column(length = 30, nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Collection<Article> articles = new ArrayList<>();
}
