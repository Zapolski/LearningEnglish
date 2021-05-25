package by.zapolski.english.service.lemma.core;

import by.zapolski.english.lemma.domain.Lemma;
import by.zapolski.english.lemma.dto.SentenceInfoDto;
import by.zapolski.english.repository.dictionary.LemmaRepository;
import by.zapolski.english.service.lemma.api.SentenceService;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;

@Service
public class SentenceServiceImpl implements SentenceService {

    @Autowired
    private TokenizerME tokenizer;

    @Autowired
    private POSTaggerME tagger;

    @Autowired
    private DictionaryLemmatizer lemmatizer;

    @Autowired
    private LemmaRepository lemmaRepository;

    @Override
    public SentenceInfoDto getSentenceInfo(String sentence) {
        SentenceInfoDto sentenceInfo = new SentenceInfoDto();
        sentenceInfo.setSource(sentence);

        String[] tokens = tokenizer.tokenize(sentence);

        // сложные слова (с дефисом) разбиваем на два слова
        // и рассматриваем их как самостоятельные токены
        List<String> tokensList = new ArrayList<>();
        for (String token : tokens) {
            if (token.contains("-")) {
                tokensList.addAll(asList(token.split("-")));
            } else {
                tokensList.add(token);
            }
        }
        tokens = tokensList.toArray(new String[0]);

        sentenceInfo.setTokens(asList(tokens));

        String[] tags = tagger.tag(tokens);
        sentenceInfo.setTags(asList(tags));

        String[] lemmas = lemmatizer.lemmatize(tokens, tags);
        sentenceInfo.setLemmas(asList(lemmas));

        // для большей глубины ранжирования заменяем
        // нераспознанные леммы на токены из исходного предложения
        List<String> searchRequest = new ArrayList<>();
        for (int i = 0; i < lemmas.length; i++) {
            searchRequest.add("O".equals(lemmas[i]) ? normalize(tokens[i].toLowerCase()) : lemmas[i]);
        }

        List<Lemma> searchResult = lemmaRepository.findByValueIn(searchRequest);
        List<Integer> ranks = new ArrayList<>();
        searchRequest.forEach(
                lemma -> {
                    Integer rank = searchResult.stream()
                            .filter(search -> search.getValue().equals(lemma))
                            .min(Comparator.comparingInt(Lemma::getRank))
                            .map(Lemma::getRank).orElse(null);
                    ranks.add(rank);
                }
        );

//        Integer[] ranks = new Integer[sentenceInfo.getLemmas().length];
//        int index = 0;
//        for (String lemma : sentenceInfo.getLemmas()) {
//            String search = "O".equals(lemma) ? tokens[index] : lemma;
//            List<Lemma> wordList = lemmaRepository.findByValue(search.toLowerCase());
//            Optional<Lemma> optional = wordList.stream().min(Comparator.comparingInt(Lemma::getRank));
//            ranks[index++] = optional.map(Lemma::getRank).orElse(null);
//        }
        sentenceInfo.setRanks(ranks);

        int rank = ranks.stream().filter(Objects::nonNull).max(Integer::compareTo).orElse(0);
        sentenceInfo.setRank(rank);

        return sentenceInfo;
    }

    private String normalize(String token){
        return token
                .replaceAll("\\.", "")
                .replaceAll("\"", "");
    }


}
