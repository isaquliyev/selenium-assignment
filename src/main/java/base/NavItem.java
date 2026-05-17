package base;

public enum NavItem {
    SEARCH("/search", "Datasets"),
    CATEGORIES("/categories", "Categories"),
    ORGANIZATIONS("/organizations", "Organizations"),
    NEWS("/news", "News"),
    OPEN_DATA_STORE("/open-data-store", "Open data store"),
    OPEN_DATA_REQUEST("/open-data-request", "Open data request");

    private final String path;
    private final String expectedTitle;

    NavItem(String path, String expectedTitle) {
        this.path = path;
        this.expectedTitle = expectedTitle;
    }

    public String getPath() { return path; }
    public String getExpectedTitle() { return expectedTitle; }
}