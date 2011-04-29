package com.force.sdk.javadoc;

import java.io.BufferedWriter;
import java.io.IOException;

import com.force.sdk.javadoc.output.OutputUtil;
import com.force.sdk.javadoc.verifier.VerifierStore;
import com.force.sdk.javadoc.verifier.exception.VerificationPolicyException;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;

/**
 * 
 * This is a doclet that extends the Standard javadoc doclet. Executing javadoc with this doclet
 * will generate a coverage report for all classes and methods found by the javadoc command.
 * This standard doclet will also be called to generate the usual HTML output.
 *  
 * Usage:
 * First build the project with maven: mvn install
 * Then you can execute it via the command line
 * javadoc <usual javadoc options> -classpath <jar that you built with maven> -doclet com.force.sdk.javadoc.JavadocCoverageReportDoclet
 *  
 * Required parameters:
 * -coverageOutput <location where the output sould be saved>
 * -propertiesLocation <directory where the properties files will be stored>
 *  
 * Maven Usage:
 * Add this plugin to your pom:
 *      <plugin>
 *           <groupId>org.apache.maven.plugins</groupId>
 *           <artifactId>maven-javadoc-plugin</artifactId>
 *           <version>2.7</version>
 *           <configuration>
 *           <doclet>com.force.sdk.javadoc.JavadocCoverageReportDoclet</doclet>
 *           <docletArtifact>
 *               <groupId>com.force.sdk</groupId>
 *               <artifactId>javadoc-parser</artifactId>
 *               <version>0.0.1-SNAPSHOT</version>
 *           </docletArtifact>
 *          <additionalparam>-coverageOutput ${project.build.directory}/javaDocCoverage/coverageOutput -propertiesLocation /home/jsimone/dev/properties</additionalparam>
 *          <useStandardDocletOptions>false</useStandardDocletOptions>
 *          </configuration>
 *      </plugin>
 *  
 * 
 * Note: This could be put into a build phase or into the reporting section for your site phase. If you want to have the report incorporated into your maven site set your output directory as ${project.build.directory}/../src/site/resources. The site plugin will then pick it up and roll it into staged or deployed sites.
 * 
 * Output:
 * html coverage report
 *
 * @author jsimone
 */
public class JavadocCoverageReportDoclet extends Standard {

    private VerifierStore verifierStore = null;
    
    /**
     * This is the main entry point which will be called by the Doclet API
     * 
     * @param root
     * @return
     */
    public static boolean start(RootDoc root) {
        JavadocCoverageReportDoclet doclet = new JavadocCoverageReportDoclet();
        return doclet.processReport(root);
    }
    
    private boolean processReport(RootDoc root) {
        BufferedWriter classListOut = null;
               
        try {

            String coverageOutputDir = readOption(root.options(), "-coverageOutput");
            String propertiesDir = readOption(root.options(), "-propertiesLocation");
            verifierStore = new VerifierStore(propertiesDir); 
            ClassProcessor classProcessor = new ClassProcessor(verifierStore, coverageOutputDir);
            OutputUtil.createOutputDirectory(coverageOutputDir);
            classListOut = OutputUtil.createOutputFile(coverageOutputDir, "index.html");
            VerificationResult projectVerificationResult = new VerificationResult();
            
            // iterate over all classes.
            ClassDoc[] classes = root.classes();
            
            OutputUtil.printHeader(classListOut);
            OutputUtil.out("<h1>Classes</h1>", classListOut);
            OutputUtil.printClassTableHeader(classListOut);
            for (int i = 0; i < classes.length; i++) {
                VerificationResult result = classProcessor.processClass(classes[i]);
                addVerificationResult(projectVerificationResult, result);
                OutputUtil.printClassTableRow(classListOut, classes[i], result);
            }
            OutputUtil.printClassTableFooter(classListOut);
            
            OutputUtil.out("<h2>Class Summary: </h2>" + projectVerificationResult.toString(), classListOut);
            
            OutputUtil.printFooter(classListOut);
            
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (VerificationPolicyException e) {
            System.out.println("Error Loading Policy: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            OutputUtil.closeWriter(classListOut);
        }
        return Standard.start(root);        
    }
    
    /**
     * Read the specified command line option
     * 
     * @param options
     * @param optionName
     * @return
     */
    private String readOption(String[][] options, String optionName) {
        String optionValue = null;
        for (int i = 0; i < options.length; i++) {
            String[] opt = options[i];
            if (opt[0].equals(optionName)) {
                optionValue = opt[1];
            }
        }
        return optionValue;
    }

    /**
     * This method is how we tell the Doclet API that we will accept these
     * two custom parameters
     * 
     * @param option
     * @return
     */
    public static int optionLength(String option) {
        if (option.equals("-coverageOutput")) { return 2; }
        if (option.equals("-propertiesLocation")) { return 2; }
        return 0;
    }
    
    private void addVerificationResult(VerificationResult projectResult, VerificationResult classResult) {
        projectResult.setClassErrors(projectResult.getClassErrors() + classResult.getClassErrors());
        projectResult.setMethodErrors(projectResult.getMethodErrors() + classResult.getMethodErrors());
        projectResult.setConstructorErrors(projectResult.getConstructorErrors() + classResult.getConstructorErrors());
        
    }

}
