package tools;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import model.Factura;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class generatePDFBill {
    Factura factura;
    String ruta;
    Document doc;
    PdfWriter writer;

    public generatePDFBill(Factura factura, String ruta) {
        this.factura = factura;
        this.ruta = ruta;
    }

    private void newPDF (){
        doc = new Document(PageSize.A4);
        try{
            writer = PdfWriter.getInstance(doc, new FileOutputStream(ruta));
            doc.open();
        }catch(FileNotFoundException ex){
            
        }catch(DocumentException ex){
            
        }
    }
    
    private void setHeaderAndMetadata (){
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        doc.addTitle("Factura El Volante Feliz");
        doc.addSubject("Factura Cliente");
        doc.addCreator("Sistema Gestion El Volante Feliz");
        doc.addAuthor("Eiffel&Cibeles Software");
    }
    
    private void closePDF (){
        doc.close();
    }
    
    private void doCabecera (){
    }
}
