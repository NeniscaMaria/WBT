package ssvv.example;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

public class BigBangIntegration{
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

    private Student getStudentWithNullName() {
        return new Student("id2", null, 2, "john@gmail.com", "A");
    }

    @Test
    public void add2AssignmentsWithSameID_ShouldReturnEntity(){
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
    }

    @Test
    public void addAssignmentWithInvalidID_ShouldThrowException(){
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
    }

    @Test
    public void addAssignmentWithInvalidDescription_ShouldThrowException(){
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
    }

    @Test
    public void addAssignmentWithInvalidDeadline_ShouldThrowException(){
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
    }

    @Test
    public void addAssignmentWithInvalidPrimire_ShouldThrowException(){
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
    }

    @Test
    public void addValidAssignment_ShouldReturnEntity(){
        try{
            Tema tema = getValidTema();
            Tema addedTema = service.addTema(tema);
            assert(addedTema==null);
        }catch(Exception ex){
            assert(false);
        }

    }

    @Test
    public void addValidStudent_ShouldReturnTrue() {
        Student student = getValidStudent();
        try {
            Student addedStudent = service.addStudent(student);
            assert (addedStudent == student);
        } catch (Exception ex) {
            assert (false);
        }
    }

    @Test
    public void addStudentWithInvalidName_ShouldReturnFalse() {
        Student student = getStudentWithEmptyName();
        try {
            service.addStudent(student);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }
    }

    @Test
    public void addStudentWithNullName_ShouldReturnFalse() {
        Student student = getStudentWithNullName();
        try {
            service.addStudent(student);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }
    }

    @Test
    public void addStudentWithNoGroup_ShouldReturnFalse() {
        Student student = getStudentWithEmptyGroup();
        try {
            Student addedStudent = service.addStudent(student);
            System.out.println(addedStudent);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }
    }

    @Test
    public void addStudentWithInvalidGroup_ShouldReturnFalse() {
        Student student = getStudentWithInvalidGroup();
        try {
            service.addStudent(student);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }
    }

    @Test
    public void addStudentWithEmptyEmail_ShouldReturnFalse() {
        Student student = getStudentWithEmptyEmail();
        try {
            service.addStudent(student);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }
    }

    @Test
    public void addStudentWithNullEmail_ShouldReturnFalse() {
        Student student = getStudentWithNullEmail();
        try {
            service.addStudent(student);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }
    }

    //BVA for group
    @Test
    public void addStudentWithValidBV0_ShouldReturnFalse() {
        Student student = new Student("id2", "John", 0, "john@gmail.com", "A");
        try {
            service.addStudent(student);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }
    }

    @Test
    public void addStudentWithValidBV_1_ShouldReturnFalse() {
        Student student = new Student("id2", "John", -1, "john@gmail.com", "A");
        try {
            service.addStudent(student);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }
    }

    @Test
    public void addStudentWithValidBV_2_ShouldReturnFalse() {
        Student student = new Student("id2", "John", -2, "john@gmail.com", "A");
        try {
            service.addStudent(student);
            assert (false);
        } catch (Exception ex) {
            assert (true);
        }
    }

    @Test
    public void addStudentWithValidBV3_ShouldReturnTrue() {
        Student student = new Student("id2", "John", 3, "john@gmail.com", "A");
        try {
            Student addedStudent = service.addStudent(student);
            assert (student == addedStudent);
        } catch (Exception ex) {
            assert (false);
        }
    }

    @Test
    public void addStudentWithValidBV922_ShouldReturnTrue() {
        Student student = new Student("id2", "John", 922, "john@gmail.com", "A");
        try {
            Student addedStudent = service.addStudent(student);
            assert (student == addedStudent);
        } catch (Exception ex) {
            assert (false);
        }
    }

    @Test
    public void addStudentWithValidBV500_ShouldReturnTrue() {
        Student student = new Student("id2", "John", 500, "john@gmail.com", "A");
        try {
            Student addedStudent = service.addStudent(student);
            assert (addedStudent == student);
        } catch (Exception ex) {
            assert (false);
        }
    }


    @Test
    public void addStudentWithValidTeacherName_ShouldReturnTrue()
    {
        Student student = getValidStudent();
        try{
            Student addedStudent = service.addStudent(student);
            assert(addedStudent == student);
        }catch (Exception ex){
            assert(false);
        }
    }

    @Test
    public void addStudentWithInvalidTeacherName_ShouldReturnFalse()
    {
        Student student = getStudentWithInvalidTeacherName();
        try{
            service.addStudent(student);
            assert(false);
        }catch (Exception ex){
            assert(true);
        }
    }

    @Test
    public void addStudentWithValidId_ShouldReturnTrue()
    {
        Student student = getValidStudent();
        try{
            Student addedStudent = service.addStudent(student);

            assert(addedStudent == student);
        }catch (Exception ex){
            assert(false);
        }
    }

    @Test
    public void addStudentWithInvalidId_ShouldReturnFalse()
    {
        Student student = getStudentWithInvalidId();
        try{
            service.addStudent(student);
            assert(false);
        }catch (Exception ex){
            assert(true);
        }
    }

}

