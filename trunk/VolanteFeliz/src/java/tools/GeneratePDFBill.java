package tools;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class GeneratePDFBill {

    Factura factura;
    Sucursal suc;
    Cliente cli;
    String rutaRaizWeb;
    Document doc;
    PdfWriter writer;

    public GeneratePDFBill(Factura factura, Sucursal suc, String rutaRaizWeb) {
        this.factura = factura;
        this.suc = suc;
        this.cli = factura.getCliente();
        this.rutaRaizWeb = rutaRaizWeb;
    }

    private void newPDF() throws FileNotFoundException, DocumentException {
        doc = new Document(PageSize.A4);
        writer = PdfWriter.getInstance(doc, new FileOutputStream(rutaRaizWeb + "/staf/billFolder/" + factura.getCodFactura() + ".pdf"));
        doc.open();

    }

    private void setHeaderAndMetadata() {
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        doc.addTitle("Factura El Volante Feliz");
        doc.addSubject("Factura Cliente");
        doc.addCreator("Sistema Gestion El Volante Feliz");
        doc.addAuthor("Eiffel&Cibeles Software");
    }

    private void closePDF() {
        doc.close();
    }

    private PdfPTable generateTable(float widthPercentage, float[] columns, boolean border) {
        PdfPTable tabla = new PdfPTable(columns);
        tabla.setWidthPercentage(100);
        if (!border) {
            tabla.getDefaultCell().disableBorderSide(PdfPCell.LEFT);
            tabla.getDefaultCell().disableBorderSide(PdfPCell.RIGHT);
            tabla.getDefaultCell().disableBorderSide(PdfPCell.TOP);
            tabla.getDefaultCell().disableBorderSide(PdfPCell.BOTTOM);
        }
        return tabla;
    }

    private void eliminarBordeCelda(PdfPCell celda) {
        celda.disableBorderSide(PdfPCell.LEFT);
        celda.disableBorderSide(PdfPCell.RIGHT);
        celda.disableBorderSide(PdfPCell.TOP);
        celda.disableBorderSide(PdfPCell.BOTTOM);
    }

    private void doBillHeader() throws DocumentException, BadElementException, MalformedURLException, IOException {
        //Logo
        Image logo = Image.getInstance(rutaRaizWeb + "/images/logo.png");
        logo.setAlignment(Image.ALIGN_LEFT);
        logo.scalePercent(90);
        PdfPCell celdaLogo = new PdfPCell(logo, true);
        celdaLogo.setRowspan(2);

        //Datos Empresa
        PdfPCell tituloDatosEmpresa = new PdfPCell(new Phrase("Datos Empresa\n", new Font(Font.FontFamily.TIMES_ROMAN, 20)));
        tituloDatosEmpresa.setBackgroundColor(BaseColor.LIGHT_GRAY);
        this.eliminarBordeCelda(tituloDatosEmpresa);

        PdfPCell celdaDatosEmpresa = new PdfPCell();
        Paragraph datosEmpresa = new Paragraph();
        Phrase nombreEmpresa = new Phrase("El Volante Feliz S.A\n");
        datosEmpresa.add(nombreEmpresa);
        Phrase nombreSucursalEmpresa = new Phrase("Sucursal: " + suc.getNombre() + "\n");
        datosEmpresa.add(nombreSucursalEmpresa);
        Phrase dirEmpresa = new Phrase("Direccion: " + suc.getDir() + "\n");
        datosEmpresa.add(dirEmpresa);
        Phrase telefonoEmpresa = new Phrase("Teléfono: " + suc.getTelefono() + "\n");
        datosEmpresa.add(telefonoEmpresa);
        Phrase faxEmpresa = new Phrase("Fax: " + suc.getFax() + "\n");
        datosEmpresa.add(faxEmpresa);

        celdaDatosEmpresa.addElement(datosEmpresa);

        //Datos Cliente
        PdfPCell tituloDatosCliente = new PdfPCell(new Phrase("Datos Cliente\n", new Font(Font.FontFamily.TIMES_ROMAN, 20)));
        tituloDatosCliente.setBackgroundColor(BaseColor.LIGHT_GRAY);
        this.eliminarBordeCelda(tituloDatosCliente);

        PdfPCell celdaDatosCliente = new PdfPCell();
        Paragraph datosCliente = new Paragraph();
        Phrase nombreCliente = new Phrase("Nombre: " + cli.getName() + "\n");
        celdaDatosCliente.addElement(nombreCliente);
        Phrase dniCliente = new Phrase("DNI: " + cli.getDni() + "\n");
        celdaDatosCliente.addElement(dniCliente);
        Phrase dirCliente = new Phrase("Dirección: " + cli.getAddress() + "\n");
        celdaDatosCliente.addElement(dirCliente);
        Phrase telefonoCliente = new Phrase("Teléfono: " + cli.getTelephone() + "\n");
        celdaDatosCliente.addElement(telefonoCliente);
        if (cli.getCompany() != null) {
            Phrase nombreEmpresaCliente = new Phrase("Empresa: " + cli.getCompany());
            celdaDatosCliente.addElement(nombreEmpresaCliente);
        }

        celdaDatosCliente.addElement(datosCliente);

        //Codigo y Fecha
        PdfPCell celdaInfoExtra = new PdfPCell();
        Phrase codFactura = new Phrase("Codigo Factura: " + factura.getCodFactura() + "\n");
        celdaInfoExtra.addElement(codFactura);
        Phrase fecha = new Phrase("Fecha: " + factura.getFechaEmision() + "\n");
        celdaInfoExtra.addElement(fecha);
        celdaInfoExtra.setColspan(4);

        //Añadir elementos de tabla
        float[] columnas = {(float) 25, (float) 10, (float) 50, (float) 50};
        PdfPTable tablaCabecera = this.generateTable(100, columnas, false);
        tablaCabecera.addCell(celdaLogo);
        PdfPCell hueco = new PdfPCell();
        hueco.setRowspan(2);

        this.eliminarBordeCelda(celdaLogo);
        this.eliminarBordeCelda(hueco);
        this.eliminarBordeCelda(celdaDatosEmpresa);
        this.eliminarBordeCelda(celdaDatosCliente);
        this.eliminarBordeCelda(celdaInfoExtra);

        tablaCabecera.addCell(hueco);
        tablaCabecera.addCell(tituloDatosEmpresa);
        tablaCabecera.addCell(tituloDatosCliente);
        tablaCabecera.addCell(celdaDatosEmpresa);
        tablaCabecera.addCell(celdaDatosCliente);
        tablaCabecera.addCell(celdaInfoExtra);
        tablaCabecera.addCell(hueco);
        tablaCabecera.addCell(hueco);

        doc.add(tablaCabecera);
    }

    private void doBillContent() throws DocumentException {
        //Crear Tabla
        float[] columnas = {70, 15, 15};
        PdfPTable tablaContenido = this.generateTable(100, columnas, true);

        //Contenido
        HashMap<String, Alquiler> alquileres = factura.getAlquileres();
        HashMap<String, Incidencia> incidencias = factura.getIncidencias();
        if (factura.getAlquileres() != null) {
            //Titulo Alquileres
            PdfPCell tituloAlquileres = new PdfPCell(new Phrase("Alquileres"));
            tituloAlquileres.setColspan(3);
            tituloAlquileres.setBackgroundColor(BaseColor.LIGHT_GRAY);
            this.eliminarBordeCelda(tituloAlquileres);
            tablaContenido.addCell(tituloAlquileres);

            //Cabecera Alquileres
            PdfPCell celdaTitulo1 = new PdfPCell(new Phrase("Descripcion"));
            PdfPCell celdaTitulo2 = new PdfPCell(new Phrase("Tarifa"));
            PdfPCell celdaTitulo3 = new PdfPCell(new Phrase("Precio (sin IVA)"));
            tablaContenido.addCell(celdaTitulo1);
            tablaContenido.addCell(celdaTitulo2);
            tablaContenido.addCell(celdaTitulo3);

            //Bucle Alquileres
            for (Alquiler alq : alquileres.values()) {
                Phrase descripcion = new Phrase("Alquiler: " + alq.getCodAlquiler() + "\n"
                        + "Marca: " + "Modelo: " + "\n"
                        + "Fecha Salida: " + "Fecha Fin Alquiler: " + "Fecha Entrega: ");
                Phrase tarifa = new Phrase(alq.getTarifa().getNombre());
                Phrase precio = new Phrase(Tools.printBigDecimal(alq.getPrecio()) + " €");
                tablaContenido.addCell(descripcion);
                tablaContenido.addCell(tarifa);
                tablaContenido.addCell(precio);
            }
        }
        if (factura.getIncidencias() != null) {
            //Titulo Incidencias
            PdfPCell tituloIncidencias = new PdfPCell(new Phrase("Incidencias"));
            tituloIncidencias.setColspan(3);
            tituloIncidencias.setBackgroundColor(BaseColor.LIGHT_GRAY);
            this.eliminarBordeCelda(tituloIncidencias);
            tablaContenido.addCell(tituloIncidencias);

            //Cabecera Incidencias
            PdfPCell celdaTitulo1 = new PdfPCell(new Phrase("Descripcion"));
            celdaTitulo1.setColspan(2);
            PdfPCell celdaTitulo2 = new PdfPCell(new Phrase("Precio (sin IVA)"));
            tablaContenido.addCell(celdaTitulo1);
            tablaContenido.addCell(celdaTitulo2);


            //Bucle incidencias
            for (Incidencia inc : incidencias.values()) {
                PdfPCell celdaDescripcion = new PdfPCell();
                Phrase descripcion = new Phrase("Incidencia: " + inc.getCodIncidencia() + "\n"
                        + "Tipo Incidencia: " + inc.getTipoIncidencia().getNombre() + "\n"
                        + "Fecha Incidencia: " + Tools.printDate(inc.getFecha()) + "\n"
                        + "Observaciones: " + inc.getObservaciones());
                celdaDescripcion.setColspan(2);
                celdaDescripcion.addElement(descripcion);
                Phrase precio = new Phrase(Tools.printBigDecimal(inc.getPrecio()) + " €");
                tablaContenido.addCell(celdaDescripcion);
                tablaContenido.addCell(precio);
            }
        }

        //Total de la factura
        if (alquileres != null || incidencias != null) {
            //Total sin IVA
            PdfPCell tituloTotalSinIVA = new PdfPCell(new Phrase("Total Sin IVA"));
            tituloTotalSinIVA.setColspan(2);
            tituloTotalSinIVA.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tablaContenido.addCell(tituloTotalSinIVA);

            PdfPCell totalSinIVA = new PdfPCell(new Phrase(Tools.printBigDecimal(factura.getImporteSinIVA()) + " €"));
            totalSinIVA.setColspan(2);
            tablaContenido.addCell(totalSinIVA);

            //IVA
            PdfPCell tituloIVA = new PdfPCell(new Phrase("IVA"));
            tituloIVA.setColspan(2);
            tituloIVA.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tablaContenido.addCell(tituloIVA);

            PdfPCell IVA = new PdfPCell(new Phrase(factura.getIVA() + " %"));
            IVA.setColspan(2);
            tablaContenido.addCell(IVA);

            //Total con IVA
            PdfPCell tituloTotalConIVA = new PdfPCell(new Phrase("Total Con IVA"));
            tituloTotalConIVA.setColspan(2);
            tituloTotalConIVA.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tablaContenido.addCell(tituloTotalConIVA);

            PdfPCell totalConIVA = new PdfPCell(new Phrase(Tools.printBigDecimal(factura.getImporte()) + " €"));
            totalConIVA.setColspan(2);
            tablaContenido.addCell(totalConIVA);
        }
        doc.add(tablaContenido);
    }

    public boolean generateBill() {
        try {
            this.newPDF();
            this.setHeaderAndMetadata();
            this.doBillHeader();
            this.doBillContent();
            this.closePDF();
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Archivo no encontrado", ex);
        } catch (BadElementException ex) {
            Logger.getLogger(GeneratePDFBill.class.getName()).log(Level.SEVERE, "Medio no válido para generacion pdf", ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(GeneratePDFBill.class.getName()).log(Level.SEVERE, "Error obteniendo medio para generacion pdf", ex);
        } catch (IOException ex) {
            Logger.getLogger(GeneratePDFBill.class.getName()).log(Level.SEVERE, "Error de escritura en PDF", ex);
        } catch (DocumentException ex) {
            Logger.getLogger(GeneratePDFBill.class.getName()).log(Level.SEVERE, "Error en documento PDF", ex);
        }
        return false;
    }
}