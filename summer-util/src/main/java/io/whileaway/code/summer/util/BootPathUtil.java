package io.whileaway.code.summer.util;

import io.whileaway.code.summer.util.exception.JarPackageNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Slf4j
public class BootPathUtil {

    private static File AGENT_PACKAGE_PATH;

    public static File getPath(Class<?> refer) throws JarPackageNotFoundException {
        if (AGENT_PACKAGE_PATH == null) {
            AGENT_PACKAGE_PATH = findPath(refer);
        }
        return AGENT_PACKAGE_PATH;
    }

    public static boolean isPathFound() {
        return AGENT_PACKAGE_PATH != null;
    }

    private static File findPath(Class<?> refer) throws JarPackageNotFoundException {
        String classResourcePath = refer.getName().replaceAll("\\.", "/") + ".class";

        URL resource = ClassLoader.getSystemClassLoader().getResource(classResourcePath);
        if (resource != null) {
            String urlString = resource.toString();

            log.debug("The beacon class location is {}.", urlString);

            int insidePathIndex = urlString.indexOf('!');
            boolean isInJar = insidePathIndex > -1;

            if (isInJar) {
                urlString = urlString.substring(urlString.indexOf("file:"), insidePathIndex);
                try {
                    File agentJarFile = new File(new URL(urlString).toURI());
                    if (agentJarFile.exists()) {
                        return agentJarFile.getParentFile();
                    }
                } catch (MalformedURLException | URISyntaxException e) {
                    log.error("Can not locate agent jar file by url:" + urlString, e);
                }
            } else {
                int prefixLength = "file:".length();
                String classLocation = urlString.substring(
                        prefixLength, urlString.length() - classResourcePath.length());
                return new File(classLocation);
            }
        }

        log.error("Can not locate agent jar file.");
        throw new JarPackageNotFoundException("Can not locate agent jar file.");
    }
}
