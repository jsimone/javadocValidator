package com.force.sdk.javadoc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import com.force.sdk.javadoc.output.OutputUtil;
import com.force.sdk.javadoc.verifier.Verifier;
import com.force.sdk.javadoc.verifier.VerifierStore;
import com.force.sdk.javadoc.verifier.exception.VerificationPolicyException;
import com.sun.javadoc.*;

public class ClassProcessor {

    private static final String POLICY_TAG_NAME = "policy";
    private static final String DEFAULT_POLICY = "default";
    
    private VerifierStore verifierStore = null;
    private String outputDirectory = null;
    
    public ClassProcessor(VerifierStore verifierStore, String outputDirectory) {
        this.verifierStore = verifierStore;
        this.outputDirectory = outputDirectory;
    }
    
    public VerificationResult processClass(ClassDoc classDoc) throws VerificationPolicyException{
        
        BufferedWriter writer = null;
        
        try {
            String policy = findPolicy(classDoc);
            Verifier verifier = findVerifier(policy);
            VerificationResult result = new VerificationResult();
            
            writer = OutputUtil.createOutputFile(outputDirectory, classDoc.name() + ".html");
            
            OutputUtil.printHeader(writer);
            OutputUtil.out("<h1>" + classDoc.qualifiedName() + "</h1>", writer);
            OutputUtil.out("<h2>Policy: " + policy + "</h2>", writer);
            OutputUtil.out("<h2>Comment Text: " + classDoc.commentText() + "</h2>", writer);
            OutputUtil.out("<h2>Javadoc Status: </h2>", writer);
            
            List <String> classErrors = verifier.verifyClass(classDoc);
            result.setClassErrors(classErrors.size());
            
            OutputUtil.printErrorList(classErrors, writer);
                        
            MethodDoc[] methods = classDoc.methods();
            ConstructorDoc[] constructors = classDoc.constructors();
            
            OutputUtil.out("<h2>Constructors:</h2>", writer);
            int constructorErrors = 0;
            int methodErrors = 0;
            
            for (int j = 0; j < constructors.length; j++) {
                constructorErrors += processContstructor(constructors[j], verifier, writer);
            }
            
            OutputUtil.out("<h2>Methods:</h2>", writer);
            
            for (int j = 0; j < methods.length; j++) {
                methodErrors += processMethod(methods[j], verifier, writer);
            }
            
            result.setMethodErrors(methodErrors);
            result.setConstructorErrors(constructorErrors);
            OutputUtil.printFooter(writer);
            return result;
        } catch(IOException e) {
            System.out.println("ERROR: IOException while processing class: " + classDoc.qualifiedName());
            e.printStackTrace();
            throw new VerificationPolicyException("IOException while process class", e);
        } finally {
            OutputUtil.closeWriter(writer);
        }
    }
    
    public int processMethod(MethodDoc methodDoc, Verifier verifier, BufferedWriter writer) throws IOException{
        OutputUtil.out("<h3>" + methodDoc.modifiers() + " " + methodDoc.name() +methodDoc.signature() + "</h3>", writer);
        OutputUtil.out("Comment Text: <br/>" + methodDoc.commentText(), writer);
        OutputUtil.out("Issues: <br/>", writer);
        List<String> errors = verifier.verifyMethod(methodDoc);
        OutputUtil.printErrorList(errors, writer);
        return errors.size();
    }
    
    public int processContstructor(ConstructorDoc constructorDoc, Verifier verifier, BufferedWriter writer) throws IOException{
        OutputUtil.out("<h3>" + constructorDoc.modifiers() + " " + constructorDoc.name() + constructorDoc.flatSignature() + "</h3>", writer);
        OutputUtil.out("Comment Text: <br/>" + constructorDoc.commentText(), writer);
        OutputUtil.out("Issues: <br/>", writer);
        List<String> errors = verifier.verifyConstructor(constructorDoc); 
        OutputUtil.printErrorList(errors, writer);
        return errors.size();
    }
    
    private String findPolicy(ClassDoc classDoc) {
        Tag[] tags = classDoc.tags(POLICY_TAG_NAME);
        String policyName = null;
        for(int i=0; i < tags.length; i++) {
            policyName = tags[i].text();
        }
        
        if(policyName == null) {
            policyName = DEFAULT_POLICY;
        }
        
        return policyName;
    }
    
    private Verifier findVerifier(String policy) throws VerificationPolicyException {
        Verifier verifier;
        try {
            verifier = verifierStore.getVerifier(policy);
        } catch (VerificationPolicyException e1) {
            System.out.println("WARN: problem loading verification policy. Using default.: " + e1.getMessage());
            e1.printStackTrace();
            verifier = verifierStore.getVerifier(DEFAULT_POLICY);
        }
        return verifier;
    }
}
