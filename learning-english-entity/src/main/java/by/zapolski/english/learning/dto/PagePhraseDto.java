package by.zapolski.english.learning.dto;

import by.zapolski.english.learning.domain.Phrase;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PagePhraseDto {

    List<PhraseDto> content;

    Integer totalElements;

    String totalDuration;
}
