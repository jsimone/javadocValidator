package com.force.sdk.javadoc.verifier;

import java.util.Properties;

public class VerificationPolicy {

    private String name;
    private boolean publicMethodCommentRequired;
    private boolean protectedMethodCommentRequired;
    private boolean privateMethodCommentRequired;
    private boolean classLevelCommentRequired;
    private boolean classAuthorTagRequired;
    
    private static final String PUB_METHOD_COMMENT_REQ_NAME = "publicMethodCommentRequired";
    private static final String PROT_METHOD_COMMENT_REQ_NAME = "protectedMethodCommentRequired";
    private static final String PRIV_METHOD_COMMENT_REQ_NAME = "privateMethodCommentRequired";

    private static final String CLASS_COMMENT_REQ_NAME = "classCommentRequired";
    private static final String CLASS_AUTHOR_TAG_REQUIRED = "classAuthorTagRequired";
    
    public VerificationPolicy(String name) {
        this.name = name;
    }
    
    public VerificationPolicy(String name, Properties props) {
        this.name = name;
        init(props);
    }
    
    private void init(Properties props) {
        classLevelCommentRequired = isTrue(props.getProperty(CLASS_COMMENT_REQ_NAME));
        classAuthorTagRequired = isTrue(props.getProperty(CLASS_AUTHOR_TAG_REQUIRED));
        publicMethodCommentRequired = isTrue(props.getProperty(PUB_METHOD_COMMENT_REQ_NAME));
        protectedMethodCommentRequired = isTrue(props.getProperty(PROT_METHOD_COMMENT_REQ_NAME));
        privateMethodCommentRequired = isTrue(props.getProperty(PRIV_METHOD_COMMENT_REQ_NAME));
    }
    
    private boolean isTrue(String propertyValue) {
        if(propertyValue != null 
                && !propertyValue.isEmpty()
                && ("true".equalsIgnoreCase(propertyValue) || "1".equals(propertyValue))) {
            return true;
        } else {
            return false;
        }
            
    }
    
    public String getName() {
        return name;
    }
    public boolean isPublicMethodCommentRequired() {
        return publicMethodCommentRequired;
    }
    public void setPublicMethodCommentRequired(boolean publicMethodComment) {
        this.publicMethodCommentRequired = publicMethodComment;
    }
    public boolean isClassLevelCommentRequired() {
        return classLevelCommentRequired;
    }
    public void setClassLevelCommentRequired(boolean classLevelCommentRequired) {
        this.classLevelCommentRequired = classLevelCommentRequired;
    }
    public boolean isProtectedMethodCommentRequired() {
        return protectedMethodCommentRequired;
    }
    public boolean isPrivateMethodCommentRequired() {
        return privateMethodCommentRequired;
    }
    public boolean isClassAuthorTagRequired() {
        return classAuthorTagRequired;
    }
    
}
