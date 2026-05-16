package base;

public enum Language {
    AZ("", "AZ"),
    RU("/ru", "RU"),
    EN("/en", "EN");

    private final String urlSuffix;
    private final String code;

    Language(String urlSuffix, String code) {
        this.urlSuffix = urlSuffix;
        this.code = code;
    }

    public String getUrl() {
        return "https://opendata.az" + urlSuffix;
    }

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public String getCode() {
        return code;
    }
}