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
import java.util.Objects;
import java.util.function.Function;

@Component
public class PhraseSpecificationsImpl implements PhraseSpecifications {
    @Override
    public Specification<Phrase> getSpecification(PhraseSearchDto search) {
        return Objects.requireNonNull(Specification.<Phrase>where(spec(search.getWord(), Phrase_.word, Word_.value))
                .and(spec(search.getRanks(), Phrase_.rank))
                .and(greaterThanOrEqualToMinRank(search))
                .and(lessThanOrEqualToMaxRank(search)))
                .and(languageSpec(search))
                .and(like(search));
    }

    private static Specification<Phrase> like(PhraseSearchDto search) {
        return (root, query, builder) -> {
            if (empty(search.getTextQuery())) {
                return null;
            }
            return builder.like(root.get(Phrase_.value), "%" + search.getTextQuery() + "%");
        };
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

    private static Specification<Phrase> lessThanOrEqualToMaxRank(PhraseSearchDto search) {
        return (root, query, builder) -> {
            if (empty(search.getMaxRank())) {
                return null;
            }
            return builder.lessThanOrEqualTo(root.get(Phrase_.rank), search.getMaxRank());
        };
    }

    private static Specification<Phrase> greaterThanOrEqualToMinRank(PhraseSearchDto search) {
        return (root, query, builder) -> {
            if (empty(search.getMinRank())) {
                return null;
            }
            return builder.greaterThanOrEqualTo(root.get(Phrase_.rank), search.getMinRank());
        };
    }

    private static <T> Specification<T> spec(Object value, SingularAttribute<?, ?> attribute, SingularAttribute<?, ?>... attributes) {
        return (root, query, cb) -> {
            if (empty(value)) {
                return null;
            }
            Path<?> path = root.get(attribute.getName());
            for (SingularAttribute<?, ?> k : attributes) {
                path = path.get(k.getName());
            }
            return value instanceof Collection
                    ? path.in((Collection) value)
                    : cb.equal(path, value);
        };
    }

    private static <T, V> Specification<T> spec(V value, Function<V, Predicate> function) {
        return (root, query, cb) ->
                empty(value) ? null : function.apply(value);
    }

    private static boolean empty(Object value) {
        return value == null || value instanceof Collection && ((Collection) value).isEmpty();
    }
}
