package by.zapolski.english.service.learning.core;

import by.zapolski.english.learning.domain.*;
import by.zapolski.english.learning.dto.PhraseSearchDto;
import by.zapolski.english.service.learning.api.PhraseSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
public class PhraseSpecificationsImpl implements PhraseSpecifications {
    @Override
    public Specification<Phrase> getSpecification(PhraseSearchDto search) {
        return Objects.requireNonNull(Specification.<Phrase>where(spec(search.getWord(), Phrase_.word, Word_.value))
                .and(spec(search.getRanks(), Phrase_.rank))
                .and(greaterOrEqualSpec(search.getMinRank(), Phrase_.rank))
                .and(lessOrEqualSpec(search.getMaxRank(), Phrase_.rank)))
                .and(languageSpec(search))
                .and(spec(search.getLearningStatus(), Phrase_.learningStatus))
                .and(likeSpec(search.getTextQuery(), Phrase_.value));
    }

    private static <T> Specification<T> likeSpec(String value, SingularAttribute<?, ?> attribute, SingularAttribute<?, ?>... attributes) {
        return Specification.where(spec(value, (root, query, cb) -> cb.or(
                        cb.like(path(root, attribute, attributes), "%" + value + "%"))));
    }

    private static Specification<Phrase> languageSpec(PhraseSearchDto search) {
        return (phraseRoot, query, builder) -> {
            if (empty(search.getLanguage())) {
                return null;
            }
            Subquery<Boolean> subquery = query.subquery(Boolean.class);
            Root<Translation> root = subquery.from(Translation.class);
            return builder.exists(
                    subquery.select(builder.literal(Boolean.TRUE))
                            .where(builder.equal(root.get(Translation_.language).get(Language_.value), search.getLanguage())));
        };
    }

    private static <T, V extends Comparable<V>> Specification<T> greaterOrEqualSpec(
            V value, SingularAttribute<?, ?> attribute, SingularAttribute<?, ?>... attributes) {
        return spec(value, (root, query, cb) ->
                cb.greaterThanOrEqualTo(path(root, attribute, attributes), value));
    }

    private static <T, V extends Comparable<V>> Specification<T> lessOrEqualSpec(
            V value, SingularAttribute<?, ?> attribute, SingularAttribute<?, ?>... attributes) {
        return spec(value, (root, query, cb) ->
                cb.lessThanOrEqualTo(path(root, attribute, attributes), value));
    }

    private static boolean empty(Object value) {
        return value == null || value instanceof Collection && ((Collection) value).isEmpty();
    }

    private static <T, V> Specification<T> spec(V value, SingularAttribute<?, ?> attribute, SingularAttribute<?, ?>... attributes) {
        return spec(value, (root, query, cb) -> {
            Path<V> path = path(root, attribute, attributes);
            return value instanceof Collection
                    ? path.in((Collection<?>) value)
                    : cb.equal(path, value);
        });
    }

    private static <T, V> Specification<T> spec(V value, Specification<T> specification) {
        if (empty(value)) {
            return (root, query, cb) ->
                    null;
        }
        return specification;
    }

    @SuppressWarnings("unchecked")
    private static <T, V> Path<V> path(Root<T> root, SingularAttribute<?, ?> attribute, SingularAttribute<?, ?>... attributes) {
        Path<?> path = root.get(attribute.getName());
        for (SingularAttribute<?, ?> k : attributes) {
            path = path.get(k.getName());
        }
        return (Path<V>) path;
    }
}
