package IntegracionBackFront.backfront.Services.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {
    //1. Constante para definir el tamaño maximo permitido para los archivos (5MB)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    //2. Extenciones de archivos permitidas
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".png", ".jpeg", ".webp"};

    //3. Cliente de cloudinary inyectado como dependencia
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    //Subir imagenes a la raiz de Cloudinary
    public String uploadImage(MultipartFile file) throws IOException {
        validateImage(file);
        Map<?, ?> uploadResult = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.asMap(
                        "resource_type", "auto",
                        "quality", "auto:good"
                ));
        return (String) uploadResult.get("secure_url");
    }

    public String uploadImage(MultipartFile file, String folder) throws IOException{
        validateImage(file);
        String originalFileName = file.getOriginalFilename();
        String fileExtensions = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
        String uniqueFileName = "img" + UUID.randomUUID() + fileExtensions;

        Map<String, Object> options = ObjectUtils.asMap(
                "folder", folder,   //Carpeta de destino
                "public_id", uniqueFileName,//NOmbre unico para el archivo
                "use_filename", false,      //No se puede usar el nombre original
                "unique_filename", false,   //No genera nombre unico (ya lo hicimos)
                "overwrite", false,          //No sobreescribir archivos existentes
                "reosurce_type", "auto",
                "quality", "auto:good"
        );
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        return (String) uploadResult.get("secure_url");

    }

    private void validateImage(MultipartFile file) {
        //Verificar si el archivo esta vacio
        if(file.isEmpty()) throw new IllegalArgumentException("El archivo no puede estar vacio");
        if(file.getSize() > MAX_FILE_SIZE) throw new IllegalArgumentException("Como excediste 5TB con una imagen?"); //Verificar si el tamaño del archivo excede el limite permitido
        String originalFileName = file.getOriginalFilename();
        if(originalFileName == null) throw new IllegalArgumentException("Nombre de archivo no valido");
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        if(!Arrays.asList(ALLOWED_EXTENSIONS).contains(extension)) throw new IllegalArgumentException("Solo se permiten archivos jpg, png, jpeg y webp");
        if(!file.getContentType().startsWith("image/")) throw new IllegalArgumentException("El archivo debe ser una imagen valida");
    }

    //Subir imagenes a una carpeta de Cloudinary
}
