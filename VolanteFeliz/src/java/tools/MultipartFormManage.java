package tools;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class MultipartFormManage {
    public static String getcontentPartText(Part input) {
        Scanner sc = null;
        String content = null;
        try {
            sc = new Scanner(input.getInputStream(), "UTF-8");
            if (sc.hasNext()) {
                content = sc.nextLine();
            } else {
                content = "";
            }
            sc.close();
            return content;
        } catch (IOException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, ex.getMessage());
        }finally{
            sc.close();
        }
        return content;
    }
    
    public static boolean recuperarYGuardarImagenFormulario (HttpServletRequest request, HttpServletResponse response, String codigo) throws IOException, ServletException{
        if (request.getPart("foto").getContentType().contains("image") == false || request.getPart("foto").getSize() > 8388608) {
            request.setAttribute("resultados", "Archivo no válido");
            Tools.anadirMensaje(request, "Solo se admiten archivos de tipo imagen", 'w');
            Tools.anadirMensaje(request, "El tamaño máximo de archivo son 8 Mb", 'w');
            request.getRequestDispatcher("/admin/administration/products_administration.jsp").forward(request, response);
            return false;
        } else {
            String fileName = request.getServletContext().getRealPath("/images/products/" + codigo);
            boolean ok = MultipartFormManage.guardarImagenDeProdructoEnElSistemaDeFicheros(request.getPart("foto").getInputStream(), fileName);
            if (ok == false) {
                request.setAttribute("resultados", "Fallo al guardar archivo");
                Tools.anadirMensaje(request, "Ocurrio un error guardando la imagen", 'e');
                request.getRequestDispatcher("/admin/administration/products_administration.jsp").forward(request, response);
                return false;
            }
        }
        return true;
    }
    
    public static boolean guardarImagenDeProdructoEnElSistemaDeFicheros(InputStream input, String fileName)
            throws ServletException {
        FileOutputStream output = null;
        boolean ok = false;
        try {
            output = new FileOutputStream(fileName);
            int leido = 0;
            leido = input.read();
            while (leido != -1) {
                output.write(leido);
                leido = input.read();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, ex.getMessage());
        } finally {
            try {
                output.flush();
                output.close();
                input.close();
                ok = true;
            } catch (IOException ex) {
                Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, "Error cerrando flujo de salida", ex);
            }
        }
        return ok;
    }
}
