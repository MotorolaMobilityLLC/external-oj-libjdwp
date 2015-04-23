/*
 * Copyright (c) 2014, 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @bug 8046817 8073357
 * @summary schemagen fails to generate xsd for enum types.
 * Check that order of Enum values is preserved.
 * @run main/othervm GenerateEnumSchema
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class GenerateEnumSchema {

    private static final String SCHEMA_OUTPUT_FILENAME = "schema1.xsd";
    private static final File schemaOutputFile = new File(SCHEMA_OUTPUT_FILENAME);
    private static final String[] expectedEnums = {
        "\"FIRST\"", "\"ONE\"", "\"TWO\"", "\"THREE\"",
        "\"FOUR\"", "\"FIVE\"", "\"SIX\"", "\"LAST\""};
    private static String schemaContent = "";

    public static void main(String[] args) throws Exception {

        //Check schema generation for class type
        runSchemaGen("TestClassType.java");
        checkIfSchemaGenerated();
        readSchemaContent();
        checkSchemaContent("<xs:complexType name=\"testClassType\">");
        checkSchemaContent("<xs:element name=\"a\" type=\"xs:int\"/>");

        //Check schema generation for enum type
        runSchemaGen("TestEnumType.java");
        checkIfSchemaGenerated();
        readSchemaContent();
        //Check if Enum type schema is generated
        checkSchemaContent("<xs:simpleType name=\"testEnumType\">");
        //Check the sequence of enum values order
        checkEnumOrder();
        schemaOutputFile.delete();
    }

    // Check if schema file successfully generated by schemagen
    private static void checkIfSchemaGenerated() {
        if (!schemaOutputFile.exists()) {
            throw new RuntimeException("FAIL:" + SCHEMA_OUTPUT_FILENAME + " was not generated by schemagen tool");
        }
    }

    //Read schema content from file
    private static void readSchemaContent() throws Exception {
        schemaContent = Files.lines(schemaOutputFile.toPath()).collect(Collectors.joining(""));
    }

    // Check if schema file contains specific string
    private static void checkSchemaContent(String expContent) {
        System.out.print("Check if generated schema contains '" + expContent + "' string: ");
        if (schemaContent.contains(expContent)) {
            System.out.println("OK");
            return;
        }
        System.out.println("FAIL");
        throw new RuntimeException("The '" + expContent + "' is not found in generated schema");
    }

    // Check if the generated schema contains all enum constants
    // and their order is preserved
    private static void checkEnumOrder() throws Exception {
        int prevElem = -1;
        for (String elem : expectedEnums) {
            int curElem = schemaContent.indexOf(elem);
            System.out.println(elem + " position = " + curElem);
            if (curElem < prevElem) {
                throw new RuntimeException("FAIL: Enum values order is incorrect or " + elem + " element is not found");
            }
            prevElem = curElem;
        }
    }

    private static String getClassFilePath(String filename) {
        String testSrc = System.getProperty("test.src");
        if (testSrc == null) {
            testSrc = ".";
        }
        return Paths.get(testSrc).resolve(filename).toString();
    }

    private static String getSchemagen() {
        String javaHome = System.getProperty("java.home");
        if (javaHome.endsWith("jre")) {
            javaHome = new File(javaHome).getParent();
        }
        String schemagen = javaHome + File.separator + "bin" + File.separator + "schemagen";
        if (System.getProperty("os.name").startsWith("Windows")) {
            schemagen = schemagen.concat(".exe");
        }
        return schemagen;
    }

    private static void logOutput(Process p) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String s = r.readLine();
        while (s != null) {
            System.out.println(s.trim());
            s = r.readLine();
        }
    }

    private static void runSchemaGen(String classFile) {
        String schemagen = getSchemagen();

        try {
            System.out.println("Call to schemagen: " + schemagen + " " + classFile);
            String[] schemagen_args = {
                schemagen,
                getClassFilePath(classFile)
            };

            ProcessBuilder pb = new ProcessBuilder(schemagen_args);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            logOutput(p);
            int result = p.waitFor();
            p.destroy();

            if (result != 0) {
                throw new RuntimeException("schemagen failed");
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Can't run schemagen tool. Exception:");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error launching schemagen tool");
        }
    }
}
