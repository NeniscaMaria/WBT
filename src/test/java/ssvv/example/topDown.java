package ssvv.example;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ssvv.example.domain.Student;
import ssvv.example.domain.Tema;
import ssvv.example.repository.NotaXMLRepo;
import ssvv.example.repository.StudentXMLRepo;
import ssvv.example.repository.TemaXMLRepo;
import ssvv.example.service.Service;
import ssvv.example.validation.NotaValidator;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.TemaValidator;
import ssvv.example.validation.ValidationException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class topDown {
    public static Service service;
    public static String studentFileName;

    private static void setup() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "src/test/java/ssvv/example/fisiere/Studenti.xml";
        studentFileName = filenameStudent;
        String filenameTema = "src/test/java/ssvv/example/fisiere/Teme.xml";
        String filenameNota = "src/test/java/ssvv/example/fisiere/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    private static void tearDown() {
        service = null;
        //empty the file after finish
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root  = document.createElement("inbox");
            document.appendChild(root);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult("src/test/java/ssvv/example/fisiere/Teme.xml"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private Tema getValidTema(){
        return new Tema("1","some description",1,2);
    }

    private Tema getValidTema2(){
        return new Tema("1000000","some description",1,2);
    }

    private Tema getTemaInvalidID(){
        return new Tema("","some description",1,2);
    }

    private Tema getTemaInvalidDescription(){
        return new Tema("3","",1,2);
    }

    private Tema getTemaInvalidDeadline(){
        return new Tema("4","some description",22,2);
    }

    private Tema getTemaInvalidPrimite(){
        return new Tema("5","some description",1,22);
    }

    private Student getValidStudent() {
        return new Student("id1", "John", 935, "john@gmail.com", "A");
    }

    private Student getStudentWithEmptyName() {
        return new Student("id2", "", 935, "john@gmail.com", "A");
    }
    private Student getStudentWithInvalidTeacherName(){
        return new Student("id8","Jack",925,"john@gmail.com", null);
    }
    private Student getStudentWithInvalidId(){
        return new Student("","Jim",925,"john@gmail.com", "A");
    }

    private Student getStudentWithEmptyGroup() {
        return new Student("id2", "John", 0, "john@gmail.com", "A");
    }

    private Student getStudentWithInvalidGroup() {
        return new Student("id2", "John", -2, "john@gmail.com", "A");
    }

    private Student getStudentWithEmptyEmail() {
        return new Student("id2", "John", 2, "", "A");
    }

    private Student getStudentWithNullEmail() {
        return new Student("id2", "John", 2, null, "A");
    }

    private Element createElementfromStudent(Document document, Student entity) {
        Element e = document.createElement("student");
        e.setAttribute("idStudent", entity.getID());

        Element nume = document.createElement("nume");
        nume.setTextContent(entity.getNume());
        e.appendChild(nume);

        Element grupa = document.createElement("grupa");
        int nrGrupa = entity.getGrupa();
        grupa.setTextContent(String.valueOf(nrGrupa));
        e.appendChild(grupa);

        Element email = document.createElement("email");
        email.setTextContent(entity.getEmail());
        e.appendChild(email);

        Element profesor = document.createElement("profesor");
        profesor.setTextContent(entity.getTeacher());
        e.appendChild(profesor);

        return e;
    }

    private Student stubAddStudent(Student student){
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument();
            Element root  = document.createElement("inbox");
            document.appendChild(root);
            service.getAllStudenti().forEach(e->{
                Element elem = createElementfromStudent(document,e);
                root.appendChild(elem);
            });

            //write Document to file
            Transformer transformer = TransformerFactory.
                    newInstance().newTransformer();
            transformer.transform(new DOMSource(document),
                    new StreamResult(studentFileName));

        }catch(Exception e) {
            e.printStackTrace();
        }
        return student;
    }

    @Test
    public void addStudentTestCase(){
        setup();
        //addValidStudent_ShouldReturnTrue
        Student student = getValidStudent();
        try {
            Student addedStudent = service.addStudent(student);
            assert (addedStudent == student);
        } catch (Exception ex) {
            assert (false);
        }

        //addStudentWithInvalidName_ShouldReturnFalse
        student = getStudentWithEmptyName();
        try {
            service.addStudent(student);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }

        //addStudentWithNullName_ShouldReturnFalse
        student = getStudentWithEmptyGroup();
        try {
            Student addedStudent = service.addStudent(student);
            System.out.println(addedStudent);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }

        //addStudentWithInvalidGroup_ShouldReturnFalse
        student = getStudentWithInvalidGroup();
        try {
            service.addStudent(student);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }

        //addStudentWithEmptyEmail_ShouldReturnFalse
        student = getStudentWithEmptyEmail();
        try {
            service.addStudent(student);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }

        //addStudentWithNullEmail_ShouldReturnFalse
        student = getStudentWithNullEmail();
        try {
            service.addStudent(student);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }

        //addStudentWithValidBV0_ShouldReturnFalse
        student = new Student("id2", "John", 0, "john@gmail.com", "A");
        try {
            service.addStudent(student);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }

        //addStudentWithValidBV_1_ShouldReturnFalse
        student = new Student("id2", "John", -1, "john@gmail.com", "A");
        try {
            service.addStudent(student);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }

        //addStudentWithValidBV_2_ShouldReturnFalse
        student = new Student("id2", "John", -2, "john@gmail.com", "A");
        try {
            service.addStudent(student);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }

        //addStudentWithValidBV3_ShouldReturnTrue
        student = new Student("id2", "John", 3, "john@gmail.com", "A");
        try {
            Student addedStudent = service.addStudent(student);
            assert (student == addedStudent);
        } catch (Exception ex) {
            assert (false);
        }

        //addStudentWithValidBV922_ShouldReturnTrue
        student = new Student("id2", "John", 922, "john@gmail.com", "A");
        try {
            Student addedStudent = service.addStudent(student);
            assert (student == addedStudent);
        } catch (Exception ex) {
            assert (false);
        }

        //addStudentWithValidBV500_ShouldReturnTrue
        student = new Student("id2", "John", 500, "john@gmail.com", "A");
        try {
            Student addedStudent = service.addStudent(student);
            assert (addedStudent == student);
        } catch (Exception ex) {
            assert (false);
        }

        //addStudentWithValidTeacherName_ShouldReturnTrue
        student = getValidStudent();
        try{
            Student addedStudent = service.addStudent(student);
            assert(addedStudent == student);
        }catch (Exception ex){
            assert(false);
        }

        //addStudentWithInvalidTeacherName_ShouldReturnFalse
        student = getStudentWithInvalidTeacherName();
        try{
            service.addStudent(student);
            assert(false);
        }catch (Exception ex){
            assert(true);
        }

        //addStudentWithValidId_ShouldReturnTrue
        student = getValidStudent();
        try{
            Student addedStudent = service.addStudent(student);

            assert(addedStudent == student);
        }catch (Exception ex){
            assert(false);
        }

        //addStudentWithInvalidId_ShouldReturnFalse
        student = getStudentWithInvalidId();
        try{
            service.addStudent(student);
            assert(false);
        }catch (Exception ex){
            assert(true);
        }
        tearDown();
    }

    @Test
    public void addAssignmentTestCase(){
        setup();
        //addAssignmentWithInvalidID_ShouldThrowException
        try{
            Tema tema = getTemaInvalidID();
            Tema addedTema = service.addTema(tema);
            assert(false);
        }catch(ValidationException ex){
            assert(ex.getMessage().equals("Numar tema invalid!"));
        }
        catch(Exception e){
            assert(false);
        }

        //addAssignmentWithInvalidDescription_ShouldThrowException
        try{
            Tema tema = getTemaInvalidDescription();
            Tema addedTema = service.addTema(tema);
            assert(false);
        }catch(ValidationException ex){
            assert(ex.getMessage().equals("Descriere invalida!"));
        }
        catch(Exception e){
            assert(false);
        }

        //addAssignmentWithInvalidDeadline_ShouldThrowException
        try{
            Tema tema = getTemaInvalidDeadline();
            Tema addedTema = service.addTema(tema);
            assert(false);
        }catch(ValidationException ex){
            assert(ex.getMessage().equals("Deadlineul trebuie sa fie intre 1-14."));
        }
        catch(Exception e){
            assert(false);
        }

        //addAssignmentWithInvalidPrimire_ShouldThrowException
        try{
            Tema tema = getTemaInvalidPrimite();
            Tema addedTema = service.addTema(tema);
            assert(false);
        }catch(ValidationException ex){
            assert(ex.getMessage().equals("Saptamana primirii trebuie sa fie intre 1-14."));
        }
        catch(Exception e){
            assert(false);
        }

        //addValidAssignment_ShouldReturnEntity
        try{
            Tema tema = getValidTema();
            Tema addedTema = service.addTema(tema);
            assert(addedTema==null);
        }catch(Exception ex){
            assert(false);
        }
    }

}
