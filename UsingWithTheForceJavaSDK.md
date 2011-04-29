The javadoc parser is already incorporated into the site phase of the java-sdk project. It is run if you use the profile site-internal. In order to configure your local site run you must do the following:

1.copy the default.javadocpolicy.example file from this project into ~/javadocCoverage and strip the .example extension off.
1.run: mvn site -Psite-internal

This will put the javadoc coverage report for each project into project/src/site/resources

If you have apache2 installed in the default way in Ubuntu you can also deploy the site automatically. 

    sudo mvn site:deploy -Psite-internal

That will deploy the full site to your /var/www directory. 
