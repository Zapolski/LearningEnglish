package by.zapolski.english.service.learning.core.loader;

import by.zapolski.english.learning.domain.*;
import by.zapolski.english.learning.domain.enums.StorageType;
import by.zapolski.english.lemma.domain.Lemma;
import by.zapolski.english.repository.dictionary.LemmaRepository;
import by.zapolski.english.repository.learning.*;
import by.zapolski.english.service.learning.api.DbService;
import by.zapolski.english.service.learning.api.PhraseService;
import by.zapolski.english.service.lemma.api.SentenceService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DbServiceImpl implements DbService {

    private static final Logger log = LoggerFactory.getLogger(DbServiceImpl.class);

    private static final String EXCEL_FILE = "phrases.xls";
    private static final String BACKUP_FILE = "backup.xls";

    @Autowired
    private PhraseRepository phraseRepository;

    @Autowired
    private LemmaRepository lemmaRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private ContextRepository contextRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private PhraseService phraseService;

    @Autowired
    private SentenceService sentenceService;

    @Override
    @Transactional
    public void resetDatabase() {

        URL resource = getClass().getClassLoader().getResource(EXCEL_FILE);
        if (resource == null) {
            log.error("Error during open {} as resource.", EXCEL_FILE);
            throw new RuntimeException();
        }

        File file = new File(resource.getFile());
        try (
                FileInputStream inputStream = new FileInputStream(file);
                HSSFWorkbook workbook = new HSSFWorkbook(inputStream)
        ) {
            logCounts("---> Counts before clean:");
            phraseRepository.deleteAll();
            log.info("Phrases was deleted.");
            languageRepository.deleteAll();
            log.info("Languages was deleted.");
            wordRepository.deleteAll();
            log.info("Words was deleted.");
            contextRepository.deleteAll();
            log.info("Contexts was deleted.");
            ruleRepository.deleteAll();
            log.info("Rules was deleted.");
            logCounts("---> Counts after clean:");

            Language currentLanguage = new Language("Russian");
            languageRepository.save(currentLanguage);

            List<Word> words = new ArrayList<>();
            List<Context> contexts = new ArrayList<>();
            List<Rule> rules = new ArrayList<>();
            List<Resource> resources = new ArrayList<>();
            List<PhraseWithTranslationDto> dtoPhrases = new ArrayList<>();

            HSSFSheet sheet = workbook.getSheetAt(0);
            int rowNum = sheet.getLastRowNum() + 1;
            for (int index = 1; index < rowNum; index++) {
                PhraseWithTranslationDto phraseWithTranslationDto = getTaskRecordFromXlsSheet(sheet, index);
                dtoPhrases.add(phraseWithTranslationDto);

                Word currentWord = new Word(
                        phraseWithTranslationDto.getWordValue(),
                        phraseWithTranslationDto.getWordRank()
                );
                words.add(currentWord);

                Context currentContext = new Context(phraseWithTranslationDto.getContextValue());
                contexts.add(currentContext);

                for (String ruleValue : phraseWithTranslationDto.getRules()) {
                    Rule currentRule = new Rule(ruleValue);
                    rules.add(currentRule);
                }

                Resource currentResource = new Resource();
                currentResource.setStorageType(StorageType.FILE_SYSTEM);
                currentResource.setPath(phraseWithTranslationDto.getResourcePath());
                currentResource.setSize(phraseWithTranslationDto.getResourceSize());
                currentResource.setDuration(phraseWithTranslationDto.getResourceDuration());
                currentResource.setChecksum(phraseWithTranslationDto.getResourceChecksum());
                resources.add(currentResource);
            }

            rules = rules.stream().distinct().collect(Collectors.toList());
            ruleRepository.saveAll(rules);
            log.info("Rules were added to DB.");

            words = words.stream().distinct().collect(Collectors.toList());
            wordRepository.saveAll(words);
            log.info("Words were added to DB.");

            contexts = contexts.stream().distinct().collect(Collectors.toList());
            contextRepository.saveAll(contexts);
            log.info("Contexts were added to DB.");

            resourceRepository.saveAll(resources);
            log.info("Resources were added to DB.");

            List<Phrase> phrases = new ArrayList<>();
            int index = 1;
            for (PhraseWithTranslationDto phraseWithTranslationDto : dtoPhrases) {
                Translation currentTranslation = new Translation(phraseWithTranslationDto.getTranslationValue());
                List<Translation> translations = new ArrayList<>();
                currentTranslation.setLanguage(currentLanguage);
                translations.add(currentTranslation);

                Phrase currentPhrase = new Phrase(
                        phraseWithTranslationDto.getPhraseValue(),
                        phraseWithTranslationDto.getPhraseRank()
                );

                Word currentWord = words.stream()
                        .filter(w -> w.getValue().equals(phraseWithTranslationDto.getWordValue()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Can't find word"));

                Resource currentResource = resources.stream()
                        .filter(r -> r.getPath().equals(phraseWithTranslationDto.getResourcePath()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Can't find resource"));

                Context currentContext = contexts.stream()
                        .filter(c -> c.getValue().equals(phraseWithTranslationDto.getContextValue()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Can't find context"));


                List<Rule> currentRules = new ArrayList<>();
                for (String ruleValue : phraseWithTranslationDto.getRules()) {
                    Rule currentRule = rules.stream()
                            .filter(r -> r.getValue().equals(ruleValue))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Can't find rule"));

                    currentRules.add(currentRule);
                }

                currentPhrase.setWord(currentWord);
                currentPhrase.setResource(currentResource);
                currentPhrase.setContext(currentContext);
                currentPhrase.setRules(currentRules);
                currentPhrase.setTranslations(translations);

                currentTranslation.setPhrase(currentPhrase);
                phrases.add(currentPhrase);
                log.info("Phrase #{} was added to list.", index++);
            }

            log.info("Adding phrases...");
            phraseRepository.saveAll(phrases);
            log.info("Phrases were added to DB.");

        } catch (IOException e) {
            log.error("Error during open XLS file with data.", e);
        }
        logCounts("---> Counts after adding:");
    }

    @Override
    @Transactional
    public void correctRanks() {

        List<Lemma> lemmas = lemmaRepository.findAll();
        log.info("Корректирую ранги.");
        List<Integer> ranks = lemmas.stream().map(Lemma::getRank).distinct().sorted().collect(Collectors.toList());
        System.out.println(ranks);
        System.out.println("Уникальныйх рангов: " + ranks.size());

        for (int index = 0; index < ranks.size() - 1; index++) {
            Integer newRank = index + 1;
            Integer currentRank = ranks.get(index);
            if (!newRank.equals(currentRank)) {
                int count = 0;
                count = count + lemmaRepository.updateRank(newRank, currentRank);
                count = count + wordRepository.updateRank(newRank, currentRank);
                count = count + phraseRepository.updateRank(newRank, currentRank);
                log.info("Новый ранг: {}. Обновил {} записей", newRank, count);
            }
        }
        log.info("Завершил корректировку рангов.");

    }

    @Override
    @Transactional
    public void correctPhrasesRanks() {
        Integer minRank = 180;
        Integer maxRank = 200;
        List<Phrase> phrases = phraseService.getAllPhrasesWithRank(minRank, maxRank, "Russian");

        phrases.forEach(phrase -> {
            Integer oldRank = phrase.getRank();
            Integer newRank = sentenceService.getSentenceInfo(phrase.getValue()).getRank();
            if (newRank != null && newRank > oldRank) {
                log.info("Меняю {} на {}", oldRank, newRank);
                phrase.setRank(newRank);
                phraseRepository.save(phrase);
            }
        });
    }

    @Transactional
    @Override
    public void backup() {
        List<Phrase> phrases = phraseRepository.findAll();

        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            log.debug("Creating xls-file in memory");
            HSSFSheet sheet = workbook.createSheet("From DB");
            int rowNum = 0;
            log.debug("Creating header row.");
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue("WordValue");
            row.createCell(1).setCellValue("WordRank");
            row.createCell(2).setCellValue("PhraseValue");
            row.createCell(3).setCellValue("PhraseRank");
            row.createCell(4).setCellValue("ResourcePath");
            row.createCell(5).setCellValue("ResourceStorageType");
            row.createCell(6).setCellValue("ContextValue");
            row.createCell(7).setCellValue("TranslationValue");
            row.createCell(8).setCellValue("TranslationLanguageValue");
            row.createCell(9).setCellValue("ResourceSize");
            row.createCell(10).setCellValue("ResourceChecksum");
            row.createCell(11).setCellValue("ResourceDuration");
            row.createCell(12).setCellValue("Rules");

            rowNum = sheet.getLastRowNum() + 1;
            for (Phrase phrase : phrases) {
                log.debug("Process record id=[{}]", phrase.getId());
                row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(phrase.getWord().getValue()); // WordValue
                row.createCell(1).setCellValue(phrase.getWord().getRank()); // WordRank
                row.createCell(2).setCellValue(phrase.getValue()); // PhraseValue
                row.createCell(3).setCellValue(phrase.getRank()); // PhraseRank
                row.createCell(4).setCellValue(phrase.getResource().getPath()); // ResourcePath
                row.createCell(5).setCellValue(phrase.getResource().getStorageType().name()); // ResourceStorageType
                row.createCell(6).setCellValue(phrase.getContext().getValue()); // ContextValue
                row.createCell(7).setCellValue(phrase.getTranslations().get(0).getValue()); // TranslationValue
                row.createCell(8).setCellValue(phrase.getTranslations().get(0).getLanguage().getValue()); // TranslationLanguageValue
                row.createCell(9).setCellValue(phrase.getResource().getSize()); // ResourceSize
                row.createCell(10).setCellValue(phrase.getResource().getChecksum()); // ResourceChecksum
                row.createCell(11).setCellValue(phrase.getResource().getDuration()); // ResourceDuration
                row.createCell(12).setCellValue(""); // Rules
            }

            log.debug("Writing file on disk, destination file: [{}]", BACKUP_FILE);
            try (FileOutputStream out = new FileOutputStream(new File(BACKUP_FILE))) {
                workbook.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logCounts(String message) {
        log.info(message);
        log.info("Phrases: {}", phraseRepository.count());
        log.info("Languages: {}", languageRepository.count());
        log.info("Words: {}", wordRepository.count());
        log.info("Translations: {}", translationRepository.count());
        log.info("Contexts: {}", contextRepository.count());
        log.info("Resources: {}", resourceRepository.count());
        log.info("Rules: {}", ruleRepository.count());
    }

    private PhraseWithTranslationDto getTaskRecordFromXlsSheet(HSSFSheet sheet, int index) {
        PhraseWithTranslationDto result = new PhraseWithTranslationDto();
        Row row = sheet.getRow(index);
        result.setWordValue(row.getCell(0).getStringCellValue());
        result.setWordRank(row.getCell(1) != null ? (int) row.getCell(1).getNumericCellValue() : 0);
        result.setPhraseValue(row.getCell(2).getStringCellValue());
        result.setPhraseRank(row.getCell(3) != null ? (int) row.getCell(3).getNumericCellValue() : 0);
        result.setResourcePath(row.getCell(4).getStringCellValue());
        result.setResourceStorageType(row.getCell(5).getStringCellValue());
        result.setContextValue(row.getCell(6) != null ? row.getCell(6).getStringCellValue() : "");
        result.setTranslationValue(row.getCell(7).getStringCellValue());
        result.setTranslationLanguageValue(row.getCell(8).getStringCellValue());
        result.setResourceSize(row.getCell(9) != null ? (long) row.getCell(9).getNumericCellValue() : 0);
        result.setResourceChecksum(row.getCell(10).getStringCellValue());
        result.setResourceDuration(row.getCell(11) != null ? (long) row.getCell(11).getNumericCellValue() : 0);

        String rulesStr = row.getCell(12) != null ? row.getCell(12).getStringCellValue() : "";
        if (rulesStr.isEmpty()) {
            result.setRules(Collections.emptyList());
        } else {
            result.setRules(Arrays.asList(rulesStr.split(";")));
        }

        return result;
    }
}
