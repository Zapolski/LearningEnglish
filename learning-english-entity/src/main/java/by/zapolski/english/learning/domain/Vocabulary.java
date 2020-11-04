package by.zapolski.english.learning.domain;

import by.zapolski.english.learning.domain.enums.StudyStatus;
import by.zapolski.english.security.domain.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Сущность для хранения слов изучаемх пользователем
 */
@Entity
@Table
@Data
@EqualsAndHashCode
public class Vocabulary implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Пользователь
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Изучаемое английское слово
     */
    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;

    /**
     * Статус изучаемого слова
     */
    @Column(nullable = false, name = "study_status")
    @Enumerated(EnumType.STRING)
    private StudyStatus status;

    /**
     * Количество попытокк изучения
     */
    @Column(nullable = false)
    private int attempt;

}
