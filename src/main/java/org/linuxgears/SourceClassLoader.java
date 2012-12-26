package org.linuxgears;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 2/27/11
 * Time: 7:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class SourceClassLoader extends ClassLoader {

    private static final char DOT = '.';
    private static final char DOLLAR = '$';
    private static final String DOT_JAVA = ".java";
    private static final String DOT_CLASS = ".class";

    private Logger logger;

    private JavaCompiler javaCompiler;
    private String sourcePath;
    private String classPath;
    private String buildPath;

    public SourceClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (javaCompiler == null) throw new NullPointerException("Java compiler not defined");
        if (sourcePath == null) throw new NullPointerException("Source path is not defined");

        int packageNameEndsHere = name.lastIndexOf(DOT);
        int outerClassNameEndsHere = name.lastIndexOf(DOLLAR);

        String packageName;
        String className;
        String innerClassName;

        if (packageNameEndsHere > 0) {
            packageName = name.substring(0, packageNameEndsHere);
            if (outerClassNameEndsHere > packageNameEndsHere) {
                className = name.substring(packageNameEndsHere + 1, outerClassNameEndsHere);
            } else {
                className = name.substring(packageNameEndsHere + 1);
            }
        } else {
            packageName = null;
            if (outerClassNameEndsHere > 0) {
                className = name.substring(0, outerClassNameEndsHere);
            } else {
                className = name;
            }
        }

        if (outerClassNameEndsHere > packageNameEndsHere) {
            innerClassName = name.substring(outerClassNameEndsHere + 1);
        } else {
            innerClassName = null;
        }

        String classFilePath = name.replace(DOT, File.separatorChar) + DOT_CLASS;
        String sourceFilePath = ((packageName != null) ? packageName.replace(DOT, File.separatorChar) + File.separator : "")
                + className + DOT_JAVA;

        // Search source path for java class source and place matching path into srcPathBase
        String srcPathBase = null;

        for (String spb: sourcePath.split(File.pathSeparator)) {
            logger.finest("Checking location : " + spb + File.separator + sourceFilePath);
            if (new File((spb + File.separator + sourceFilePath)).exists()) {
                srcPathBase = spb;
                logger.finest("Found source file for [" + name + "] in " + spb + File.separator + sourceFilePath);
                break;
            }
        }

        try {
            File sourceFile = srcPathBase == null ? null : new File(srcPathBase + File.separator + sourceFilePath);

            File classFile;
            if (buildPath != null && buildPath.length() > 0) {
                classFile = new File(buildPath + File.separator + classFilePath);

                // Create any temporary directories we might need
                File buildPathDir = new File(buildPath);
                if (! buildPathDir.exists()) {
                    buildPathDir.mkdirs();
                }
            } else {
                classFile = new File((srcPathBase == null ? "" : srcPathBase + File.separator) + classFilePath);
            }

            if (sourceFile != null) {
                if (sourceFile.lastModified() > classFile.lastModified()) {
                    logger.finest("Compiling class [" + name  + "]");

                    boolean compilationSuccess =
                        javaCompiler
                            .setTargetDir(buildPath)
                            .setSourcePath(sourcePath)
                            .setClassPath(classPath)
                            .compile(sourceFile.getPath()) == 0;
                    if (compilationSuccess) {
                        logger.finest("Compiled [" + sourceFile.getPath() + "]");
                    } else {
                        logger.finest("Compilation failed");
                        throw new RuntimeException("Compilation failed");
                    }
                } else {
                    logger.finest("Class [" + name + "] and it's up to date");
                }
            }

            if (classFile.exists()) {
                if (sourceFile == null) {
                    logger.finest("No source found, but class [" + name + "] already compiled to " + classFile.getPath());
                }
            } else {
                String message = null;

                if (sourceFile == null) {
                    message = "Neither class file or source file found for [" + name + "]";
                } else {
                    message = "Source file found but compilation failed for [" + name + "]";
                }

                logger.finest(message);
                throw new ClassNotFoundException(message);
            }

            logger.finest("Loading class [" + name + "] from " + classFile.getPath());

            FileInputStream fis = new FileInputStream(classFile);
            byte[] classData = new byte[(int)classFile.length()];

            if (fis.read(classData) != classFile.length()) {
                throw new RuntimeException("Compiled class [ " + name + "] cannot be fully read");
            };

            return defineClass(name, classData, 0 , classData.length);

        } catch (IOException ex) {
            logger.warning(ex.getMessage());
            throw new RuntimeException("Compiled class [ " + name + "] cannot be read", ex);
        }

        // unreachable
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public void setJavaCompiler(JavaCompiler javaCompiler) {
        this.javaCompiler = javaCompiler;
    }

    public String getBuildPath() {
        return buildPath;
    }

    public void setBuildPath(String buildPath) {
        this.buildPath = buildPath;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}

