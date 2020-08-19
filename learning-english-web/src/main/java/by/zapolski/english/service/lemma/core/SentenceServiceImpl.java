package by.zapolski.english.service.lemma.core;

import by.zapolski.english.lemma.domain.Lemma;
import by.zapolski.english.lemma.dto.SentenceInfoDto;
import by.zapolski.english.repository.dictionary.DictionaryRepository;
import by.zapolski.english.service.lemma.api.SentenceService;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SentenceServiceImpl implements SentenceService {

    @Autowired
    private TokenizerME tokenizer;

    @Autowired
    private POSTaggerME tagger;

    @Autowired
    private DictionaryLemmatizer lemmatizer;

    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Override
    public SentenceInfoDto getSentenceInfo(String sentence) {
        SentenceInfoDto sentenceInfo = new SentenceInfoDto();

        sentenceInfo.setTokens(tokenizer.tokenize(sentence));
        sentenceInfo.setTags(tagger.tag(sentenceInfo.getTokens()));
        sentenceInfo.setLemmas(lemmatizer.lemmatize(sentenceInfo.getTokens(), sentenceInfo.getTags()));
        sentenceInfo.setSource(sentence);

        Integer[] ranks = new Integer[sentenceInfo.getLemmas().length];

        int rank;
        int index = 0;
        for (String lemma : sentenceInfo.getLemmas()) {
            if (!"O".equals(lemma)) {
                List<Lemma> wordList = dictionaryRepository.findByValue(lemma);
                Optional<Lemma> optional = wordList.stream().min(Comparator.comparingInt(Lemma::getRank));
                int currentRank = optional.isPresent() ? optional.get().getRank() : 0;
                ranks[index] = currentRank;
            }
            index++;
        }

        if (Arrays.stream(ranks).filter(Objects::nonNull).anyMatch(value -> value == 0)) {
            rank = 0;
        } else {
            rank = Arrays.stream(ranks).filter(Objects::nonNull).max(Integer::compareTo).orElse(0);
        }

        sentenceInfo.setRanks(ranks);
        sentenceInfo.setRank(rank);
        return sentenceInfo;
    }


}
