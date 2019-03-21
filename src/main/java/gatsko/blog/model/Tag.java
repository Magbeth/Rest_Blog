package gatsko.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "articles")
@Builder
@Table(name = "tags")
public class Tag implements Serializable {
    @Id
    @GeneratedValue
    private Long Id;

    @Column(length = 30, nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private Collection<Article> articles = new ArrayList<>();

    public Tag (String tagName) {
        this.name = tagName;
    }
}
