package download;

import org.openqa.selenium.TimeoutException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public final class DownloadDirectory {

    private DownloadDirectory() {
    }

    public static Path defaultPath() {
        return Path.of("target", "downloads").toAbsolutePath();
    }

    public static void ensureExists(Path directory) throws IOException {
        Files.createDirectories(directory);
    }

    public static void clear(Path directory) throws IOException {
        if (!Files.isDirectory(directory)) {
            return;
        }
        try (Stream<Path> entries = Files.list(directory)) {
            for (Path entry : entries.toList()) {
                Files.deleteIfExists(entry);
            }
        }
    }

    public static Path waitForFile(Path directory, String extension, Duration timeout) {
        String ext = extension.toLowerCase();
        Instant deadline = Instant.now().plus(timeout);

        while (Instant.now().isBefore(deadline)) {
            Optional<Path> file = findNewestMatchingFile(directory, ext);
            if (file.isPresent()) {
                Path candidate = file.get();
                try {
                    if (Files.size(candidate) > 0 && !candidate.getFileName().toString().endsWith(".crdownload")) {
                        return candidate;
                    }
                } catch (IOException ignored) {
                    // file may still be writing
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        throw new TimeoutException("No downloaded file with extension '" + ext + "' appeared in " + directory);
    }

    private static Optional<Path> findNewestMatchingFile(Path directory, String extension) {
        try (Stream<Path> entries = Files.list(directory)) {
            return entries
                    .filter(Files::isRegularFile)
                    .filter(path -> matchesExtension(path, extension))
                    .max(Comparator.comparing(path -> {
                        try {
                            return Files.getLastModifiedTime(path);
                        } catch (IOException e) {
                            return null;
                        }
                    }));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private static boolean matchesExtension(Path path, String extension) {
        String name = path.getFileName().toString().toLowerCase();
        return name.endsWith("." + extension) && !name.endsWith(".crdownload");
    }
}
