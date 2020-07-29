package by.zapolski.english.dictionary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDictionaryRequestByRank {

    @NotNull(message = "Can't be empty")
    @Min(value = 0, message = "Value must be more than 0")
    private Integer searchRank;
}
