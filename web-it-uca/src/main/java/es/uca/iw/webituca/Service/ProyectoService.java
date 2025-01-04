package es.uca.iw.webituca.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.Repository.ProyectoRepository;
import org.apache.commons.io.IOUtils;



import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProyectoService {
    
    @Autowired
    private ProyectoRepository proyectoRepository;

    public Proyecto guardarProyecto(Proyecto proyecto, MemoryBuffer memoryBuffer) {
        String filePath = guardarFile(memoryBuffer, "src/main/resources/static/uploads", proyecto.getUsuario().getNombre() + ".pdf");
        proyecto.setArchivoPath(filePath);
        return proyectoRepository.save(proyecto);
    }

   
    public static String guardarFile(MemoryBuffer buffer, String targetDirPath, String fileName) {
        String filePath = null;

        try (InputStream inputStream = buffer.getInputStream()) {

            File targetDir = new File(targetDirPath);
            if (!targetDir.exists()) {
                // Crear directorio si no existe
                targetDir.mkdirs();
            }
            // Define the target file
            File targetFile = new File(targetDir, fileName);

            // Cambiar Nombre para archivos repetidos
            if (targetFile.exists()) {
                String newFileName = fileName.substring(0, fileName.lastIndexOf('.'));
                newFileName += "_" + System.currentTimeMillis() + ".pdf";
                targetFile = new File(targetDir, newFileName);
            }

            // Write the uploaded file to the target directory
            try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
                IOUtils.copy(inputStream, outputStream);
                filePath = targetFile.getPath();
            } catch (IOException e) {
                System.err.println("Error al guardar el archivo: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }

        return filePath;
    }
    



    public long count() {
        return proyectoRepository.count();
    }

    // public List<Proyecto> listarProyectos() {
    //     return proyectoRepository.findAll();
    // }
    

    public List<Proyecto> listarProyectos() {
        return proyectoRepository.findAll();
}

}


