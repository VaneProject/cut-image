import java.util.Locale;

public enum LanguageField {
    TITLE("이미지 자르기", "Cut Image", null),
    SAVE("저장", "Save", null),
    LOAD("이미지 불러오기", "Image Load", null),
    CROP("자르기", "Crop", null),
    PATH("저장 경로", "Storage Path", null),
    AUTO_FILE_NAME("파일 이름 자동 지정", "Automatic file name", null),
    WIDTH("넓이", "Width", null),
    HEIGHT("높이", "Height", null),
    FILE("파일", "File", null),
    FILE_NAME("파일명", "File Name", null),
    IMAGE_SELECT("이미지 선택", "Image Select", null),
    NO_IMAGE("이미지를 선택해주세요.", "Please select an image.", null),
    ERROR("에러", "Error", null),
    IMAGE_SIZE_WARRING("이미지 크기를 넘길 수 없습니다.", "Image size cannot be exceeded.", null),
    WIDTH_HEIGHT_WARRING("넓이 또는 높이가 0 일 수 없습니다.", "Width or height cannot be 0.", null)
    ;

    private final Locale locale = Locale.getDefault();
    private final String kor;
    private final String eng;
    private final String jap;

    public String getText() {
        return switch (locale) {
            case Locale l when Locale.KOREA.equals(l) || Locale.KOREAN.equals(l) -> kor;
            case Locale l when Locale.JAPAN.equals(l) -> jap;
            default -> eng;
        };
    }

    LanguageField(String kor, String eng, String jap) {
        this.kor = kor;
        this.eng = eng;
        this.jap = jap;
    }
}
