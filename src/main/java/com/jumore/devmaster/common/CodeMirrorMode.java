package com.jumore.devmaster.common;

public class CodeMirrorMode {
    private String name;
    private String[] references;

    public CodeMirrorMode() {
    }
    
    public CodeMirrorMode(String name, String... reference){
        this.name = name;
        this.references = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getReferences() {
        return references;
    }

    public void setReferences(String[] references) {
        this.references = references;
    }

}
