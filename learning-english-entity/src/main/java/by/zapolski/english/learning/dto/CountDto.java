package by.zapolski.english.learning.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CountDto {
    private Integer notViewedCount;
    private Integer viewedCount;
}
