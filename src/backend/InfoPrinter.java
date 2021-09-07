package backend;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Text;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class InfoPrinter {
        static int i =1;
    public void patientPdf(String name,String lastname,String billing,String birth,String Phone,String Condition,String doctore,String id, String CIN,String appoint,boolean paid){
        Document document = new Document();
        try{
            PdfWriter writer  = PdfWriter.getInstance(document,new FileOutputStream(name+"_"+id+".pdf"));
            document.open();
            //fonts
            // PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            //the logo of the hospital
            String imgSrc ="res\\logo.png";
            Image image = Image.getInstance(imgSrc);
            image.setAbsolutePosition(200,720);
            document.add(image);

            //text appove in frensh
            document.add(new Paragraph("Royaume du Maroc"));
            document.add(new Paragraph("Ministère de la Santé"));
            document.add(new Paragraph("Béni Mellal-khnifra"));
            document.add(new Paragraph("Hôpital Régional Béni Mellal"));


            //the first Row in the table
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(105);
            table.setSpacingBefore(11f);
            float[] colWidth={2f};
            table.setWidths(colWidth);
            PdfPCell c1 = new PdfPCell(new Paragraph("Code de patient :  "+id));
            table.addCell(c1);
            document.add(table);

            //the second Row in the table
            PdfPTable table1 = new PdfPTable(2);
            table1.setWidthPercentage(105);
            float[] colWidth1={2f,2f};
            table1.setWidths(colWidth1);
            PdfPCell c11 = new PdfPCell(new Paragraph("            Identification :"));
            PdfPCell c12 = new PdfPCell(new Paragraph("            Débiteur :"));
            table1.addCell(c11);
            table1.addCell(c12);
            document.add(table1);

            //third Row in the table
            PdfPTable table2 = new PdfPTable(2);
            table2.setWidthPercentage(105);
            Paragraph paragraph = new Paragraph();
            paragraph.add("\nIndex Patient : "+i++);
            paragraph.add("\n\nNom et Prénom : "+name+" "+lastname);
            paragraph.add("\n\nCIN : "+CIN);
            paragraph.add("\n\nCondition : "+Condition+"\n\n");
            float[] colWidth2={2f,2f};
            table2.setWidths(colWidth2);
            PdfPCell c22 = new PdfPCell(paragraph);
            Paragraph paragraph1 = new Paragraph();
            paragraph1.add("\nBillings : "+billing);
            if(!paid){
                paragraph1.add("\n\n\nPAYANT/NON PAYANT :\n OUI" );
            }else{
                paragraph1.add("\n\n\nPAYANT/NON PAYANT :\n NON" );

            }
            PdfPCell c23 = new PdfPCell(paragraph1);
            table2.addCell(c22);
            table2.addCell(c23);
            document.add(table2);

            //the 4 Row in the table
            PdfPTable table3 = new PdfPTable(2);
            table3.setWidthPercentage(105);
            float[] colWidth3={2f,2f};
            table3.setWidths(colWidth3);
            PdfPCell c31 = new PdfPCell(new Paragraph("Doctore : "+doctore));
            PdfPCell c32 = new PdfPCell(new Paragraph("Date de rendez-vous : "+ appoint));
            table3.addCell(c31);
            table3.addCell(c32);
            document.add(table3);
            //show the date in the pdf
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime now = LocalDateTime.now();
            document.add(new Paragraph("Date Actuel :  "+dtf.format(now)));

            document.close();
            writer.close();

            //open the file
            TimeUnit.SECONDS.sleep(2);
            File myFile = new File(name+"_"+id+".pdf");
            Desktop.getDesktop().open(myFile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
