package ezgenai;

import ai.onnxruntime.genai.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import com.mendix.core.Core;
import com.mendix.logging.ILogNode;
import com.mendix.thirdparty.org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class MxGenAI {
    private final SimpleGenAI simpleGenAI;
    private final ILogNode LOG;
    private final String name;
    private final Path absoluteModelPath;

    public MxGenAI(String name, Path absoluteModelPath) {
        this.LOG = Core.getLogger("GenAI - " + name);
        this.name = name;
        this.absoluteModelPath = absoluteModelPath;
        String absoluteModelDirectory = absoluteModelPath.toAbsolutePath().toString();
        try {
            LOG.info(String.format("Initializing model %s on path %s", name, absoluteModelDirectory));
            this.simpleGenAI = new SimpleGenAI(absoluteModelDirectory);
        } catch (GenAIException gae) {
            this.LOG.error(gae);
            throw new RuntimeException(gae);
        }
    }

    public String getName() {
        return this.name;
    }

    public Map<String, Object> getDefaultGeneratorParams() {
        try {
            String jsonString = Files.readString(absoluteModelPath.resolve("genai_config.json"));
            // Should exist if initialisation ran succesfully
            JSONObject genaiConfig = new JSONObject(jsonString);
            JSONObject search = genaiConfig.getJSONObject("search");

            Map<String, Object> defaultGeneratorParams = new HashMap<>();
            Iterator<String> keys = search.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = search.get(key);
                defaultGeneratorParams.put(key, value);
            }
            return defaultGeneratorParams;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

    }

    public String getResponse(String prompt, Map<String, Object> searchParams) {
        return this.getResponse(prompt, searchParams, null);
    }

    public String getResponse(String prompt, Map<String, Object> searchParams, Consumer<String> listener) {
        try {

            GeneratorParams generatorParams = this.createGeneratorParams(searchParams);
            long startTime = System.currentTimeMillis();
            this.LOG.debug(String.format("Init inference using model %s", name));
            String response = simpleGenAI.generate(generatorParams, prompt, listener);
            /*
             * Potential feature: easy hook for configuring microflow as listener consumer
             */
            long executionTime = System.currentTimeMillis() - startTime;
            this.LOG.debug(String.format("Inference finished in %d seconds", Math.round(executionTime / 1000)));
            return response;
        } catch (GenAIException gae) {
            this.LOG.error(gae);
            throw new RuntimeException(gae);
        }

    }

    private GeneratorParams createGeneratorParams(Map<String, Object> searchParamsOverride) {
        try {
            GeneratorParams generatorParams = this.simpleGenAI.createGeneratorParams();
            if (searchParamsOverride != null) {
                for (String key : searchParamsOverride.keySet()) {
                    Object value = searchParamsOverride.get(key);
                    if (value instanceof Boolean) {
                        java.lang.Boolean valueBoolean = (java.lang.Boolean) value;
                        this.LOG.debug(
                                String.format("Overriding searchparameter %s with %s", key, valueBoolean.toString()));
                        generatorParams.setSearchOption(key, valueBoolean.booleanValue());
                    } else {
                        java.lang.Double valueDouble = (java.lang.Double) value;
                        this.LOG.debug(
                                String.format("Overriding searchparameter %s with %s", key, valueDouble.toString()));
                        generatorParams.setSearchOption(key, valueDouble.doubleValue());
                    }
                }
            }
            return generatorParams;

        } catch (GenAIException gae) {
            throw new RuntimeException(gae);
        }

    }
}