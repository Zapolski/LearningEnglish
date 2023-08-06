package by.zapolski.english.learning.domain.enums;

import java.util.Arrays;

/**
 * Перечесление для статусов фраз
 */
public enum LearningStatus {

    /**
     * новая фраза, учавствует в случайной выборке для новых фраз
     */
    NEW,

    /**
     * фраза в активном изучении, учавствует в ежедневной случайной выборке для изучаемых/повторяемых фраз
     */
    ACTIVE_LEARNING,

    /**
     * изученная фраза, не учавствует в случайных выборках
     */
    LEARNED;

    public static LearningStatus getByName(String name) {
        return Arrays.stream(LearningStatus.values())
                .filter(learningStatus -> learningStatus.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported learning status: " + name));
    }
}
