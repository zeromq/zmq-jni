/*
 * Copyright 2013-2014 Trevor Bernard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.zeromq.jni;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Loader {

    private Loader() {
    }

    public static void loadLibrary(String library) {
        try {
            System.load(bundled(library));
        } catch (Exception e) {
            System.loadLibrary(library);
        }
    }

    private static String libraryPath(String library) {
        String osArch = System.getProperty("os.arch");
        String osName = System.getProperty("os.name");
        String libraryName = System.mapLibraryName(library);
        return osName + File.separator + osArch + File.separator + libraryName;
    }

    private static String bundled(String library) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            String libraryName = File.separator + libraryPath(library);
            in = Loader.class.getResourceAsStream(libraryName);
            File tmpDir = new File(System.getProperty("java.io.tmpdir"));
            String s = System.mapLibraryName(library);
            int idx = s.lastIndexOf('.');
            File file = null;
            if (idx >= 0)
                file = File.createTempFile(s.substring(0, idx), s.substring(idx), tmpDir);
            else
                file = File.createTempFile(s, null, tmpDir);
            file.deleteOnExit();
            out = new FileOutputStream(file);
            int cnt;
            byte buf[] = new byte[4096];
            while ((cnt = in.read(buf)) >= 1) {
                out.write(buf, 0, cnt);
            }
            return file.getAbsolutePath();
        } finally {
            try {
                in.close();
            } catch (Exception ignore) {
            }
            try {
                out.close();
            } catch (Exception ignore) {
            }
        }
    }
}
