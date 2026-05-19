package download;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class ResourceDownloader {

    private final WebDriver driver;

    public ResourceDownloader(WebDriver driver) {
        this.driver = driver;
    }

    public Path downloadTo(Path targetDirectory, String downloadUrl, String fallbackExtension) throws Exception {
        String filename = resolveFilename(downloadUrl, fallbackExtension);
        Path targetFile = targetDirectory.resolve(filename);

        HttpURLConnection conn = openConnection(downloadUrl, "GET");
        try {
            int code = conn.getResponseCode();
            if (code < 200 || code >= 400) {
                throw new IllegalStateException("Download failed with HTTP " + code + " for " + downloadUrl);
            }
            try (InputStream in = conn.getInputStream();
                 OutputStream out = Files.newOutputStream(targetFile)) {
                in.transferTo(out);
            }
        } finally {
            conn.disconnect();
        }
        return targetFile;
    }

    public String resolveFilename(String downloadUrl, String fallbackExtension) throws Exception {
        String filename = getContentDispositionFilename(downloadUrl);
        if (!filename.isEmpty()) {
            return filename;
        }
        String path = downloadUrl.split("\\?")[0];
        String lastSegment = path.substring(path.lastIndexOf('/') + 1);
        if (lastSegment.contains(".")) {
            return lastSegment;
        }
        return "resource." + fallbackExtension;
    }

    private String getContentDispositionFilename(String downloadUrl) throws Exception {
        HttpURLConnection conn = connectForHead(downloadUrl);
        try {
            String disposition = conn.getHeaderField("Content-Disposition");
            if (disposition != null && disposition.contains("filename=")) {
                return disposition.split("filename=")[1].replaceAll("\"", "").trim();
            }
            return "";
        } finally {
            conn.disconnect();
        }
    }

    private HttpURLConnection connectForHead(String downloadUrl) throws Exception {
        HttpURLConnection conn = openConnection(downloadUrl, "HEAD");
        int code = conn.getResponseCode();
        if (code == 403 || code == 405) {
            conn.disconnect();
            conn = openConnection(downloadUrl, "GET");
        }
        return conn;
    }

    private HttpURLConnection openConnection(String downloadUrl, String method) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(downloadUrl).openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("User-Agent",
                (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;"));
        conn.setRequestProperty("Referer", driver.getCurrentUrl());
        String cookieHeader = driver.manage().getCookies().stream()
                .map(c -> c.getName() + "=" + c.getValue())
                .collect(Collectors.joining("; "));
        if (!cookieHeader.isEmpty()) {
            conn.setRequestProperty("Cookie", cookieHeader);
        }
        conn.setInstanceFollowRedirects(true);
        conn.connect();
        return conn;
    }
}
