package by.zapolski.english.config.lemma;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class NLPConfig {

    @Bean
    public SentenceDetectorME getSentenceDetectorME() throws IOException {
        try (InputStream is = getClass().getResourceAsStream(("/model/en-sent.bin"))){
            SentenceModel model = new SentenceModel(is);
            return new SentenceDetectorME(model);
        }
    }

    @Bean
    public TokenizerME getTokenizerME() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/model/en-token.bin")){
            TokenizerModel model = new TokenizerModel(inputStream);
            return new TokenizerME(model);
        }
    }

    @Bean
    public POSTaggerME getPosTaggerME() throws IOException {
        try (InputStream inputStreamPOSTagger = getClass().getResourceAsStream("/model/en-pos-maxent.bin")){
            POSModel posModel = new POSModel(inputStreamPOSTagger);
            return new POSTaggerME(posModel);
        }
    }

    @Bean
    public DictionaryLemmatizer getLemmatizer() throws IOException {
        try (InputStream dictLemmatizer = getClass().getResourceAsStream("/model/en-lemmatizer.dict")){
            return new DictionaryLemmatizer(dictLemmatizer);
        }
    }
}
