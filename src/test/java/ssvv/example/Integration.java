package ssvv.example;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ssvv.example.domain.Nota;
import ssvv.example.domain.Student;
import ssvv.example.domain.Tema;
import ssvv.example.repository.NotaXMLRepo;
import ssvv.example.repository.StudentXMLRepo;
import ssvv.example.repository.TemaXMLRepo;
import ssvv.example.service.Service;
import ssvv.example.validation.NotaValidator;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.TemaValidator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.time.LocalDate;

public class Integration {
    public static Service service;
    public static StudentValidator studentValidator;
    public static TemaValidator temaValidator;
    public static NotaValidator notaValidator;
    public static String studentFileName = "src/test/java/ssvv/example/fisiere/Studenti.xml";
    public static String temaFileName = "src/test/java/ssvv/example/fisiere/Teme.xml";
    public static String notaFileName = "src/test/java/ssvv/example/fisiere/Note.xml";

    @BeforeClass
    public static void setup() {
        studentValidator = new StudentValidator();
        temaValidator = new TemaValidator();

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(studentFileName);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(temaFileName);
        notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(notaFileName);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @AfterClass
    public static void tearDown() {
        service = null;
        //empty the file after finish
//        try {
//            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
//            Element root  = document.createElement("inbox");
//            document.appendChild(root);
//            Transformer transformer = TransformerFactory.newInstance().newTransformer();
//            transformer.transform(new DOMSource(document), new StreamResult("src/test/java/ssvv/example/fisiere/Studenti.xml"));
//            transformer.transform(new DOMSource(document), new StreamResult("src/test/java/ssvv/example/fisiere/Teme.xml"));
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }

    private Student getValidStudent() {
        return new Student("7", "John", 935, "john@gmail.com", "A");
    }

    private Tema getValidTema(){
        return new Tema("7","some description",2,1);
    }

    private Nota getValidNota(Student student, Tema tema){
        LocalDate dataPredare = LocalDate.of(2018, 10, 10);
        return new Nota(student.getID() + "#" + tema.getID(), student.getID(), tema.getID(), 10.00, dataPredare);
    }

    @Test
    public void addStudentTopDown(){
        Student student = getValidStudent();
        try{
            studentValidator.validate(student);
            Student saved = service.addStudent(student);
            assert(student == saved);
        }
        catch (Exception e){
            assert false;
        }
    }

    @Test
    public void addStudentAssignmentTopDown(){
        Student student = getValidStudent();
        Tema tema = getValidTema();
        try{
            studentValidator.validate(student);
            Student savedStudent = service.addStudent(student);
            assert(student == savedStudent);
            temaValidator.validate(tema);
            Tema savedTema = service.addTema(tema);
            assert(tema == savedTema);
        }
        catch (Exception e){
            assert false;
        }
    }

    @Test
    public void addStudentAssignmentGradeTopDown(){
        Student student = getValidStudent();
        Tema tema = getValidTema();
        Nota nota = getValidNota(student, tema);
        try{
            studentValidator.validate(student);
            Student savedStudent = service.addStudent(student);
            assert(student == savedStudent);
            temaValidator.validate(tema);
            Tema savedTema = service.addTema(tema);
            assert(tema == savedTema);
            notaValidator.validate(nota);
            double savedNota = service.addNota(nota, "good");
            assert(nota.getNota() == savedNota);
        }
        catch (Exception e){
            assert false;
        }
    }



}
