// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package ezgenai.actions;

import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;
import ezgenai.ModelManager;
import ezgenai.NativeLoader;
import java.nio.file.Path;
import com.mendix.core.Core;

/**
 * This AfterStartup action will:
 * 1. Load the native libraries from the genai/native-libs folder, using linux or windows depending on the system os
 * 2. Create MxGenAI instances for each folder inside genai/models
 * 
 * If any of this goes wrong, a RuntimeException will be thrown
 */
public class ASU_EZGenAI extends CustomJavaAction<java.lang.Void>
{
	public ASU_EZGenAI(IContext context)
	{
		super(context);
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		// BEGIN USER CODE
		Path genAiResources = Core.getConfiguration().getResourcesPath().toPath().resolve("genai");
		Path nativeLibsPath = genAiResources.resolve("native-libs");
		Path modelsPath = genAiResources.resolve("models");

		NativeLoader.initNatives(nativeLibsPath);		
		ModelManager.init(modelsPath);
		
		return null;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "ASU_EZGenAI";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
