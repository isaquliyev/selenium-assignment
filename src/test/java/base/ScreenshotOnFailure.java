package base;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class ScreenshotOnFailure implements TestWatcher {

    private final Supplier<WebDriver> driverSupplier;

    public ScreenshotOnFailure(Supplier<WebDriver> driverSupplier) {
        this.driverSupplier = driverSupplier;
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        WebDriver driver = driverSupplier.get();
        if (!(driver instanceof TakesScreenshot)) return;

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String testName = context.getDisplayName().replaceAll("[^a-zA-Z0-9_-]", "_");
        Path dir = Paths.get("target", "screenshots").toAbsolutePath();
        Path file = dir.resolve(testName + "_" + timestamp + ".png");
        try {
            Files.createDirectories(dir);
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(file, png);
            System.out.println("Screenshot saved: " + file);
        } catch (IOException e) {
            System.err.println("Could not save screenshot: " + e.getMessage());
        }
    }
}
