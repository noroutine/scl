import sun.misc.Launcher;
import sun.net.www.ParseUtil;

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
    public static void main(String[] args) {
        ClassLoader systemCL = ClassLoader.getSystemClassLoader();
        Launcher launcher = Launcher.getLauncher();
        ClassLoader anotherSystemCL = launcher.getClassLoader();
        System.out.println(systemCL.equals(anotherSystemCL));       // true

        URLClassLoader urlSystemCL = (URLClassLoader) systemCL;


        try {
            urlSystemCL.loadClass("org.linuxgears.Test");
            System.out.println("class found");
        } catch (ClassNotFoundException e) {
            System.out.println("class not found");
        }


        URLClassLoader childCL = null;
        try {
            System.out.println(ParseUtil.fileToEncodedURL(new File("/tmp")));
            childCL = new URLClassLoader(new URL[] { ParseUtil.fileToEncodedURL(new File("/tmp")) }, systemCL);
        } catch (MalformedURLException e) {
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
