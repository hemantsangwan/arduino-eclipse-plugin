package it.baeyens.arduino.common;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

public class ConfigurationPreferences {

    // private static final String defaulDownloadLocation = new
    // Path(System.getProperty("user.home")).append("arduinoPlugin").toString();
    // //$NON-NLS-1$ //$NON-NLS-2$

    private static String getGlobalString(String key, String defaultValue) {
	IEclipsePreferences myScope = ConfigurationScope.INSTANCE.getNode(Const.NODE_ARDUINO);
	return myScope.get(key, defaultValue);
    }

    private static void setGlobalString(String key, String value) {
	IEclipsePreferences myScope = ConfigurationScope.INSTANCE.getNode(Const.NODE_ARDUINO);
	myScope.put(key, value);
	try {
	    myScope.flush();
	} catch (BackingStoreException e) {
	    e.printStackTrace();
	}
    }

    public static Path getInstallationPath() {
	final String ArgStart = "-manager_path:"; //$NON-NLS-1$
	String args[] = Platform.getApplicationArgs();
	for (String arg : args) {
	    if (arg.startsWith(ArgStart)) {
		String pathName = arg.substring(ArgStart.length());
		return new Path(pathName);
	    }
	}
	String storedValue = getGlobalString(Const.KEY_MANAGER_DOWNLOAD_LOCATION, Const.EMPTY_STRING);
	if (storedValue.isEmpty()) {
	    URI uri;
	    try {
		uri = Platform.getInstallLocation().getURL().toURI();
		String defaulDownloadLocation = Paths.get(uri).resolve("arduinoPlugin").toString(); //$NON-NLS-1$
		return new Path(defaulDownloadLocation);
	    } catch (URISyntaxException e) {
		// this should not happen
		e.printStackTrace();
	    }

	}
	return new Path(storedValue);
    }

    public static IPath getInstallationPathLibraries() {
	return getInstallationPath().append(Const.LIBRARY_PATH_SUFFIX);
    }

    public static IPath getInstallationPathExamples() {
	return getInstallationPath().append(Const.EXAMPLE_FOLDER_NAME);
    }

    public static IPath getInstallationPathDownload() {
	return getInstallationPath().append(Const.DOWNLOADS_FOLDER);
    }

    /**
     * Get the file that contains the preprocessing platform content
     * 
     * @return
     */
    public static File getPreProcessingPlatformFile() {
	return getInstallationPath().append(Const.PRE_PROCESSING_PLATFORM_TXT).toFile();
    }

    /**
     * Get the file that contains the post processing platform content
     * 
     * @return
     */
    public static File getPostProcessingPlatformFile() {
	return getInstallationPath().append(Const.POST_PROCESSING_PLATFORM_TXT).toFile();
    }

    public static File getPreProcessingBoardsFile() {
	return getInstallationPath().append(Const.PRE_PROCESSING_BOARDS_TXT).toFile();
    }

    public static File getPostProcessingBoardsFile() {
	return getInstallationPath().append(Const.POST_PROCESSING_BOARDS_TXT).toFile();
    }

    public static String getBoardURLs() {
	return getGlobalString(Const.KEY_MANAGER_BOARD_URLS, Const.DEFAULT_MANAGER_BOARD_URLS);
    }

    public static void setBoardURLs(String urls) {
	setGlobalString(Const.KEY_MANAGER_BOARD_URLS, urls);
    }

    public static Path getPathExtensionPath() {
	return new Path(getInstallationPath().append("tools/make").toString()); //$NON-NLS-1$

    }

}
