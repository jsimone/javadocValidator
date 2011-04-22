package com.force.sdk.javadoc.verifier;

import java.io.*;
import java.util.Hashtable;
import java.util.Properties;

import com.force.sdk.javadoc.verifier.exception.VerificationPolicyException;

public class VerifierStore {

    private Hashtable<String, Verifier> verifiers;
    private String propertiesDir = null;
    
    public VerifierStore(String propertiesDir) {
        verifiers = new Hashtable<String, Verifier>();
        this.propertiesDir = propertiesDir;
    }
    
    public Verifier getVerifier(String policyName) throws VerificationPolicyException {
        if(!verifiers.containsKey(policyName)) {
            loadVerifier(policyName);
        }

        return verifiers.get(policyName);
    }
    
    private void loadVerifier(String policyName) throws VerificationPolicyException {
        Properties props = loadProperties(policyName);
        
        Verifier verifier = new Verifier(new VerificationPolicy(policyName, props));
        verifiers.put(policyName, verifier);
    }
    
    private Properties loadProperties(String policyName) throws VerificationPolicyException {
        File file = new File(propertiesDir + "/" + policyName + ".javadocpolicy.properties");
        Properties props = new Properties();
        try {
            props.load(new BufferedReader(new FileReader(file)));
        } catch (Exception e) {
            throw new VerificationPolicyException("Policy not found: " + policyName, e);
        }
        
        return props;
    }
    
}
