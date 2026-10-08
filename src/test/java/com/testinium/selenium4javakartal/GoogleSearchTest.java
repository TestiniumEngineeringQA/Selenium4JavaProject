package com.testinium.selenium4javakartal;

import com.testinium.driver.TestiniumSeleniumDriver;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class GoogleSearchTest {
    private RemoteWebDriver driver;

    private static final String WRITE_READ_EXCEL_UPLOAD_PATH = "/app/uploads/DenemeExcel.xlsx";
    private static final String READ_TXT_UPLOAD_PATH = "/app/uploads/fileName.txt";

    @Test
    public void searchSelenium() {
        // Dismiss cookie consent if present
        try {
            WebElement consentButton = driver.findElement(By.xpath("//div[contains(@class,'VfPpkd-RLmnJb') or @id='L2AGLb']"));
            if (consentButton.isDisplayed()) {
                consentButton.click();
            }
        } catch (Exception ignored) {}

        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("Selenium");
        searchBox.submit();

        WebElement results = driver.findElement(By.id("search"));
        assertTrue(results.isDisplayed());
        assertTrue(driver.getTitle().toLowerCase().contains("selenium"));
    }

    @Test
    public void runDefaultTest2() throws MalformedURLException, InterruptedException {
        ChromeOptions options =  new ChromeOptions();
        // Testinium anahtarı gerekiyorsa Options üstünden ver:

        // İstersen browser'ı env'den oku
        //String browserName = System.getenv("browser");

        // RemoteWebDriver yerine kendi TestiniumSeleniumDriver'ını options ile başlat
        RemoteWebDriver driver = new TestiniumSeleniumDriver(new URL("http://host.docker.internal:4444/wd/hub"), options);

        driver.get("https://www.amazon.com");
        System.out.println("Page title: " + driver.getTitle());
        Thread.sleep(5000); // 3 saniye bekler

        // 1. COMMAND_PARAMETER
        String demoParam = System.getProperty("commandParameter");
        // 2. ENVIRONMENT_PARAMETER
        String denemeParam = System.getenv("environmentParameter");

        System.out.println(">>> [COMMAND_PARAMETER] demo: " + demoParam);
        System.out.println(">>> [ENVIRONMENT_PARAMETER] deneme: " + denemeParam);

        driver.quit();
    }

    @Test
    public void runDefaultTest3() throws MalformedURLException, InterruptedException {
        ChromeOptions options =  new ChromeOptions();
        // Testinium anahtarı gerekiyorsa Options üstünden ver:

        // İstersen browser'ı env'den oku
        //String browserName = System.getenv("browser");

        // RemoteWebDriver yerine kendi TestiniumSeleniumDriver'ını options ile başlat
        RemoteWebDriver driver = new TestiniumSeleniumDriver(new URL("http://host.docker.internal:4444/wd/hub"), options);

        driver.get("https://www.google.com");
        System.out.println("Page title: " + driver.getTitle());
        Thread.sleep(5000); // 3 saniye bekler

        // 1. COMMAND_PARAMETER
        String demoParam = System.getProperty("commandParameter");
        // 2. ENVIRONMENT_PARAMETER
        String denemeParam = System.getenv("environmentParameter");

        System.out.println(">>> [COMMAND_PARAMETER] demo: " + demoParam);
        System.out.println(">>> [ENVIRONMENT_PARAMETER] deneme: " + denemeParam);

        driver.quit();
    }

    @Test
    public void runDefaultTest4() throws MalformedURLException, InterruptedException {
        ChromeOptions options =  new ChromeOptions();
        // Testinium anahtarı gerekiyorsa Options üstünden ver:

        // İstersen browser'ı env'den oku
        //String browserName = System.getenv("browser");

        // RemoteWebDriver yerine kendi TestiniumSeleniumDriver'ını options ile başlat
        RemoteWebDriver driver = new TestiniumSeleniumDriver(new URL("http://host.docker.internal:4444/wd/hub"), options);

        driver.get("https://www.microsoft.com");
        System.out.println("Page title: " + driver.getTitle());
        Thread.sleep(5000); // 3 saniye bekler

        // 1. COMMAND_PARAMETER
        String demoParam = System.getProperty("commandParameter");
        // 2. ENVIRONMENT_PARAMETER
        String denemeParam = System.getenv("environmentParameter");

        System.out.println(">>> [COMMAND_PARAMETER] demo: " + demoParam);
        System.out.println(">>> [ENVIRONMENT_PARAMETER] deneme: " + denemeParam);

        driver.quit();
    }

    @Test
    public void writeAndReadUploadedExcelFile() throws IOException {
        Path uploadedExcel = Path.of(
                System.getProperty("writeReadUploadedExcelPath", WRITE_READ_EXCEL_UPLOAD_PATH)
        );

        assertTrue(
                Files.isRegularFile(uploadedExcel),
                () -> "Uploaded Excel file was not found: " + uploadedExcel
        );
        assertTrue(Files.size(uploadedExcel) > 0, "Uploaded Excel file is empty on disk");
        System.out.printf(
                "[FILE_UPLOAD_TEST] Uploaded Excel found: path=%s, size=%d bytes%n",
                uploadedExcel,
                Files.size(uploadedExcel)
        );

        Workbook workbook;
        try (InputStream inputStream = Files.newInputStream(uploadedExcel)) {
            workbook = WorkbookFactory.create(inputStream);
        }

        try (workbook; OutputStream outputStream = Files.newOutputStream(uploadedExcel)) {
            assertEquals(1, workbook.getNumberOfSheets(), "Unexpected worksheet count");

            Sheet sheet = workbook.getSheetAt(0);
            assertEquals("Sheet1", sheet.getSheetName());

            sheet.createRow(0).createCell(0).setCellValue("test_id");
            sheet.getRow(0).createCell(1).setCellValue("SELENIUM4_UPLOAD_WRITE_READ_001");
            sheet.createRow(1).createCell(0).setCellValue("status");
            sheet.getRow(1).createCell(1).setCellValue("written");
            sheet.createRow(2).createCell(0).setCellValue("path");
            sheet.getRow(2).createCell(1).setCellValue(uploadedExcel.toString());

            workbook.write(outputStream);
        }

        System.out.printf("[FILE_UPLOAD_TEST] Excel write completed: path=%s%n", uploadedExcel);

        try (InputStream inputStream = Files.newInputStream(uploadedExcel);
             Workbook writtenWorkbook = WorkbookFactory.create(inputStream)) {
            Sheet writtenSheet = writtenWorkbook.getSheet("Sheet1");

            assertNotNull(writtenSheet, "Sheet1 was not found after writing");
            assertEquals("test_id", writtenSheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("SELENIUM4_UPLOAD_WRITE_READ_001", writtenSheet.getRow(0).getCell(1).getStringCellValue());
            assertEquals("status", writtenSheet.getRow(1).getCell(0).getStringCellValue());
            assertEquals("written", writtenSheet.getRow(1).getCell(1).getStringCellValue());
            assertEquals("path", writtenSheet.getRow(2).getCell(0).getStringCellValue());
            assertEquals(uploadedExcel.toString(), writtenSheet.getRow(2).getCell(1).getStringCellValue());
        }

        System.out.printf(
                "[FILE_UPLOAD_TEST] Excel read verification completed successfully: test_id=%s, status=%s%n",
                "SELENIUM4_UPLOAD_WRITE_READ_001",
                "written"
        );
    }

    @Test
    public void readUploadedTxtFile() throws IOException {
        Path uploadedTxt = Path.of(
                System.getProperty("readUploadedTxtPath", READ_TXT_UPLOAD_PATH)
        );

        assertTrue(
                Files.isRegularFile(uploadedTxt),
                () -> "Uploaded txt file was not found: " + uploadedTxt
        );

        String content = Files.readString(uploadedTxt, StandardCharsets.UTF_8);

        System.out.printf("[FILE_READ_TEST] Txt file found: path=%s, size=%d bytes%n",
                uploadedTxt, Files.size(uploadedTxt));
        System.out.println(content);
    }

}
