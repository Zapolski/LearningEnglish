package by.zapolski.english.utils;

import by.zapolski.english.dto.PhraseWithTranslationDto;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;

import java.io.*;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Log4j2
public class RetrieveResourceInformation {

    private static final String EXCEL_FILE = "phrases_copy.xls";
    private static final String RESOURCE_PATH = "D:/ideaProjects/learning-english/learning-english-web/";
    private static final String ALGORITHM = "MD5";
    private static final String DESTINATION_PATH = "D:/ideaProjects/learning-english/learning-english-db-service/src/main/resources/resources-info.xls";

    private void fillFromXlsFile() throws FileNotFoundException {

        URL resource = getClass().getClassLoader().getResource(EXCEL_FILE);
        if (resource == null) {
            log.error("File with data does not exist.");
            throw new RuntimeException();
        }

        HSSFWorkbook resourceInfoBook = new HSSFWorkbook();
        HSSFSheet resourceInfoBookSheet = resourceInfoBook.createSheet("DB");
        int resourceInfoBookSheetRowNum = 0;
        Row resourceInfoBookSheetRow = resourceInfoBookSheet.createRow(resourceInfoBookSheetRowNum);
        resourceInfoBookSheetRow.createCell(0).setCellValue("Path");
        resourceInfoBookSheetRow.createCell(1).setCellValue("Size");
        resourceInfoBookSheetRow.createCell(2).setCellValue("Checksum");
        resourceInfoBookSheetRow.createCell(3).setCellValue("Duration");


        File file = new File(resource.getFile());
        FileInputStream inputStream = new FileInputStream(file);
        try (HSSFWorkbook workbook = new HSSFWorkbook(inputStream)) {

            HSSFSheet sheet = workbook.getSheetAt(0);
            int rowNum = sheet.getLastRowNum() + 1;
            for (int index = 1; index < rowNum; index++) {

                PhraseWithTranslationDto phraseWithTranslationDto = getTaskRecordFromXlsSheet(sheet, index);

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
                long duration = 0;
                try{
                    AudioFile audioFile = AudioFileIO.read(soundFile);
                    AudioHeader audioHeader = audioFile.getAudioHeader();
                    MP3AudioHeader mp3AudioHeader = (MP3AudioHeader) audioHeader;
                    long frameCount = mp3AudioHeader.getNumberOfFrames();
                    double frameDurationInMs = (mp3AudioHeader.getPreciseTrackLength() / (double) frameCount) * 1000;
                    duration = Math.round((frameCount * frameDurationInMs));
                }catch (InvalidAudioFrameException exception){
                    log.error("No mp3 file",exception);
                }


                // checksum
                String checksum = getCheckSum(ALGORITHM, soundFile);

                resourceInfoBookSheetRow = resourceInfoBookSheet.createRow(++resourceInfoBookSheetRowNum);
                Cell cell = resourceInfoBookSheetRow.createCell(0);
                cell.setCellValue(phraseWithTranslationDto.getResourcePath());
                cell = resourceInfoBookSheetRow.createCell(1);
                cell.setCellValue(soundFile.length());
                cell = resourceInfoBookSheetRow.createCell(2);
                cell.setCellValue(checksum);
                cell = resourceInfoBookSheetRow.createCell(3);
                cell.setCellValue(duration);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Writing filled file on disk, destination file: [{0}]", DESTINATION_PATH);
        try (FileOutputStream out = new FileOutputStream(new File(DESTINATION_PATH))) {
            resourceInfoBook.write(out);
        } catch (IOException e) {
            log.info("Error during writing xls-file.", e);
        }

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
        RetrieveResourceInformation fillerDB = new RetrieveResourceInformation();
        fillerDB.fillFromXlsFile();
    }
}
