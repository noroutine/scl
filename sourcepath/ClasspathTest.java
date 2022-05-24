// import sun.misc.Launcher;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 3/5/11
 * Time: 12:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClasspathTest {

    public static URL fileToEncodedURL(File file) throws Exception {
        String path = file.getAbsolutePath();
        // String path = URLEncoder.encode(filePath, "UTF-8");

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.endsWith("/") && file.isDirectory()) {
            path = path + "/";
        }

        return new URL("file://" + path);
    }

    public static void main(String[] args) {
        ClassLoader systemCL = ClassLoader.getSystemClassLoader();
        // Launcher launcher = Launcher.getLauncher();
        // ClassLoader anotherSystemCL = launcher.getClassLoader();
        // System.out.println(systemCL.equals(anotherSystemCL));       // true

        // URLClassLoader urlSystemCL = (URLClassLoader) systemCL;


        try {
            systemCL.loadClass("org.linuxgears.Test");
            System.out.println("class found");
        } catch (ClassNotFoundException e) {
            System.out.println("class not found");
        }


        URLClassLoader childCL = null;
        try {
            System.out.println(fileToEncodedURL(new File("/tmp")));
            childCL = new URLClassLoader(new URL[] { fileToEncodedURL(new File("/tmp")) }, systemCL);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            childCL.loadClass("org.linuxgears.Test");
            System.out.println("class found");
        } catch (ClassNotFoundException e) {
            System.out.println("class not found");
        }


    }
}
