package ssvv.example;

import ssvv.example.domain.Nota;
import ssvv.example.repository.*;
import ssvv.example.service.NotaService;
import ssvv.example.service.Service;
import ssvv.example.service.StudentService;
import ssvv.example.service.TemaService;
import ssvv.example.validation.NotaValidator;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.TemaValidator;
import ssvv.example.view.UI;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();

        // xml
        String filenameStudent = "src/main/java/ssvv/example/fisiere/Studenti.xml";
        String filenameTema = "src/main/java/ssvv/example/fisiere/Teme.xml";
        String filenameNota = "src/main/java/ssvv/example/fisiere/Note.xml";

        // txt
//        String filenameStudent = "src/main/java/ssvv/example/fisiere/Studenti.txt";
//        String filenameTema = "src/main/java/ssvv/example/fisiere/Teme.txt";
//        String filenameNota = "src/main/java/ssvv/example/fisiere/Note.txt";


//        StudentFileRepository studentFileRepository = new StudentFileRepository(filenameStudent);
//        TemaFileRepository temaFileRepository = new TemaFileRepository(filenameTema);
//        NotaValidator notaValidator = new NotaValidator(studentFileRepository, temaFileRepository);
//        NotaFileRepository notaFileRepository = new NotaFileRepository(filenameNota);
//        Service service = new Service(studentFileRepository, studentValidator, temaFileRepository, temaValidator, notaFileRepository, notaValidator);

        // xml repo
        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);

        StudentService studentService = new StudentService(studentXMLRepository, studentValidator);
        TemaService temaService = new TemaService(temaXMLRepository, temaValidator);
        NotaService notaService = new NotaService(notaXMLRepository, notaValidator, studentXMLRepository, temaXMLRepository);

//        Service service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
        UI ui = new UI(studentService, temaService, notaService);
        ui.run();
    }
}
