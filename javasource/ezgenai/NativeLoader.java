package ezgenai;

import java.nio.file.Path;

import com.mendix.core.Core;
import com.mendix.logging.ILogNode;

public class NativeLoader {

    private static ILogNode LOG = Core.getLogger("GenAI - NativeLoader");
    private static final String NATIVE_PATH_PROPERTY = "onnxruntime-genai.native.path";

    public static void initNatives(Path nativeLibsDirectory) {
        String directory = getOSSpecificNativeLibsDirectory(nativeLibsDirectory);
        LOG.debug("Setting property " + NATIVE_PATH_PROPERTY + " to: " + directory);
        System.setProperty(NATIVE_PATH_PROPERTY, directory);
    }

    private static String getOSSpecificNativeLibsDirectory(Path nativeLibsDirectory) {
        String os = System.getProperty("os.name").toLowerCase();
        LOG.debug("System OS: " + os);
        String osSpecificFolderName = os.contains("win") ? "windows" : "linux";
        String osSpecificNativeLibsDirectory = nativeLibsDirectory.resolve(osSpecificFolderName).toAbsolutePath().toString(); 

        return osSpecificNativeLibsDirectory;
    }

}
