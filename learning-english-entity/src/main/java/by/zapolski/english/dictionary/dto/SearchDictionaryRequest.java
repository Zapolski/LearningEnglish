package by.zapolski.english.dictionary.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SearchDictionaryRequest {

    @NotBlank(message = "Can't be blank")
    private String word;

    @NotNull(message = "Can't be empty")
    @Min(value = 1, message = "Value must be from 1 to 100")
    @Max(value = 100, message = "Value must be from 1 to 100")
    private Integer threshold;
}
