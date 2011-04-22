package com.force.sdk.javadoc.verifier.exception;

public class VerificationPolicyException extends Exception {

    public VerificationPolicyException(String message) {
        super(message);
    }
    
    public VerificationPolicyException(String message, Throwable e) {
        super(message, e);
    }
}
