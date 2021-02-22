package by.zapolski.english.lemma.dto;

import lombok.Data;

import java.util.List;

@Data
public class SentenceInfoDto {
    private String source;
    private List<String> tokens;
    private List<String> tags;
    private List<String> lemmas;
    private List<Integer> ranks;
    private Integer rank;
}
