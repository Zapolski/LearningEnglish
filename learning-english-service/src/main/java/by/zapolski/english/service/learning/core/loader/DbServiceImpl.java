package by.zapolski.english.service.learning.core.loader;

import by.zapolski.english.learning.domain.Context;
import by.zapolski.english.learning.domain.Language;
import by.zapolski.english.learning.domain.Phrase;
import by.zapolski.english.learning.domain.Resource;
import by.zapolski.english.learning.domain.Rule;
import by.zapolski.english.learning.domain.Translation;
import by.zapolski.english.learning.domain.Word;
import by.zapolski.english.learning.domain.enums.LearningStatus;
import by.zapolski.english.learning.domain.enums.StorageType;
import by.zapolski.english.repository.learning.ContextRepository;
import by.zapolski.english.repository.learning.LanguageRepository;
import by.zapolski.english.repository.learning.PhraseRepository;
import by.zapolski.english.repository.learning.ResourceRepository;
import by.zapolski.english.repository.learning.RuleRepository;
import by.zapolski.english.repository.learning.TranslationRepository;
import by.zapolski.english.repository.learning.WordRepository;
import by.zapolski.english.service.learning.api.DbService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class DbServiceImpl implements DbService {

    private static final Logger log = LoggerFactory.getLogger(DbServiceImpl.class);

    private static final String EXCEL_FILE = "phrases.xls";
    private static final String BACKUP_FILE = "backup.xls";

    private final PhraseRepository phraseRepository;
    private final LanguageRepository languageRepository;
    private final WordRepository wordRepository;
    private final TranslationRepository translationRepository;
    private final ContextRepository contextRepository;
    private final ResourceRepository resourceRepository;
    private final RuleRepository ruleRepository;

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
                currentPhrase.setVerifyDate(phraseWithTranslationDto.getVerifyDate());
                currentPhrase.setLastSuccessViewDate(phraseWithTranslationDto.getLastSuccessViewDate());
                currentPhrase.setSuccessViewsCount(phraseWithTranslationDto.getSuccessViewsCount());
                currentPhrase.setLearningStatus(LearningStatus.getByName(phraseWithTranslationDto.getLearningStatus()));

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
            row.createCell(13).setCellValue("VerifyDate");
            row.createCell(14).setCellValue("LastSuccessViewDate");
            row.createCell(15).setCellValue("SuccessViewsCount");
            row.createCell(16).setCellValue("LearningStatus");

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

                row.createCell(13).setCellValue(phrase.getVerifyDate());// VerifyDate
                row.createCell(14).setCellValue(phrase.getLastSuccessViewDate());// LastSuccessViewDate
                row.createCell(15).setCellValue(ofNullable(phrase.getSuccessViewsCount()).orElse(0));// SuccessViewsCount
                row.createCell(16).setCellValue(ofNullable(phrase.getLearningStatus()).map(Enum::name).orElse("NEW"));// LearningStatus
            }

            log.debug("Writing file on disk, destination file: [{}]", BACKUP_FILE);
            try (FileOutputStream out = new FileOutputStream(BACKUP_FILE)) {
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

        result.setVerifyDate(row.getCell(13) != null ? row.getCell(13).getLocalDateTimeCellValue() : null);
        result.setLastSuccessViewDate(row.getCell(14) != null ? row.getCell(14).getLocalDateTimeCellValue() : null);
        result.setSuccessViewsCount(row.getCell(15) != null ? (int) row.getCell(15).getNumericCellValue() : 0);
        result.setLearningStatus(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : "");

        return result;
    }
}
