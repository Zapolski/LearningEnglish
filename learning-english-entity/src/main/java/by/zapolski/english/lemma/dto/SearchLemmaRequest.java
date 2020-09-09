package by.zapolski.english.lemma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchLemmaRequest {

    @NotBlank(message = "Can not be blank")
    private String word;

    @NotNull(message = "Can not be empty")
    @Min(value = 1, message = "Value must be from 1 to 100")
    @Max(value = 100, message = "Value must be from 1 to 100")
    private Integer threshold;
}
