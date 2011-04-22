package com.force.sdk.javadoc.output;

import java.io.*;
import java.util.List;

import com.force.sdk.javadoc.ClassVerificationResult;
import com.sun.javadoc.ClassDoc;

public class OutputUtil {
    
    /**
     * Write a line of output to the specified writer
     * 
     * @param msg
     * @param out
     * @throws IOException
     */
    public static void out(String msg, BufferedWriter out) throws IOException {
        out.write(msg);
        out.newLine();
    }

    /**
     * Creates the output directory.
     * 
     * @param coverageOutputDir
     */
    public static void createOutputDirectory(String coverageOutputDir) {
        File outputDirFile = new File(coverageOutputDir);
        if(!outputDirFile.exists()) {
            outputDirFile.mkdirs();
        }
    }
    
    /**
     * Create an output file and return a BufferedWriter that will write to it
     * 
     * @param coverageOutputDir
     * @param fileName
     * @return
     * @throws IOException
     */
    public static BufferedWriter createOutputFile(String coverageOutputDir, String fileName) throws IOException {
        File classesOutFile = new File(coverageOutputDir + "/" + fileName);
        if (classesOutFile.exists()) {
            classesOutFile.delete();
        }
        classesOutFile.createNewFile();
        
        BufferedWriter out = new BufferedWriter(new FileWriter(classesOutFile));
        return out;
    }

    public static void printHeader(BufferedWriter writer) throws IOException{
        out("<html>", writer);
    }

    public static void printFooter(BufferedWriter writer) throws IOException{
        out("</html>", writer);
    }
    
    public static void closeWriter(BufferedWriter writer) {
        if(writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                System.out.println("ERROR: Writer could not be closed");
                e.printStackTrace();
                writer = null;
            }
        }
    }
    
    public static void printErrorList(List<String> errors, BufferedWriter out) throws IOException{
        if(errors.size() != 0) {
            OutputUtil.out("<ul class='errorList'>", out);
            
            for(String error : errors) {
                OutputUtil.out("<li>" + error, out);
            }
            
            OutputUtil.out("</ul>", out);
        } else {
            OutputUtil.out("No Errors", out);
        }
    }
    
    public static void printClassTableHeader(BufferedWriter out) throws IOException {
        out("<table><tr><th>Class</th><th>Class Errors</th><th>Method Errors</th><th>Constructor Errors</th></tr>", out);
    }
    
    public static void printClassTableRow(BufferedWriter out, ClassDoc classDoc, ClassVerificationResult result) throws IOException {
        out("<tr><td><a href='" + classDoc.name() + ".html'>" + classDoc.qualifiedName() + "</a></td>" +
        		"<td>" + result.getClassErrors() + "</td>" +
        		"<td>" + result.getMethodErrors() + "</td>" +
        		"<td>" + result.getConstructorErrors() + "</td></tr>", out);
    }
    
    public static void printClassTableFooter(BufferedWriter out) throws IOException {
        out("</table>", out);
    }
 
}
