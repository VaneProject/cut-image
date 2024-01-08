import java.io.File;
import java.io.Serial;
import java.io.Serializable;

public class SettingFile implements Serializable {
    @Serial
    private static final long serialVersionUID = 1391911266022262825L;

    public String savePath = CutImageRepository.separator;
}
