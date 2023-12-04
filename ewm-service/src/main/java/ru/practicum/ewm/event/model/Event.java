package ru.practicum.ewm.event.model;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "events")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
/* TypeDef почему и зачем
https://vladmihalcea.com/the-best-way-to-map-an-enum-type-with-jpa-and-hibernate/*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    /* Общая информация о событии*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false, length = 120)
    private String title;
    @Column(name = "annotation", nullable = false, length = 2000)
    private String annotation;
    @Column(name = "description", nullable = false, length = 7000)
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit; //Лимит участников (0 - нет лимита)
    @Column(name = "confirmed_requests", nullable = false)
    private Long confirmedRequests; // Количество одобренных заявок на участие в данном событии
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private EventState state; //Состояние события
    /*Даты*/
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate; //Дата события
    @Column(name = "created_on", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdOn; //Дата создания
    @Column(name = "published_on", nullable = false)
    private LocalDateTime publishedOn; //Дата публикации
    /*Координаты проведения события*/
    @Embedded
    private Location location;
    /*Булевы значения*/
    @Column(name = "paid", nullable = false)
    private Boolean paid; // Нужно ли платить за участие
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration; // Нужна ли модерация заявок
    /*Компиляция*/
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "compilation_id")
    private Compilation compilation;
    /*Комментарии*/
    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
    private Set<Comment> comments;
    /*Просмотры*/
    @Column(name = "views", nullable = false)
    private Long views;
}
