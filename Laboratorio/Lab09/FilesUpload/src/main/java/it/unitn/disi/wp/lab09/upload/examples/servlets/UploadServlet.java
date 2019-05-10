/*
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 09 - Upload examples
 * UniTN
 */
package it.unitn.disi.wp.lab09.upload.examples.servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Serlvet that handle files uploading
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2019.04.14
 */
@MultipartConfig
public class UploadServlet extends HttpServlet {

    public String uploadDir;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        uploadDir = getServletContext().getInitParameter("uploadDir");
        if (uploadDir == null) {
            throw new ServletException("Please supply uploadDir parameter");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        response.setContentType("text/plain");
        out.println("Demo of a upload using MultiPartRequest");
        out.println();

        out.println("PARAMS: ");
        Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String name = (String) params.nextElement();
            String value = request.getParameter(name);

            out.println(name + ": " + value);
        }
        out.println();

        File uploadDirFile = new File(uploadDir);

        int count = 1;
        Part filePart1 = request.getPart("file1");
        if ((filePart1 != null) && (filePart1.getSize() > 0)) {
            String filename1 = Paths.get(filePart1.getSubmittedFileName()).getFileName().toString();//MSIE  fix.
            File file1 = new File(uploadDirFile, filename1);
            try (InputStream fileContent = filePart1.getInputStream()) {
                Files.copy(fileContent, file1.toPath());
                out.println((count++) + " file: " + file1.getAbsolutePath());
            }
        }

        Part filePart2 = request.getPart("file2");
        if ((filePart2 != null) && (filePart2.getSize() > 0)) {
            String filename2 = Paths.get(filePart2.getSubmittedFileName()).getFileName().toString();
            File file2 = new File(uploadDirFile, filename2);
            try (InputStream fileContent = filePart2.getInputStream()) {
                Files.copy(fileContent, file2.toPath());
                out.println((count++) + " file: " + file2.getAbsolutePath());
            }
        }

        Part filePart3 = request.getPart("file3");
        if ((filePart3 != null) && (filePart3.getSize() > 0)) {
            String filename3 = Paths.get(filePart3.getSubmittedFileName()).getFileName().toString();
            File file3 = new File(uploadDirFile, filename3);
            try (InputStream fileContent = filePart3.getInputStream()) {
                Files.copy(fileContent, file3.toPath());
                out.println((count++) + " file: " + file3.getAbsolutePath());
            }
        }

        List<Part> multiFileParts = request.getParts().stream().filter(part -> "multiplefiles".equals(part.getName())).collect(Collectors.toList());
        if ((multiFileParts != null) && !multiFileParts.isEmpty()) {
            for (Part multiFilePart : multiFileParts) {
                String filename = Paths.get(multiFilePart.getSubmittedFileName()).getFileName().toString();
                File file = new File(uploadDirFile, filename);
                try (InputStream fileContent = multiFilePart.getInputStream()) {
                    Files.copy(fileContent, file.toPath());
                    out.println((count++) + " file: " + file.getAbsolutePath());
                }
            }
        }
    }

}
