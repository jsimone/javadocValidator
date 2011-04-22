 This is a doclet that extends the Standard javadoc doclet. Executing javadoc with this doclet
 will generate a coverage report for all classes and methods found by the javadoc command.
 This standard doclet will also be called to generate the usual HTML output.
 
 Usage:
 First build the project with maven: mvn install
 Then you can execute it via the command line
 javadoc <usual javadoc options> -classpath <jar that you built with maven> -doclet com.force.sdk.javadoc.JavadocCoverageReportDoclet
 
 Required parameter:
 -coverageOutput <location where the output sould be saved>
 
 Optional parameter:
 -licenseFileLocation <location of the license file>
 If a license file location is given the class comment report will include a flag that indicates whether
 the text contained in the license file appears in the class level comment.
 
 Maven Usage:
 Add this plugin to your pom:
 <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-javadoc-plugin</artifactId>
      <version>2.7</version>
      <configuration>
          <doclet>com.force.sdk.javadoc.JavadocCoverageReportDoclet</doclet>
          <docletArtifact>
              <groupId>com.force.sdk</groupId>
              <artifactId>javadoc-parser</artifactId>
              <version>0.0.1-SNAPSHOT</version>
          </docletArtifact>
         <additionalparam>-coverageOutput ${project.build.directory}/javaDocCoverage/coverageOutput -licenseFileLocation /home/jsimone/dev/lense.txt</additionalparam>
         <useStandardDocletOptions>false</useStandardDocletOptions>
     </configuration>
 </plugin>
 
Output:
Two csv files: ClassCoverage.csv and MethodCoverage.csv
