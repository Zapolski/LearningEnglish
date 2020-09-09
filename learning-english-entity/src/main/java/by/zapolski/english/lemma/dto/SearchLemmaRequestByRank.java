package by.zapolski.english.lemma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchLemmaRequestByRank {

    @NotNull(message = "Can not be empty")
    @Min(value = 0, message = "Value must be more than 0")
    private Integer searchRank;
}
