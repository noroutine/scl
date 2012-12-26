package org.linuxgears;

import sun.net.www.ParseUtil;

import java.io.File;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 2/27/11
 * Time: 11:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {
    @SuppressWarnings("unchecked")
    public static <T> T[] mergeArrays(T[] a, T[] b) {

        if (a == null || a.length == 0) return b;
        if (b == null || b.length == 0) return a;

        T[] m = (T[]) Array.newInstance(a.getClass().getComponentType(), a.length + b.length);

        System.arraycopy(a, 0, m, 0, a.length);
        System.arraycopy(b, 0, m, a.length, b.length);

        return m;
    }

    public static URL[] getClasspathURLs(String classpath) {
        List<URL> urls = new ArrayList<URL>();

        String[] cps = classpath.split(File.pathSeparator);

        for (int i = 0; i < cps.length; i++) {
            try {
                urls.add(ParseUtil.fileToEncodedURL(new File(cps[i])));
            } catch (MalformedURLException ex) {
                // shh
            }
        }

        return urls.toArray(new URL[0]);
    }
}
