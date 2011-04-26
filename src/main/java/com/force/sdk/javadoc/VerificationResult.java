package com.force.sdk.javadoc;

public class VerificationResult {

    private int classErrors;
    private int methodErrors;
    private int constructorErrors;
    
    public int getClassErrors() {
        return classErrors;
    }
    public void setClassErrors(int classErrors) {
        this.classErrors = classErrors;
    }
    public int getMethodErrors() {
        return methodErrors;
    }
    public void setMethodErrors(int methodErrors) {
        this.methodErrors = methodErrors;
    }
    public int getConstructorErrors() {
        return constructorErrors;
    }
    public void setConstructorErrors(int constructorErrors) {
        this.constructorErrors = constructorErrors;
    }
    @Override
    public String toString() {
        return "Class Errors: " + classErrors + ", Method Errors: " + methodErrors + ", ConstructorErrors: " + constructorErrors;
    }
    
}
