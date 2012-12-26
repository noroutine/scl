package org.linuxgears;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 2/27/11
 * Time: 10:52 PM
 * To change this template use File | Settings | File Templates.
 */
class JavaCompiler {

    private final String compiler;
    private String targetDir;
    private String classPath;
    private String sourcePath;

    private SourceLevel target = JavaCompiler.SourceLevel.JDK16;
    private SourceLevel source = JavaCompiler.SourceLevel.JDK16;

    private Logger logger;

    enum SourceLevel {
        JDK13("1.3"), JDK14("1.4"), JDK15("1.5"), JDK16("1.6");

        final String optionValue;

        SourceLevel(String optionValue) {
            this.optionValue = optionValue;
        }
    }

    JavaCompiler(String compiler) {
        this.compiler = compiler;
    }

    private String[] buildCompilerCommand() {

        class CmdList extends ArrayList<String> {
            CmdList(String... es) {
                for (String e: es) {
                    this.add(e);
                }
            }

            CmdList tryAdd(String option, String value) {
                if (value != null && value.length() > 0) {
                    add(option);
                    add(value);
                }
                return this;
            }

            CmdList addIf(String option, boolean condition) {
                if (condition) {
                    add(option);
                }
                return this;
            }
        }

        return new CmdList(compiler)
                .tryAdd("-d", targetDir)
                .tryAdd("-sourcepath", sourcePath)
                .tryAdd("-classpath", classPath)
                .tryAdd("-source", source.optionValue)
                .tryAdd("-target", target.optionValue)
                .toArray(new String[]{});
    }

    int compile(String... src) {

        String[] cmd = Utils.mergeArrays(buildCompilerCommand(), src);

        try {
            logger.finest("Invoking: " + Arrays.toString(cmd));

            Process javac = new ProcessBuilder(cmd).redirectErrorStream(true).start();

            BufferedReader javacReader = new BufferedReader(new InputStreamReader(javac.getInputStream()));
            for (String line = javacReader.readLine(); line != null; line = javacReader.readLine())  {
                logger.finest(line);
            }

            return javac.waitFor();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return -1;
    }

    public JavaCompiler setClassPath(String classPath) {
        this.classPath = classPath;
        return this;
    }

    public String getClassPath() {
        return classPath;
    }

    public JavaCompiler setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
        return this;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public JavaCompiler setTargetDir(String targetDir) {
        this.targetDir = targetDir;
        return this;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

}
