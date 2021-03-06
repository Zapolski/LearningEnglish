package by.zapolski.english.learning.dto;

import lombok.Data;

import java.util.List;

@Data
public class RuleDto {
    private Long id;
    private List<PhraseDto> phrases;
    private String value;
}
