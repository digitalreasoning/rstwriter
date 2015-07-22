package com.digitalreasoning.rstwriter;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Created by creynolds on 7/13/15.
 */
public class FileWriter {
    private RstFile file;

    public FileWriter(RstFile file){
        this.file = file;
    }

    public void writeTo(Appendable out) throws IOException {
        out.append(file.write());
    }

    public void writeTo(File f) throws IOException {
        if(f.exists() && f.isDirectory()) {
            File rst = new File(f.getCanonicalPath() + "/" + file.getContentBase().getTitle() + ".rst");
            rst.createNewFile();
            PrintWriter w = new PrintWriter(rst);
            w.write(file.write());
            w.close();
        }
        else{
            throw new IllegalArgumentException("File must be an existing directory");
        }
    }

    public void writeTo(Filer filer) throws IOException {
        FileObject fo = filer.createResource(StandardLocation.CLASS_OUTPUT, "",
                file.getContentBase().getTitle() + ".rst");
        Writer w = fo.openWriter();
        w.write(file.write());
        w.close();
    }
}
