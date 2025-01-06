package es.uca.iw.webituca.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.List;
import java.util.Optional;

import es.uca.iw.webituca.Model.Estado;
import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Repository.ProyectoRepository;

@Service
public class ProyectoService {
    
    @Autowired
    private ProyectoRepository proyectoRepository;

    public Proyecto guardarProyecto(Proyecto proyecto, MemoryBuffer memoryBuffer) {
        if(memoryBuffer != null && memoryBuffer.getInputStream() != null) {
            String filePath = guardarFile(memoryBuffer, "src/main/resources/static/uploads", 
                proyecto.getUsuario().getNombre() + ".pdf");
            proyecto.setArchivoPath(filePath);
        } else {
            System.err.println("MemoryBuffer está vacío. No se guardará ningún archivo.");
        }
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
            // Define el target file
            File targetFile = new File(targetDir, fileName);

            // Cambiar nombre para archivos repetidos
            if (targetFile.exists()) {
                String newFileName = fileName.substring(0, fileName.lastIndexOf('.'));
                newFileName += "_" + System.currentTimeMillis() + ".pdf";
                targetFile = new File(targetDir, newFileName);
            }

            // Guarda el archivo subido en el directorio target
            try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
                IOUtils.copy(inputStream, outputStream);
                filePath = targetFile.getPath();
                System.out.println("Archivo guardado en: " + filePath);
            } catch (IOException e) {
                System.err.println("Error al guardar el archivo: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }

        return filePath;
    }

    public Proyecto cambiarEstadoProyecto(Long idProyecto, Estado nuevoEstado) {
        Optional<Proyecto> proyectoOptional = buscarProyectoPorId(idProyecto);
        if (proyectoOptional.isPresent()) {
            Proyecto proyecto = proyectoOptional.get();
            proyecto.setEstado(nuevoEstado);
            return proyectoRepository.save(proyecto);
        }
        throw new IllegalArgumentException("El proyecto con ID " + idProyecto + " no se encontró.");
    }

    public List<Proyecto> listarProyectos() {
        return proyectoRepository.findAll();
    }

    public Optional<Proyecto> buscarProyectoPorId(Long idProyecto) {
        return proyectoRepository.findById(idProyecto);
    }

    public long count() {
        return proyectoRepository.count();
    }
}
