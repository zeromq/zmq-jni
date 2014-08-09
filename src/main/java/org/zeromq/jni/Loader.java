/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.zeromq.jni;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Loader
{

    private Loader()
    {
    }

    public static void loadLibrary(String library)
    {
        try
        {
            System.load(bundled(library));
        }
        catch (Exception e)
        {
            System.loadLibrary(library);
        }
    }

    private static String libraryPath(String library)
    {
        String osArch = System.getProperty("os.arch");
        String osName = System.getProperty("os.name");
        String libraryName = System.mapLibraryName(library);
        return osName + File.separator + osArch + File.separator + libraryName;
    }

    private static String bundled(String library) throws IOException
    {
        InputStream in = null;
        OutputStream out = null;
        try
        {
            String libraryName = File.separator + libraryPath(library);
            in = Loader.class.getResourceAsStream(libraryName);
            File tmpDir = new File(System.getProperty("java.io.tmpdir"));
            String s = System.mapLibraryName(library);
            int idx = s.lastIndexOf('.');
            File file = null;
            if (idx >= 0) {
                file = File.createTempFile(s.substring(0, idx), s.substring(idx), tmpDir);
            } else {
                file = File.createTempFile(s, null, tmpDir);
            }
            file.deleteOnExit();
            out = new FileOutputStream(file);
            int cnt;
            byte buf[] = new byte[4096];
            while ((cnt = in.read(buf)) >= 1)
            {
                out.write(buf, 0, cnt);
            }
            return file.getAbsolutePath();
        }
        finally
        {
            try
            {
                in.close();
            }
            catch (Exception ignore)
            {
            }
            try
            {
                out.close();
            }
            catch (Exception ignore)
            {
            }
        }
    }
}
