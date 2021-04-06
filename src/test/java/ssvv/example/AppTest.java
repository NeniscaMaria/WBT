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
import ssvv.example.validation.ValidationException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.time.LocalDate;

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
        service.addStudent(new Student("id12", "John", 935, "john@gmail.com", "A"));
        service.addTema(new Tema("id121","some description",2,1));
    }

    @AfterClass
    public static void tearDown() {
        service = null;
        //empty the file after finish
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root  = document.createElement("inbox");
            document.appendChild(root);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult("src/test/java/ssvv/example/fisiere/Teme.xml"));
            transformer.transform(new DOMSource(document), new StreamResult("src/test/java/ssvv/example/fisiere/Note.xml"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private Tema getValidTema(){
        return new Tema("1","some description",1,2);
    }

    private Tema getValidTema2(){
        return new Tema("10","some description",1,2);
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

    @Test
    public void addAssignmentTestCase(){
        //add2AssignmentsWithSameID_ShouldReturnEntity
        try{
            Tema tema1 = getValidTema2();
            Tema tema2 = getValidTema2();
            Tema addedTema1 = service.addTema(tema1);
            Tema addedTema2 = service.addTema(tema2);
            assert(addedTema2.getID().equals(tema2.getID()));
            assert(addedTema1==null);
        }catch(Exception ex){
            assert(false);
        }

        //addAssignmentWithInvalidID_ShouldThrowException
        try{
            Tema tema = getTemaInvalidID();
            Tema addedTema = service.addTema(tema);            assert(false);
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

    @Test
    public void addStudentTestCase(){
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
    }

    private Nota getValidNota(){
        String[] date = "2018-10-06".split("-");
        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        return new Nota("id12#id121", "id12", "id121", 10.00, dataPredare);
    }

    private Nota getNotaWithInvalidStudent(){
        String[] date = "2018-10-06".split("-");
        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        return new Nota("787#id121", "787", "id121", 10.00, dataPredare);
    }

    private Nota getNotaWithInvalidAssignment(){
        String[] date = "2018-10-06".split("-");
        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        return new Nota("id12#787", "id12", "787", 10.00, dataPredare);
    }

    private Nota getNotaWithInvalidGrade(){
        String[] date = "2018-10-06".split("-");
        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        return new Nota("id12#787", "id12", "787", 12.00, dataPredare);
    }

    private Nota getNotaWithInvalidDelivery(){
        String[] date = "2018-08-06".split("-");
        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        return new Nota("id12#787", "id12", "787", 10.00, dataPredare);
    }

    private Nota getNotaWithDelay(){
        String[] date = "2018-10-20".split("-");
        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        return new Nota("id12#id121", "id12", "id121", 10.00, dataPredare);
    }

    private Nota getNotaLateDelivery(){
        String[] date = "2018-12-06".split("-");
        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        return new Nota("id12#id121", "id12", "id121", 10.00, dataPredare);
    }

    @Test
    public void addGradeTestCase(){
        //add valid grade + within deadline
        try{
            Nota nota = getValidNota();
            String feedback = "very good";
            double addedNota = service.addNota(nota, feedback);
            assert(nota.getNota() == addedNota);
        }
        catch(Exception e){
            assert(false);
        }

        //add with nonexisting student id
        try{
            Nota nota = getNotaWithInvalidStudent();
            String feedback = "very good";
            double addedNota = service.addNota(nota, feedback);
            assert(false);
        }
        catch(Exception e){
            assert(true);
        }


        //add with nonexisting assignment id
        try{
            Nota nota = getNotaWithInvalidAssignment();
            String feedback = "very good";
            double addedNota = service.addNota(nota, feedback);
            assert(false);
        }
        catch(Exception e){
            assert(true);
        }

        //add with invalid grade
        try{
            Nota nota = getNotaWithInvalidGrade();
            String feedback = "very good";
            double addedNota = service.addNota(nota, feedback);
            assert(false);
        }
        catch(Exception e){
            assert(true);
        }

        //add with invalid predare
        try{
            Nota nota = getNotaWithInvalidDelivery();
            String feedback = "very good";
            double addedNota = service.addNota(nota, feedback);
            assert(false);
        }
        catch(Exception e){
            assert(true);
        }

        //add with 1 week delay
        try{
            Nota nota = getNotaWithDelay();
            String feedback = "very good";
            double addedNota = service.addNota(nota, feedback);
            assert(nota.getNota() == addedNota);
        }
        catch(Exception e){
            assert(false);
        }

        //add with 3 week delay
        try{
            Nota nota = getNotaLateDelivery();
            String feedback = "very good";
            double addedNota = service.addNota(nota, feedback);
            assert(nota.getNota() == addedNota);
        }
        catch(Exception e){
            assert(false);
        }
    }

    @Test
    public void integrationAll(){
        addAssignmentTestCase();
        addStudentTestCase();
        addGradeTestCase();
    }

}

