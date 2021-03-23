package ssvv.example;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ssvv.example.domain.Tema;
import ssvv.example.repository.NotaXMLRepo;
import ssvv.example.repository.StudentXMLRepo;
import ssvv.example.repository.TemaXMLRepo;
import ssvv.example.service.Service;
import ssvv.example.validation.NotaValidator;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.TemaValidator;

/**
 * Unit test for simple App.
 */
public class AppTest {
    public static Service service;

    @BeforeClass
    public static void setup() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "src/test/java/ssvv/example/fisiere/Studenti.xml";
        String filenameTema = "src/test/java/ssvv/example/fisiere/Teme.xml";
        String filenameNota = "src/test/java/ssvv/example/fisiere/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);

    }

    @AfterClass
    public static void tearDown() {
        service = null;
    }

    private Tema getValidTema(){
        return new Tema("1","some description",1,2);
    }

    @Test
    public void addValidAssignment_ShouldReturnEntity(){
        try{
            Tema tema = getValidTema();
            Tema addedTema = service.addTema(tema);
            assert(addedTema.getID().equals(tema.getID()));
        }catch(Exception ex){
            assert(false);
        }

    }

    @Test
    public void add2AssignmentsWithSameID_ShouldReturnNull(){
        try{
            Tema tema1 = getValidTema();
            Tema tema2 = getValidTema();
            Tema addedTema1 = service.addTema(tema1);
            Tema addedTema2 = service.addTema(tema2);
            assert(addedTema1.getID().equals(tema1.getID()));
            assert(addedTema2==null);
        }catch(Exception ex){
            assert(true);
        }
    }

}

