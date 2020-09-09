package by.zapolski.english.lemma.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LemmaDto {
    private Long id;

    @NotBlank(message = "Can not be blank")
    private String value;

    @NotNull(message = "Can not be blank")
    @Min(value = 1, message = "Value must be more than zero")
    private Integer rank;

    @NotNull(message = "Can not be blank")
    private Character partOfSpeech;
}
