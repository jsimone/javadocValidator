 This is a doclet that extends the Standard javadoc doclet. Executing javadoc with this doclet
 will generate a coverage report for all classes and methods found by the javadoc command.
 This standard doclet will also be called to generate the usual HTML output.
 
 Usage:
 First build the project with maven: mvn install
 Then you can execute it via the command line
 javadoc <usual javadoc options> -classpath <jar that you built with maven> -doclet com.force.sdk.javadoc.JavadocCoverageReportDoclet
 
 Required parameters:
 -coverageOutput <location where the output sould be saved>
 -propertiesLocation <directory where the properties files will be stored>
 
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
		 <additionalparam>-coverageOutput ${project.build.directory}/javaDocCoverage/coverageOutput -propertiesLocation /home/jsimone/dev/properties</additionalparam>
		 <useStandardDocletOptions>false</useStandardDocletOptions>
	     </configuration>
	 </plugin>
 

Note: This could be put into a build phase or into the reporting section for your site phase. If you want to have the report incorporated into your maven site set your output directory as ${project.build.directory}/../src/site/resources. The site plugin will then pick it up and roll it into staged or deployed sites.

Output:
html coverage report
