package by.zapolski.english.dictionary.dto;

import lombok.Data;

@Data
public class SentenceInfoDto {
    private String source;
    private String[] tokens;
    private String[] tags;
    private String[] lemmas;
    private Integer[] ranks;
    private Integer rank;
}