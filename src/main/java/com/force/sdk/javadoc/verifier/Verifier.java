package com.force.sdk.javadoc.verifier;

import java.util.ArrayList;
import java.util.List;

import com.sun.javadoc.*;

public class Verifier {

    private VerificationPolicy policy;
    
    public Verifier(VerificationPolicy policy) {
        this.policy = policy;
    }
    
    public List<String> verifyClass(ClassDoc classDoc) {
        List<String> errors = new ArrayList<String>();
        if(policy.isClassLevelCommentRequired()) {
            if(classDoc.commentText().isEmpty()) {
                errors.add("Class level doc is required");
            }
        }
        
        Tag[] authorTags = classDoc.tags("author");
        if(policy.isClassAuthorTagRequired() && (authorTags == null || authorTags.length == 0)) {
            errors.add("Author tag is required");
        }
        
        return errors;
    }
    
    public List<String> verifyMethod(MethodDoc methodDoc) {
        String commentText = methodDoc.commentText();
        List<String> errors = new ArrayList<String>();
        if(methodDoc.isPublic()) {
            if(policy.isPublicMethodCommentRequired() && (commentText == null || commentText.length() == 0)) {
                errors.add("Comments are requried on public methods");
            }
            
        } else if(methodDoc.isProtected()) {
            if(policy.isProtectedMethodCommentRequired() && (commentText == null || commentText.length() == 0)) {
                errors.add("Comments are requried on protected methods");
            }
        }else if(methodDoc.isPrivate()) {
            if(policy.isPrivateMethodCommentRequired() && (commentText == null || commentText.length() == 0)) {
                errors.add("Comments are requried on private methods");
            }            
        }
        return errors;
    }
    
    
    public List<String> verifyConstructor(ConstructorDoc constructorDoc) {
        String commentText = constructorDoc.commentText();
        List<String> errors = new ArrayList<String>();
        if(constructorDoc.isPublic()) {
            if(policy.isPublicMethodCommentRequired() && (commentText == null || commentText.length() == 0)) {
                errors.add("Comments are requried on public constructors");
            }
            
        } else if(constructorDoc.isProtected()) {
            if(policy.isProtectedMethodCommentRequired() && (commentText == null || commentText.length() == 0)) {
                errors.add("Comments are requried on protected constructors");
            }
        }else if(constructorDoc.isPrivate()) {
            if(policy.isPrivateMethodCommentRequired() && (commentText == null || commentText.length() == 0)) {
                errors.add("Comments are requried on private constructors");
            }            
        }
        return errors;
    }
    
}
