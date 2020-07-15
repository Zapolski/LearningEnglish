package by.zapolski.english.utils;

import by.zapolski.english.*;
import by.zapolski.english.domain.*;
import by.zapolski.english.dto.PhraseWithTranslationDto;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class FillerDB {

    private static final String EXCEL_FILE = "phrases.xls";
    private static final String RESOURCE_PATH = "D:/ideaProjects/learning-english/learning-english-web/src/main/resources/static";
    private static final String ALGORITHM = "MD5";

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

    private void fillFromXlsFile() throws FileNotFoundException {

        URL resource = getClass().getClassLoader().getResource(EXCEL_FILE);
        if (resource == null) {
            log.error("File with data does not exist.");
            throw new RuntimeException();
        }

        File file = new File(resource.getFile());
        FileInputStream inputStream = new FileInputStream(file);
        try (HSSFWorkbook workbook = new HSSFWorkbook(inputStream)) {

            HSSFSheet sheet = workbook.getSheetAt(0);
            int rowNum = sheet.getLastRowNum() + 1;
            for (int index = 1; index < rowNum; index++) {

                PhraseWithTranslationDto phraseWithTranslationDto = getTaskRecordFromXlsSheet(sheet, index);
                Word currentWord = wordRepository.getByValue(phraseWithTranslationDto.getWordValue());
                if (currentWord == null) {
                    currentWord = new Word(
                            phraseWithTranslationDto.getWordValue(),
                            phraseWithTranslationDto.getWordRank()
                    );
                }

                Context currentContext = contextRepository.getByValue(phraseWithTranslationDto.getContextValue());
                if (currentContext == null) {
                    currentContext = new Context(phraseWithTranslationDto.getContextValue());
                }

                Language currentLanguage = languageRepository.getByValue(phraseWithTranslationDto.getTranslationLanguageValue());
                if (currentLanguage == null) {
                    currentLanguage = new Language(phraseWithTranslationDto.getTranslationLanguageValue());
                }

                Translation currentTranslation = translationRepository.getByValue(phraseWithTranslationDto.getTranslationValue());
                if (currentTranslation == null) {
                    currentTranslation = new Translation(phraseWithTranslationDto.getTranslationValue());
                }
                List<Translation> translations = new ArrayList<>();
                currentTranslation.setLanguage(currentLanguage);
                translations.add(currentTranslation);

                List<Rule> rules = new ArrayList<>();
                for (String ruleValue : phraseWithTranslationDto.getRules()) {
                    Rule currentRule = ruleRepository.getByValue(ruleValue);
                    if (currentRule == null) {
                        currentRule = new Rule(ruleValue);
                        rules.add(currentRule);
                    }
                }

                Resource currentResource = new Resource();
                currentResource.setStorageType(StorageType.FILE_SYSTEM);
                currentResource.setPath(phraseWithTranslationDto.getResourcePath());
                currentResource.setSize(phraseWithTranslationDto.getResourceSize());
                currentResource.setDuration(phraseWithTranslationDto.getResourceDuration());
                currentResource.setChecksum(phraseWithTranslationDto.getResourceChecksum());

                Phrase currentPhrase = new Phrase(
                        phraseWithTranslationDto.getPhraseValue(),
                        phraseWithTranslationDto.getPhraseRank()
                );
                currentPhrase.setWord(currentWord);
                currentPhrase.setResource(currentResource);
                currentPhrase.setContext(currentContext);
                currentPhrase.setRules(rules);
                currentPhrase.setTranslations(translations);

                currentTranslation.setPhrase(currentPhrase);

                phraseRepository.save(currentPhrase);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        result.setResourceSize(row.getCell(11) != null ? (long) row.getCell(11).getNumericCellValue() : 0);
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        FillerDB fillerDB = new FillerDB();
        fillerDB.fillFromXlsFile();
    }
}
