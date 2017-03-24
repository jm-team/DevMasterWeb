package com.jumore.devmaster.common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class CodeMirrorModeContainer {
    private static final String Mode_Script_Path_Prefix = "assets/js/codemirror-5.24.2/mode/";
    private static final String Hint_Script_Path_Prefix = "assets/js/codemirror-5.24.2/addon/hint/";
    private static Map<String, CodeMirrorMode> container = new HashMap<String, CodeMirrorMode>();

    static {
        container.put("js", new CodeMirrorMode("text/javascript", 
                Mode_Script_Path_Prefix + "javascript/javascript.js", 
                Hint_Script_Path_Prefix + "javascript-hint.js"));
        
        container.put("css", new CodeMirrorMode("text/css", 
                Mode_Script_Path_Prefix + "css/css.js", 
                Hint_Script_Path_Prefix + "css-hint.js"));
        
        container.put("html", new CodeMirrorMode("text/html", 
                Mode_Script_Path_Prefix + "htmlmixed/htmlmixed.js", 
                Hint_Script_Path_Prefix + "html-hint.js",
                Mode_Script_Path_Prefix + "xml/xml.js", 
                Hint_Script_Path_Prefix + "xml-hint.js", 
                Mode_Script_Path_Prefix + "javascript/javascript.js", 
                Hint_Script_Path_Prefix + "javascript-hint.js", 
                Mode_Script_Path_Prefix + "css/css.js", 
                Hint_Script_Path_Prefix + "css-hint.js"));
        
        container.put("sql", new CodeMirrorMode("text/x-sql", 
                Mode_Script_Path_Prefix + "sql/sql.js", 
                Hint_Script_Path_Prefix + "sql-hint.js"));
        
        container.put("xml", new CodeMirrorMode("xml", 
                Mode_Script_Path_Prefix + "xml/xml.js", 
                Hint_Script_Path_Prefix + "xml-hint.js"));
    }

    public static CodeMirrorMode get(String key) {
        return container.get(key);
    }

    public static CodeMirrorMode get(File file) {
        if (null == file || !file.isFile()) {
            return null;
        }

        String fileName = file.getName();
        int index = fileName.lastIndexOf('.');

        if (index == -1) {
            return null;
        }

        String key = fileName.substring(index + 1);
        return get(key);
    }
}
