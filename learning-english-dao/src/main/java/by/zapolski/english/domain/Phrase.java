package by.zapolski.english.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class Phrase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Английское слово связанное с примером-фразой
     */
    private Word word;

    /**
     * Ресурс для файла-трэка с произношением английской фразы
     */
    private Resource resource;

    /**
     * Контекст-определение изучаемого слова в данном примере-фразе
     */
    private Context context;

    /**
     * Текстовое представление английской фразы-примера
     */
    private String value;

    /**
     * Ранг фразы, определенный по наименее часто употребляемому слову во фразе
     *
     * Позволяет фильтровать фразы по "сложности"
     */
    private Integer rank;

}
