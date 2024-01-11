package data;

import java.util.Locale;

public enum LanguageField {
    TITLE("컷 이미지", "Cut Image", null),
    SAVE("저장", "Save", null),
    LOAD("이미지 불러오기", "Image Load", null),
    CROP("자르기", "Crop", null),
    PATH("저장 경로", "Storage Path", null),
    AUTO_FILE_NAME("파일 이름 자동 지정", "Automatic file name", null),
    WIDTH("너비", "Width", null),
    HEIGHT("높이", "Height", null),
    FILE("파일", "File", null),
    FILE_NAME("파일명", "File Name", null),
    IMAGE_SELECT("이미지 선택", "Image Select", null),
    NO_IMAGE("이미지를 선택해주세요.", "Please select an image.", null),
    ERROR("에러", "Error", null),
    IMAGE_SIZE_WARRING("이미지 크기를 넘길 수 없습니다.", "Image size cannot be exceeded.", null),
    WIDTH_HEIGHT_WARRING("넓이 또는 높이가 0 일 수 없습니다.", "Width or height cannot be 0.", null),
    COLOR("색상", "Color", null),
    COLOR_SELECT("색깔 선택", "Change Colors", null),

    COLOR_FROM("변경할 색", "", null),
    COLOR_TO("변경될 색", "", null),
    USE_COLOR("색 변경 사용", "", null),
    ;

    private final Locale locale = Locale.getDefault();
    private final String kor;
    private final String eng;
    private final String jap;

    public String getText() {
        if (locale.equals(Locale.KOREAN) || locale.equals(Locale.KOREA))
            return kor;
        else if (Locale.JAPAN.equals(locale))
            return jap;
        else return eng;
    }

    LanguageField(String kor, String eng, String jap) {
        this.kor = kor;
        this.eng = eng;
        this.jap = jap;
    }
}
