package org.linuxgears;

import org.apache.commons.cli.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.logging.*;

/**
 * Hello world!
 *
 */
public class App {

    static Options options;
    static CommandLine cmdLine;
    static boolean verbose;

    static JavaCompiler javaCompiler;
    static SourceClassLoader scl;

    static Logger logger;

    static {
        options = new Options()
                .addOption("sp", "sourcepath", true, "source path locations")
                .addOption("cp", "classpath", true, "class path location")
                .addOption("d", "build", true, "where to store compiled classes")
                .addOption("ea", "enableassertions", false, "enable assertions processing")
                .addOption("v", "verbose", false, "enable more output")
                .addOption("h", "help", false, "this help");
    }


    public static void initCommandLine(String[] args) {
        try {
            cmdLine = new PosixParser().parse(options, args);
        } catch (ParseException e) {
            System.err.println("Parse error" + e.getMessage());
            printHelp();
            System.exit(1);
        }

        if (cmdLine.hasOption("help")) {
            printHelp();
            System.exit(0);
        }

        verbose = cmdLine.hasOption("verbose");
    }

    public static void printHelp() {
        new HelpFormatter().printHelp("scl", options);
    }

    public static void initLogger() {
        logger = Logger.getLogger("scl");

        ConsoleHandler logHandler = new ConsoleHandler();
        logger.setUseParentHandlers(false);
        logger.addHandler(logHandler);

        Formatter onlyMessageFormatter = new Formatter() {
            @Override
            public String format(LogRecord record) {
                return record.getMessage() + "\n";
            }
        };
        logHandler.setFormatter(onlyMessageFormatter);

        if (verbose) {
            logHandler.setLevel(Level.ALL);
            logger.setLevel(Level.ALL);
        } else {
            logHandler.setLevel(Level.OFF);
            logger.setLevel(Level.OFF);
        }
    }

    public static void initClassLoader() {

        class VerboseURLClassLoader extends URLClassLoader {
            VerboseURLClassLoader(URL[] urls) {
                super(urls);
            }

            @Override
            public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
                logger.finest("Parent is trying to load [" + name + "]");

                Class<?> c = null;
                try {
                    c = super.loadClass(name, resolve);
                } catch (ClassNotFoundException ex) {
                    logger.finest("Parent didn't load the class");
                    throw ex;
                }

                logger.finest("Parent loaded the class");
                return c;
            }
        }

        String compiler = System.getProperty("org.linuxgears.scl.java.compiler");
        javaCompiler = new JavaCompiler(compiler);

        // build loader chain based on classpath presence
        if (cmdLine.hasOption("classpath")) {

            String classpath = cmdLine.getOptionValue("classpath");

            logger.finest("Classpath given, building classloading chain. Classpath: " + classpath);

            URLClassLoader parentURLClassLoader = new VerboseURLClassLoader(Utils.getClasspathURLs(classpath));
            scl = new SourceClassLoader(parentURLClassLoader);
            scl.setClassPath(classpath);
        } else {
            scl = new SourceClassLoader(null);
        }

        scl.setJavaCompiler(javaCompiler);
        scl.setLogger(logger);
        javaCompiler.setLogger(logger);

        if (cmdLine.hasOption("build")) {
            scl.setBuildPath(cmdLine.getOptionValue("build"));
            logger.finest("build target: " + scl.getBuildPath());
        }

        if (cmdLine.hasOption("sourcepath")) {
            scl.setSourcePath(cmdLine.getOptionValue("sourcepath"));
        } else {
            scl.setSourcePath(System.getProperty("user.dir"));
        }

        logger.finest("source path: " + scl.getSourcePath());

        if (cmdLine.hasOption("enableassertions")) {
            scl.setDefaultAssertionStatus(true);
            logger.finest("assertions enabled");
        }
    }

    @SuppressWarnings("unchecked")
    public static void main( String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        initCommandLine(args);

        initLogger();

        initClassLoader();

        logger.finest(Arrays.toString(args));

        if (cmdLine.getArgs().length > 0) {
            Class c = null;
            Method m = null;

            try {
                c = scl.loadClass(cmdLine.getArgs()[0]);
                m = c.getMethod("main", String[].class);

                if (! m.getReturnType().isAssignableFrom(void.class)) {
                    throw new NoSuchMethodException();
                }
            } catch (NoSuchMethodException e) {
                // mimic java invocaton error
                NoSuchMethodError nsme = new NoSuchMethodError("main");
                nsme.setStackTrace(new StackTraceElement[0]);
                throw nsme;
            } catch (Throwable e) {
                System.err.println("Cannot execute class: " + e.getMessage());
                System.exit(-1);
            }

            // Do invocation
            try {
                m.invoke(null, (Object) Arrays.copyOfRange(cmdLine.getArgs(), 1, cmdLine.getArgs().length));
            } catch (Throwable e) {
                if (e.getCause() != null) {
                    e = e.getCause();
                }
                e.printStackTrace();
            }

        } else {
            System.err.println("The argument should be at least a single java class");
            printHelp();
        }
    }


}

