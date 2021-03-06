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

        SentenceInfoDto sentenceInfo = getNlpSentenceInfo(sentence);
        List<String> lemmas = sentenceInfo.getLemmas();
        List<String> tokens = sentenceInfo.getTokens();

        // для большей глубины ранжирования заменяем
        // нераспознанные леммы на токены из исходного предложения
        List<String> searchRequest = new ArrayList<>();
        for (int i = 0; i < lemmas.size(); i++) {
            searchRequest.add("O".equals(lemmas.get(i)) ? normalize(tokens.get(i).toLowerCase()) : lemmas.get(i));
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

    @Override
    public String markProperNouns(String sentence) {

        // удаляем возможное маркирование
        String normSentence = sentence.replace("[", "").replace("]", "");
        SentenceInfoDto info = getNlpSentenceInfo(normSentence);
        // в info-структуру сетаем исходное преложение
        info.setSource(sentence);

        for (int i = 0; i < info.getTags().size(); i++) {
            String tag = info.getTags().get(i);
            // находим "Proper noun singular" или "Proper noun plural"
            if ("NNP".equals(tag) || "NNPS".equals(tag)) {
                String properNoun = info.getTokens().get(i);

                // если имя собственное - притяжательное, то окончание тоже захватываем
                // для этого проверяем есть ли следующий токен и является ли он "Possessive ending"
                if ((info.getTags().size() > i + 1) && ("POS".equals(info.getTags().get(i + 1)))) {
                    properNoun = properNoun + info.getTokens().get(i + 1);
                }

                String newProperNoun = "[" + properNoun + "]";
                if (!info.getSource().contains(newProperNoun)) {
                    info.setSource(
                            // заменяем через регулярку, что не было случайных попаданий внутри длинных слов
                            // например имя собсвтенное Dart может входить в другое имя сосбтвенное Dartmouth
                            // Finally, we arrived at [Dartmouth], where the [River] [Dart] joins the sea.
                            info.getSource().replaceAll("\\b" + properNoun + "\\b", newProperNoun));
                }
            }
        }
        return info.getSource();
    }

    private SentenceInfoDto getNlpSentenceInfo(String sentence) {
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

        return sentenceInfo;
    }

    private String normalize(String token) {
        return token
                .replaceAll("\\.", "")
                .replaceAll("\"", "");
    }


}
