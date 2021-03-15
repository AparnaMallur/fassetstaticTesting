package com.fasset.pdf;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDate;
import org.springframework.web.servlet.view.AbstractView;

import com.fasset.entities.Company;
import com.fasset.form.ExceptionReport5Form;
import com.fasset.form.ExceptionReport6Form;
import com.fasset.form.QuotationForm;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFBuilderForExceptionReport5PdfView extends AbstractView{

	public PDFBuilderForExceptionReport5PdfView() {
        setContentType("application/pdf");
    }
 
    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }
         
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    }
    
    protected Document newDocument() {
        return new Document(PageSize.A4);
    }
     
    protected PdfWriter newWriter(Document document, OutputStream os) throws DocumentException {
        return PdfWriter.getInstance(document, os);
    }
     
    protected void prepareWriter(Map<String, Object> model, PdfWriter writer, HttpServletRequest request)
            throws DocumentException {
        writer.setViewerPreferences(getViewerPreferences());
    }
     
    protected int getViewerPreferences() {
        return PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage;
    }
     
    protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request) {
    }
     
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	
    }
    public ByteArrayOutputStream GeneratePDf(List<ExceptionReport6Form> exceptionReport6FormsList,LocalDate fromdate,LocalDate todate,Company company) throws Exception 
    {
    	 HttpServletRequest request = null;
    	 HttpServletResponse response =null;
    	 ByteArrayOutputStream baos = createTemporaryOutputStream();
         Document document = newDocument();
         PdfWriter writer = newWriter(document, baos);
         prepareWriter(new HashMap<String, Object>(), writer,request);
         buildPdfMetadata(new HashMap<String, Object>(), document,request);
         document.open();
         PDFBuilderForExceptionReport5Pdf obj = new PDFBuilderForExceptionReport5Pdf();
         obj.buildPdfDocumentForQuotation(new HashMap<String, Object>(), document, writer, request, response,exceptionReport6FormsList,fromdate,todate,company);
         document.close();
         return baos;
    }
}
