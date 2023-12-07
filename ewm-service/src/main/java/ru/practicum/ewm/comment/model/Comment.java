package ru.practicum.ewm.comment.model;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
/* TypeDef почему и зачем
https://vladmihalcea.com/the-best-way-to-map-an-enum-type-with-jpa-and-hibernate/*/
@Data
@EqualsAndHashCode(exclude = {"event"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "comment_text", nullable = false, length = 10000)
    private String text;
    @JoinColumn(name = "event_id", nullable = false)
    @ManyToOne
    private Event event;
    @JoinColumn(name = "commentator_id", nullable = false)
    @ManyToOne
    private User commentator;
    @Column(name = "commentator_role", nullable = false)
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private CommentatorRole commentatorRole;
    @Column(name = "comment_state", nullable = false)
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private CommentState state;
    @Column(name = "pinned", nullable = false)
    private Boolean pinned;
    @CreationTimestamp
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
}
