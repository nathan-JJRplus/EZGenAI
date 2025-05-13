package ezgenai;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.mendix.core.Core;
import com.mendix.logging.ILogNode;

public class ModelManager {
    private static final ILogNode LOG = Core.getLogger("GenAI - ModelManager");
    private static List<MxGenAI> mxGenAIList = new ArrayList<>();

    public static void init(Path modelsPath) {
        File modelsDirectory = new File(modelsPath.toAbsolutePath().toString());
        for(File modelFolder : modelsDirectory.listFiles()) {
            String name = modelFolder.getName();
            if (modelFolder.isDirectory()) {
                LOG.info("Adding model " + name);
                mxGenAIList.add(new MxGenAI(name, modelFolder.toPath()));
            } else if (name.toLowerCase().startsWith("readme")) {
                continue;
            } else {
                throw new RuntimeException("genAI directory setup incorrectly: make sure its structured like this - resources/genai/models/<modelname>/{modelfiles}");
            }
        }
    }

    public static MxGenAI getModel() {
        if (mxGenAIList.size() == 1) {
            return mxGenAIList.get(0);
        } else if (mxGenAIList.size() > 1){
            throw new RuntimeException("More than 1 model initialized, pass the name of the model required");
        } else {
            throw new RuntimeException("No models initialized - cannot perform prompt");
        }
    }

    
    public static MxGenAI getModel(String name) {
        if (name == null || name.isEmpty() || name.isBlank()) {
            return getModel();
        }

        for (MxGenAI mxGenAI : mxGenAIList) {
            if (mxGenAI.getName().equals(name)) {
                return mxGenAI;
            }
        }
        throw new RuntimeException("No model found with name: " + name);
    }

}
