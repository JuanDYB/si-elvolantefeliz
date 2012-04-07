package tools;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Cliente;
import model.Factura;
import model.Sucursal;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class generatePDFBill {
    Factura factura;
    Sucursal suc;
    Cliente cli;
    String rutaRaizWeb;
    Document doc;
    PdfWriter writer;

    public generatePDFBill(Factura factura, Sucursal suc, Cliente cli, String rutaRaizWeb) {
        this.factura = factura;
        this.suc = suc;
        this.cli = cli;
        this.rutaRaizWeb = rutaRaizWeb;
    }

    private void newPDF (){
        doc = new Document(PageSize.A4);
        try{
            writer = PdfWriter.getInstance(doc, new FileOutputStream(rutaRaizWeb + "/staf/billFolder/prueba.pdf"));
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
    
    private boolean doCabecera (){
        try {
            //Logo, Fecha y codigo
            
            Image logo = Image.getInstance(rutaRaizWeb + "/images/logo.png");
            logo.setAlignment(Image.ALIGN_LEFT);
            logo.scalePercent(90);
            PdfPCell celdaLogo = new PdfPCell(logo, true);
            
            //Datos Empresa
            PdfPCell celdaDatosEmpresa = new PdfPCell();
            Paragraph datosEmpresa = new Paragraph();
            Phrase tituloDatosEmpresa = new Phrase("Datos Empresa\n");
            tituloDatosEmpresa.setFont(new Font(Font.FontFamily.TIMES_ROMAN, 20));
            celdaDatosEmpresa.addElement(tituloDatosEmpresa);
            Phrase nombreEmpresa = new Phrase("El Volante Feliz S.A\n");
            datosEmpresa.add(nombreEmpresa);
            Phrase nombreSucursalEmpresa = new Phrase("Sucursal: " + suc.getNombre() + "\n");
            datosEmpresa.add(nombreSucursalEmpresa);
            Phrase dirEmpresa = new Phrase("Direccion: " + suc.getDir() + "\n");
            datosEmpresa.add(dirEmpresa);
            Phrase telefonoEmpresa = new Phrase("Teléfono: " + suc.getTelefono() + "\n");
            datosEmpresa.add(telefonoEmpresa);
            Phrase faxEmpresa = new Phrase("Fax: " + suc.getFax() +"\n");
            datosEmpresa.add(faxEmpresa);
            
            celdaDatosEmpresa.addElement(datosEmpresa);
            
            //Datos Cliente
            PdfPCell celdaDatosCliente = new PdfPCell();
            Paragraph datosCliente = new Paragraph();
            Phrase tituloDatosCliente = new Phrase("Datos Cliente\n");
            tituloDatosCliente.setFont(new Font(Font.FontFamily.TIMES_ROMAN, 20));
            celdaDatosCliente.addElement(tituloDatosCliente);
            Phrase nombreCliente = new Phrase("Nombre: " + cli.getName() + "\n");
            celdaDatosCliente.addElement(nombreCliente);
            Phrase dniCliente = new Phrase("DNI: " + cli.getDni() + "\n");
            celdaDatosCliente.addElement(dniCliente);
            Phrase dirCliente = new Phrase("Dirección: " + cli.getAddress() + "\n");
            celdaDatosCliente.addElement(dirCliente);
            Phrase telefonoCliente = new Phrase("Teléfono: " + cli.getTelephone() + "\n");
            celdaDatosCliente.addElement(telefonoCliente);
            
            celdaDatosCliente.addElement(datosCliente);
            
            //Añadir elementos de tabla
            float [] columnas = {(float)25, (float)10, (float)50, (float)50};
            PdfPTable tablaCabecera = new PdfPTable(columnas);
            tablaCabecera.addCell(celdaLogo);
            PdfPCell hueco = new PdfPCell();
            hueco.disableBorderSide(PdfPCell.TOP);
            hueco.disableBorderSide(PdfPCell.BOTTOM);
            tablaCabecera.addCell(hueco);
            tablaCabecera.addCell(celdaDatosEmpresa);
            tablaCabecera.addCell(celdaDatosCliente);
            
            doc.add(tablaCabecera);
            doc.add(logo);
            
            if (cli.getCompany() != null){
                Phrase nombreEmpresaCliente = new Phrase("Empresa: " + cli.getCompany());
                celdaDatosCliente.addElement(nombreEmpresaCliente);
            }
                
        } catch (BadElementException ex) {
            Logger.getLogger(generatePDFBill.class.getName()).log(Level.SEVERE, "Medio no válido para generacion pdf", ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(generatePDFBill.class.getName()).log(Level.SEVERE, "Error obteniendo medio para generacion pdf", ex);
        } catch (IOException ex) {
            Logger.getLogger(generatePDFBill.class.getName()).log(Level.SEVERE, "Error de escritura en PDF", ex);
        } catch (DocumentException ex){
            Logger.getLogger(generatePDFBill.class.getName()).log(Level.SEVERE, "Error en documento PDF", ex);
        }
        return false;
    }
    
    public void generateBill (){
        this.newPDF();
        this.setHeaderAndMetadata();
        this.doCabecera();
        this.closePDF();
    }
}
