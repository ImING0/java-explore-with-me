package ru.practicum.ewm.service.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.ewm.service.category.model.Category;
import ru.practicum.ewm.service.compilation.model.Compilation;
import ru.practicum.ewm.service.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
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
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(name = "participant_limit", nullable = false)
    @Builder.Default
    private Integer participant_limit = 0; //Лимит участников (0 - нет лимита)
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
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
    @Column(name = "paid", columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean paid = false; // Нужно ли платить за участие
    @Column(name = "request_moderation", columnDefinition = "boolean default true")
    private Boolean requestModeration = true; // Нужна ли модерация заявок
    /*Компиляция*/
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "compilation_id", nullable = false)
    private Compilation compilation;
}
