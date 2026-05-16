package base;

import org.openqa.selenium.Dimension;

public enum Device {
    DESKTOP(1280, 800),
    MOBILE(768, 800);

    private final int width;
    private final int height;

    Device(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Dimension getDimension() {
        return new Dimension(width, height);
    }
}
