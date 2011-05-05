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
        
        //check if this is an inner class and if inner classes are being skipped
        if(isInnerClass(classDoc) && policy.isSkipInnerClasses()) {
            return errors;
        }
        
        if(policy.isClassLevelCommentRequired()) {
            if(classDoc.commentText().isEmpty()) {
                errors.add("Class level doc is required");
            }
        }
        
        Tag[] authorTags = classDoc.tags("author");
        if(policy.isClassAuthorTagRequired()) {
            if(authorTags == null || authorTags.length == 0) {
                errors.add("Author tag is required");
            } else {
                for(int i = 0; i < authorTags.length; i++) {
                    if(!authorTags[i].text().matches(policy.getAuthorTagFormat())) {
                        errors.add("Author tag: " + authorTags[i].text() + " does not match the author tag format: " + policy.getAuthorTagFormat());
                    }
                }
            }
                
        }
        
        return errors;
    }
    
    public List<String> verifyMethod(MethodDoc methodDoc) {
        String commentText = methodDoc.commentText();
        List<String> errors = new ArrayList<String>();
        
        //check if this is an inner class and if inner classes are being skipped
        if(isInnerClass(methodDoc) && policy.isSkipInnerClasses()) {
            return errors;
        }
        
        //do not verify simple getters and setters
        if(isGetterOrSetter(methodDoc.name()) || isToString(methodDoc.name())) {
            return errors;
        }
        
        //check if this is an overriden method and if it should be skipped
        if(policy.isSkipOverridenMethods() && isOverridenMethod(methodDoc)) {
            return errors;
        }
        
        //skip if the method is a standard enum method
        if(isStandardEnumMethod(methodDoc)) {
            return errors;
        }
        
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
        } else if(methodDoc.isPackagePrivate()) {
            if(policy.isPackagePrivateMethodCommentRequired() && (commentText == null || commentText.length() == 0)) {
                errors.add("Comments are requried on package private methods");
            }              
        }
        
        return errors;
    }
    
    
    public List<String> verifyConstructor(ConstructorDoc constructorDoc) {
        String commentText = constructorDoc.commentText();
        List<String> errors = new ArrayList<String>();
        
        //check if this is an inner class and if inner classes are being skipped
        if(isInnerClass(constructorDoc) && policy.isSkipInnerClasses()) {
            return errors;
        }       
        
        //do not verify the default constructor or a constructor that was added by the compiler
        if(constructorDoc.isSynthetic() || constructorDoc.parameters().length == 0) {
            return errors;
        }
        
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
        } else if(constructorDoc.isPackagePrivate()) {
            if(policy.isPackagePrivateMethodCommentRequired() && (commentText == null || commentText.length() == 0)) {
                errors.add("Comments are requried on package private constructors");
            } 
        }
        return errors;
    }
    
    private boolean isInnerClass(ClassDoc classDoc) {
        return classDoc.containingClass() != null;
    }
    
    private boolean isInnerClass(ExecutableMemberDoc memberDoc) {
        ClassDoc classDoc = memberDoc.containingClass();
        return isInnerClass(classDoc);
    }
    
    private boolean isGetterOrSetter(String methodName) {
        return methodName != null && (methodName.startsWith("get") || methodName.startsWith("set"));
    }
    
    private boolean isToString(String methodName) {
        return methodName != null && methodName.startsWith("toString");
    }
    
    private boolean isOverridenMethod(MethodDoc methodDoc) {
        AnnotationDesc[] annotations = methodDoc.annotations();
        for(int i=0; i < annotations.length; i++) {
            if("Override".equals(annotations[i].annotationType().name())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isStandardEnumMethod(MethodDoc methodDoc) {
        if(methodDoc.containingClass().isEnum() && 
                ("values".equals(methodDoc.name()) || "valueOf".equals(methodDoc.name()))) {
            return true;
        }
        return false;
    }
}
