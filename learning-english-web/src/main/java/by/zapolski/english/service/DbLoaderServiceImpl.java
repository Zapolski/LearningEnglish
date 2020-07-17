package by.zapolski.english.service;

import by.zapolski.english.domain.*;
import by.zapolski.english.dto.PhraseWithTranslationDto;
import by.zapolski.english.repository.*;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class DbLoaderServiceImpl implements DbLoaderService {

    private static final String EXCEL_FILE = "phrases.xls";

    @Autowired
    private PhraseRepository phraseRepository;

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
            languageRepository.deleteAll();
            wordRepository.deleteAll();
            contextRepository.deleteAll();
            ruleRepository.deleteAll();
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
