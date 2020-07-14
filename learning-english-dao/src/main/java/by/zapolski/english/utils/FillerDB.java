package by.zapolski.english.utils;

import by.zapolski.english.dao.api.PhraseDao;
import by.zapolski.english.dao.core.*;
import by.zapolski.english.domain.*;
import by.zapolski.english.dto.PhraseWithTranslationDto;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;

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

    private SessionFactory sessionFactory;
    private PhraseDao phraseDao;
    private LanguageDaoImpl languageDao;
    private WordDaoImpl wordDao;
    private TranslationDaoImpl translationDao;
    private ContextDaoImpl contextDao;
    private ResourceDaoImpl resourceDao;
    private RuleDaoImpl ruleDao;

    public FillerDB() {
        sessionFactory = HibernateUtils.getSessionFactory();
        this.phraseDao = new PhraseDaoImpl(sessionFactory);
        this.languageDao = new LanguageDaoImpl(sessionFactory);
        this.wordDao = new WordDaoImpl(sessionFactory);
        this.translationDao = new TranslationDaoImpl(sessionFactory);
        this.contextDao = new ContextDaoImpl(sessionFactory);
        this.resourceDao = new ResourceDaoImpl(sessionFactory);
        this.ruleDao = new RuleDaoImpl(sessionFactory);
    }

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
                //log.info("Extracted phrase: {}", phraseWithTranslationDto);
                Transaction transaction = null;
                try {
                    transaction = sessionFactory.getCurrentSession().beginTransaction();

                    Word currentWord = wordDao.getByValue(phraseWithTranslationDto.getWordValue());
                    if (currentWord == null) {
                        currentWord = new Word(
                                phraseWithTranslationDto.getWordValue(),
                                phraseWithTranslationDto.getWordRank()
                        );
                    }

                    Context currentContext = contextDao.getByValue(phraseWithTranslationDto.getContextValue());
                    if (currentContext == null) {
                        currentContext = new Context(phraseWithTranslationDto.getContextValue());
                    }

                    Language currentLanguage = languageDao.getByValue(phraseWithTranslationDto.getTranslationLanguageValue());
                    if (currentLanguage == null) {
                        currentLanguage = new Language(phraseWithTranslationDto.getTranslationLanguageValue());
                    }

                    Translation currentTranslation = translationDao.getByValue(phraseWithTranslationDto.getTranslationValue());
                    if (currentTranslation == null) {
                        currentTranslation = new Translation(phraseWithTranslationDto.getTranslationValue());
                    }
                    List<Translation> translations = new ArrayList<>();
                    currentTranslation.setLanguage(currentLanguage);
                    translations.add(currentTranslation);

                    List<Rule> rules = new ArrayList<>();
                    for (String ruleValue : phraseWithTranslationDto.getRules()) {
                        Rule currentRule = ruleDao.getByValue(ruleValue);
                        if (currentRule == null) {
                            currentRule = new Rule(ruleValue);
                            rules.add(currentRule);
                        }
                    }

                    String soundPath = String.format("%s/audio/%s/%s",
                            RESOURCE_PATH,
                            phraseWithTranslationDto.getWordValue(),
                            phraseWithTranslationDto.getResourcePath()
                    );
                    File soundFile = new File(soundPath);
                    if (!soundFile.exists()) {
                        log.error("File [{}] not found.", soundFile);
                    }

                    // duration
                    AudioFile audioFile = AudioFileIO.read(soundFile);
                    AudioHeader audioHeader = audioFile.getAudioHeader();
                    MP3AudioHeader mp3AudioHeader = (MP3AudioHeader) audioHeader;
                    long frameCount = mp3AudioHeader.getNumberOfFrames();
                    double frameDurationInMs = (mp3AudioHeader.getPreciseTrackLength() / (double) frameCount) * 1000;
                    long duration = Math.round((frameCount * frameDurationInMs));

                    // checksum
                    String checksum = getCheckSum(ALGORITHM, soundFile);

                    Resource currentResource = new Resource();
                    currentResource.setStorageType(StorageType.FILE_SYSTEM);
                    currentResource.setPath(phraseWithTranslationDto.getResourcePath());
                    currentResource.setSize(soundFile.length());
                    currentResource.setDuration(duration);
                    currentResource.setChecksum(checksum);

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

                    phraseDao.save(currentPhrase);

                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                        log.error("Error during adding phrase", e);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        log.info("Phrases: {}", phraseDao.getCount());
        log.info("Languages: {}", languageDao.getCount());
        log.info("Words: {}", wordDao.getCount());
        log.info("Translations: {}", translationDao.getCount());
        log.info("Contexts: {}", contextDao.getCount());
        log.info("Resources: {}", resourceDao.getCount());
        log.info("Rules: {}", ruleDao.getCount());
        transaction.commit();

        HibernateUtils.shutdown();
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
        return result;
    }

    private String getCheckSum(String algorithm, File file) throws IOException, NoSuchAlgorithmException {
        try (FileInputStream fis = new FileInputStream(file)) {
            final MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] dataBytes = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(dataBytes)) > 0) {
                md.update(dataBytes, 0, bytesRead);
            }
            byte[] mdBytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte mdByte : mdBytes) {
                sb.append(Integer.toString((mdByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        FillerDB fillerDB = new FillerDB();
        fillerDB.fillFromXlsFile();
    }
}
