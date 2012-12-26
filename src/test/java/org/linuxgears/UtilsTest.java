package org.linuxgears;

import org.junit.Test;

import static junit.framework.Assert.*;
import java.net.URL;

/**
 * Unit test for simple App.
 */
public class UtilsTest {

    @Test
    public void test_getClasspathURLs() {
        String classpath = "/tmp";


        URL[] urls = Utils.getClasspathURLs(classpath);

        System.out.println(urls[0]);

    }
}
