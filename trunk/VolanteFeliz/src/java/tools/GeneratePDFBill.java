package tools;

import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
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

    private Factura factura;
    private Sucursal suc;
    private Cliente cli;
    private String rutaRaizWeb;
    private Document doc;
    private PdfWriter writer;
    private final Font fuenteTitulo_1 = new Font(FontFamily.TIMES_ROMAN, 22, Font.BOLD);
    private final Font fuenteTitulo_2 = new Font(FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private final Font fuenteTitulo_3 = new Font(FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    private final Font fuenteNormal = new Font(FontFamily.TIMES_ROMAN, 11, Font.NORMAL);
    private final Font fuenteNormalDestacado = new Font(FontFamily.TIMES_ROMAN, 11, Font.BOLD);

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
        logo.setBorder(0);
        logo.setBorderWidth(0);
        PdfPCell celdaLogo = new PdfPCell(logo, true);
        celdaLogo.setRowspan(2);

        //Datos Empresa
        PdfPCell tituloDatosEmpresa = new PdfPCell(new Phrase("Datos Empresa\n", fuenteTitulo_1));
        tituloDatosEmpresa.setBackgroundColor(BaseColor.LIGHT_GRAY);
        

        PdfPCell celdaDatosEmpresa = new PdfPCell();
        Paragraph datosEmpresa = new Paragraph();
        datosEmpresa.add(new Phrase("El Volante Feliz S.A\n", fuenteNormalDestacado));
        datosEmpresa.add(new Phrase("Sucursal: ", fuenteNormalDestacado));
        datosEmpresa.add(new Phrase (suc.getNombre() + "\n", fuenteNormal));
        datosEmpresa.add(new Phrase ("Direccion: ", fuenteNormalDestacado));
        datosEmpresa.add(new Phrase(suc.getDir() + "\n", fuenteNormal));
        datosEmpresa.add(new Phrase ("Teléfono: ", fuenteNormalDestacado));
        datosEmpresa.add(new Phrase (suc.getTelefono() + "\n", fuenteNormal));
        datosEmpresa.add(new Phrase ("Fax: ", fuenteNormalDestacado));
        datosEmpresa.add(new Phrase(suc.getFax() + "\n", fuenteNormal));

        celdaDatosEmpresa.addElement(datosEmpresa);

        //Datos Cliente
        PdfPCell tituloDatosCliente = new PdfPCell(new Phrase("Datos Cliente\n", fuenteTitulo_1));
        tituloDatosCliente.setBackgroundColor(BaseColor.LIGHT_GRAY);
        

        PdfPCell celdaDatosCliente = new PdfPCell();
        Paragraph datosCliente = new Paragraph();
        datosCliente.add(new Phrase("Nombre: ", fuenteNormalDestacado));
        datosCliente.add(new Phrase(cli.getName() + "\n", fuenteNormal));
        datosCliente.add(new Phrase("DNI: ", fuenteNormalDestacado));
        datosCliente.add(new Phrase(cli.getDni() + "\n", fuenteNormal));
        datosCliente.add(new Phrase("Direccion: ", fuenteNormalDestacado));
        datosCliente.add(new Phrase(cli.getAddress() + "\n", fuenteNormal));
        datosCliente.add(new Phrase("Teléfono: ", fuenteNormalDestacado));
        datosCliente.add(new Phrase(cli.getTelephone() + "\n", fuenteNormal));
        if (cli.getCompany() != null) {
            datosCliente.add(new Phrase("Empresa: ", fuenteNormalDestacado));
            datosCliente.add(new Phrase(cli.getCompany() + "\n", fuenteNormal));
        }

        celdaDatosCliente.addElement(datosCliente);

        //Codigo y Fecha
        PdfPCell celdaInfoExtra = new PdfPCell();
        Paragraph infoExtra = new Paragraph();
        infoExtra.add(new Phrase("Código Factura: ", fuenteNormalDestacado));
        infoExtra.add(new Phrase(factura.getCodFactura() + "\n", fuenteNormal));
        infoExtra.add(new Phrase("Fecha Emisión: ", fuenteNormalDestacado));
        infoExtra.add(new Phrase(Tools.printDate(factura.getFechaEmision()) + "\n", fuenteNormal));
        celdaInfoExtra.setColspan(4);
        celdaInfoExtra.setFixedHeight(50);
        celdaInfoExtra.addElement(infoExtra);

        //Añadir elementos de tabla
        float[] columnas = {(float) 25, (float) 5, (float) 52.5, (float) 52.5};
        PdfPTable tablaCabecera = this.generateTable(100, columnas, false);
        tablaCabecera.addCell(celdaLogo);
        PdfPCell hueco = new PdfPCell();
        hueco.setRowspan(2);
        
        this.eliminarBordeCelda(celdaLogo);
        this.eliminarBordeCelda(hueco);
        this.eliminarBordeCelda(tituloDatosEmpresa);
        this.eliminarBordeCelda(tituloDatosCliente);
        this.eliminarBordeCelda(celdaDatosEmpresa);
        this.eliminarBordeCelda(celdaDatosCliente);
        this.eliminarBordeCelda(celdaInfoExtra);

        tablaCabecera.addCell(hueco);
        tablaCabecera.addCell(tituloDatosEmpresa);
        tablaCabecera.addCell(tituloDatosCliente);
        tablaCabecera.addCell(celdaDatosEmpresa);
        tablaCabecera.addCell(celdaDatosCliente);
        tablaCabecera.addCell(celdaInfoExtra);
        
        doc.add(tablaCabecera);
    }

    private void doBillContent() throws DocumentException {
        //Crear Tabla
        float[] columnas = {70, 15, 15};
        PdfPTable tablaContenido = this.generateTable(100, columnas, true);

        //Contenido
        HashMap<String, Alquiler> alquileres = factura.getAlquileres();
        HashMap<String, Incidencia> incidencias = factura.getIncidencias();
        
        if (factura.getAlquileres() != null  && factura.getAlquileres().size() > 0) {
            //Titulo Alquileres
            PdfPCell tituloAlquileres = new PdfPCell(new Phrase("Alquileres", fuenteTitulo_2));
            tituloAlquileres.setColspan(3);
            tituloAlquileres.setBackgroundColor(BaseColor.LIGHT_GRAY);
            this.eliminarBordeCelda(tituloAlquileres);
            tablaContenido.addCell(tituloAlquileres);

            //Cabecera Alquileres
            PdfPCell celdaTitulo1 = new PdfPCell(new Phrase("Descripcion", fuenteTitulo_3));
            PdfPCell celdaTitulo2 = new PdfPCell(new Phrase("Tarifa", fuenteTitulo_3));
            PdfPCell celdaTitulo3 = new PdfPCell(new Phrase("Precio\n(sin IVA)", fuenteTitulo_3));
            tablaContenido.addCell(celdaTitulo1);
            tablaContenido.addCell(celdaTitulo2);
            tablaContenido.addCell(celdaTitulo3);

            //Bucle Alquileres
            for (Alquiler alq : alquileres.values()) {
                Paragraph descripcionAlquiler = new Paragraph();
                descripcionAlquiler.add(new Phrase("Marca: ", fuenteNormalDestacado));
                descripcionAlquiler.add(new Phrase(alq.getVehiculo().getMarca() + "     ", fuenteNormal));
                descripcionAlquiler.add(new Phrase("Modelo: ", fuenteNormalDestacado));
                descripcionAlquiler.add(new Phrase(alq.getVehiculo().getModelo() + "     ", fuenteNormal));
                descripcionAlquiler.add(new Phrase("Matrícula: ", fuenteNormalDestacado));
                descripcionAlquiler.add(new Phrase(alq.getVehiculo().getMatricula() + "\n", fuenteNormal));
                descripcionAlquiler.add(new Phrase("Fecha Salida: ", fuenteNormalDestacado));
                descripcionAlquiler.add(new Phrase(Tools.printDate(alq.getFechaInicio()) + "     ", fuenteNormal));
                descripcionAlquiler.add(new Phrase("Fecha Fin: ", fuenteNormalDestacado));
                descripcionAlquiler.add(new Phrase(Tools.printDate(alq.getFechaFin()) + "\n", fuenteNormal));
                descripcionAlquiler.add(new Phrase("Fecha Entrega Vehiculo", fuenteNormalDestacado));
                descripcionAlquiler.add(new Phrase(Tools.printDate(alq.getFechaEntrega()) + "\n ", fuenteNormal));
                Phrase tarifa = new Phrase(alq.getTarifa().getNombre());
                Phrase precio = new Phrase(Tools.printBigDecimal(alq.getPrecio()) + " €");
                tablaContenido.addCell(descripcionAlquiler);
                tablaContenido.addCell(tarifa);
                tablaContenido.addCell(precio);
            }
        }
        if (factura.getIncidencias() != null && factura.getIncidencias().size() > 0) {
            //Titulo Incidencias
            PdfPCell tituloIncidencias = new PdfPCell(new Phrase("Incidencias", fuenteTitulo_2));
            tituloIncidencias.setColspan(3);
            tituloIncidencias.setBackgroundColor(BaseColor.LIGHT_GRAY);
            this.eliminarBordeCelda(tituloIncidencias);
            tablaContenido.addCell(tituloIncidencias);

            //Cabecera Incidencias
            PdfPCell celdaTitulo1 = new PdfPCell(new Phrase("Descripcion", fuenteTitulo_3));
            celdaTitulo1.setColspan(2);
            PdfPCell celdaTitulo2 = new PdfPCell(new Phrase("Precio\n(sin IVA)", fuenteTitulo_3));
            tablaContenido.addCell(celdaTitulo1);
            tablaContenido.addCell(celdaTitulo2);


            //Bucle incidencias
            for (Incidencia inc : incidencias.values()) {
                PdfPCell celdaDescripcion = new PdfPCell();
                Paragraph descripcionIncidencia = new Paragraph();
                descripcionIncidencia.add(new Phrase("Incidencia: ", fuenteNormalDestacado));
                descripcionIncidencia.add(new Phrase(inc.getCodIncidencia() + "\n", fuenteNormal));
                descripcionIncidencia.add(new Phrase("TipoIncidencia: ", fuenteNormalDestacado));
                descripcionIncidencia.add(new Phrase(inc.getTipoIncidencia().getNombre() + "     ", fuenteNormal));
                descripcionIncidencia.add(new Phrase("Fecha Incidencia: ", fuenteNormalDestacado));
                descripcionIncidencia.add(new Phrase(Tools.printDate(inc.getFecha()) + "\n", fuenteNormal));
                descripcionIncidencia.add(new Phrase("Observaciones: ", fuenteNormalDestacado));
                descripcionIncidencia.add(new Phrase(inc.getObservaciones() + "\n ", fuenteNormal));
                celdaDescripcion.setColspan(2);
                celdaDescripcion.addElement(descripcionIncidencia);
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