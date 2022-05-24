package org.linuxgears;

import sun.net.www.ParseUtil;

import java.io.File;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * User: grim
 * Date: 2/27/11
 * Time: 11:46 PM
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

    public static URL[] getClasspathURLs(String classpath) {
        List<URL> urls = new ArrayList<URL>();

        String[] cps = classpath.split(File.pathSeparator);

        for (int i = 0; i < cps.length; i++) {
            try {
                urls.add(Utils.fileToEncodedURL(new File(cps[i])));
            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            }
        }

        return urls.toArray(new URL[0]);
    }
}
