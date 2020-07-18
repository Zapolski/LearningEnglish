package by.zapolski.english.service.dictionary.core;

import by.zapolski.english.service.dictionary.api.TextService;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.tokenize.TokenizerME;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class TextServiceImpl implements TextService {

    @Autowired
    private SentenceDetectorME sentenceDetectorME;

    @Autowired
    private TokenizerME tokenizer;

    @Autowired
    private POSTaggerME tagger;

    @Autowired
    private DictionaryLemmatizer lemmatizer;

    @Override
    public Set<String> getUniqueLemmasFromText(String text) {
        Set<String> resultSet = new HashSet<>();

        String[] sentences = sentenceDetectorME.sentDetect(text);
        for (String sentence : sentences) {
            String[] tokens = tokenizer.tokenize(sentence);
            resultSet.addAll(Arrays.asList(lemmatizer.lemmatize(tokens, tagger.tag(tokens))));
        }
        resultSet.remove("O");
        return resultSet;
    }
}
