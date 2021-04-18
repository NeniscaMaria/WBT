package ssvv.example;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
import java.sql.SQLOutput;
import java.time.LocalDate;

public class topDown {
    public static Service service;
    public static String studentFileName;
    public static String temaFileName;
    public static String notaFileName;

    private static void setup() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        studentFileName = "src/test/java/ssvv/example/fisiere/Studenti.xml";
        temaFileName = "src/test/java/ssvv/example/fisiere/Teme.xml";
        notaFileName = "src/test/java/ssvv/example/fisiere/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(studentFileName);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(temaFileName);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(notaFileName);
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
//            transformer.transform(new DOMSource(document), new StreamResult("src/test/java/ssvv/example/fisiere/Studenti.xml"));
//            transformer.transform(new DOMSource(document), new StreamResult("src/test/java/ssvv/example/fisiere/Teme.xml"));

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private Tema getValidTema(){
        return new Tema("1","some description",2,1);
    }

    private Tema getValidTema2(){
        return new Tema("1000000","some description",2,1);
    }

    private Tema getTemaInvalidID(){
        return new Tema("","some description",2,1);
    }

    private Tema getTemaInvalidDescription(){
        return new Tema("3","",2,1);
    }

    private Tema getTemaInvalidDeadline(){
        return new Tema("4","some description",1,2);
    }

    private Tema getTemaInvalidPrimite(){
        return new Tema("5","some description",1,22);
    }

    private Student getValidStudent() {
        return new Student("id2", "John", 935, "john@gmail.com", "A");
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
            Element root = document.createElement("inbox");
            document.appendChild(root);

            Element elem = createElementfromStudent(document, student);
            root.appendChild(elem);

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



    private Element createElementfromTema(Document document, Tema entity) {
        Element e = document.createElement("nrTema");
        e.setAttribute("nrTema", entity.getID());

        Element descriere = document.createElement("descriere");
        descriere.setTextContent(entity.getDescriere());
        e.appendChild(descriere);

        Element deadline = document.createElement("deadline");
        int dataDeadline = entity.getDeadline();
        deadline.setTextContent(String.valueOf(dataDeadline));
        e.appendChild(deadline);

        Element primire = document.createElement("primire");
        int dataPrimire = entity.getPrimire();
        primire.setTextContent(String.valueOf(dataPrimire));
        e.appendChild(primire);

        return e;
    }


    private Tema stubAddAssignment(Tema tema){
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument();
            Element root = document.createElement("inbox");
            document.appendChild(root);

            Element elem = createElementfromTema(document, tema);
            root.appendChild(elem);

            //write Document to file
            Transformer transformer = TransformerFactory.
                    newInstance().newTransformer();
            transformer.transform(new DOMSource(document),
                    new StreamResult(temaFileName));

        }catch(Exception e) {
            e.printStackTrace();
        }
        return tema;
    }

    @Test
    public void cmon(){
        setup();
        Student student = getValidStudent();
        stubAddStudent(student);
        stubAddAssignment(getValidTema());
        tearDown();
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


    private Nota getValidNota(Student student, Tema tema){
        String[] date = "2018-10-16".split("-");
        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        String id = student.getID() + "#" + tema.getID();
        return new Nota(id, student.getID(), tema.getID(), 10.00, dataPredare);
    }

    private Nota getNotaWithInvalidStudent(Student student, Tema tema){
        String[] date = "2018-10-06".split("-");
        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        String id = student.getID() + "#" + tema.getID();
        return new Nota(id, student.getID() + "none", tema.getID(), 10.00, dataPredare);
    }

    private Nota getNotaWithInvalidAssignment(Student student, Tema tema){
        String[] date = "2018-10-06".split("-");
        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        String id = student.getID() + "#" + tema.getID();

        return new Nota(id, student.getID() + "none", tema.getID() + "none", 10.00, dataPredare);
    }

    private Nota getNotaWithInvalidGrade(Student student, Tema tema){
        String[] date = "2018-10-06".split("-");
        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        String id = student.getID() + "#" + tema.getID();
        return new Nota(id, student.getID(), tema.getID(), 12.00, dataPredare);
    }

    private Nota getNotaWithInvalidDelivery(Student student, Tema tema){
        String[] date = "2018-08-06".split("-");
        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        String id = student.getID() + "#" + tema.getID();
        return new Nota(id, student.getID(), tema.getID(), 10.00, dataPredare);
    }

    private Nota getNotaWithDelay(Student student, Tema tema){
        String[] date = "2018-10-20".split("-");
        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        String id = student.getID() + "#" + tema.getID();

        return new Nota(id, student.getID(), tema.getID(), 10.00, dataPredare);
    }

    private Nota getNotaLateDelivery(Student student, Tema tema){
        String[] date = "2018-12-06".split("-");
        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        String id = student.getID() + "#" + tema.getID();

        return new Nota(id, student.getID(), tema.getID(), 10.00, dataPredare);
    }



    @Test
    public void addGradeTestCase(){
        Student student = getValidStudent();
        Tema tema = getValidTema();
        setup();
        //add valid grade + within deadline
        try{
            Nota nota = getValidNota(stubAddStudent(student), stubAddAssignment(tema));
            String feedback = "very good";
            double addedNota = service.addNota(nota, feedback);
            assert(nota.getNota() == addedNota);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            assert(false);
        }
        tearDown();

        setup();
        //add with nonexisting student id
        try{
            Nota nota = getNotaWithInvalidStudent(stubAddStudent(student), stubAddAssignment(tema));
            String feedback = "very good";
            double addedNota = service.addNota(nota, feedback);
            assert(false);
        }
        catch(Exception e){
            assert(true);
        }
        tearDown();

        setup();
        //add with nonexisting assignment id
        try{
            Nota nota = getNotaWithInvalidAssignment(stubAddStudent(student), stubAddAssignment(tema));
            String feedback = "very good";
            double addedNota = service.addNota(nota, feedback);
            assert(false);
        }
        catch(Exception e){
            assert(true);
        }
        tearDown();

        setup();
        //add with invalid grade
        try{
            Nota nota = getNotaWithInvalidGrade(stubAddStudent(student), stubAddAssignment(tema));
            String feedback = "very good";
            double addedNota = service.addNota(nota, feedback);
            assert(false);
        }
        catch(Exception e){
            assert(true);
        }
        tearDown();

        setup();
        //add with invalid predare
        try{
            Nota nota = getNotaWithInvalidDelivery(stubAddStudent(student), stubAddAssignment(tema));
            String feedback = "very good";
            double addedNota = service.addNota(nota, feedback);
            assert(false);
        }
        catch(Exception e){
            assert(true);
        }
        tearDown();

        setup();
        //add with 1 week delay
        try{
            Nota nota = getNotaWithDelay(stubAddStudent(student), stubAddAssignment(tema));
            String feedback = "very good";
            double addedNota = service.addNota(nota, feedback);
            assert(nota.getNota() == addedNota);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            assert(false);
        }
        tearDown();

        setup();
        //add with 3 week delay
        try{
            Nota nota = getNotaLateDelivery(stubAddStudent(student), stubAddAssignment(tema));
            String feedback = "very good";
            double addedNota = service.addNota(nota, feedback);
            assert(nota.getNota() == addedNota);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            assert(false);
        }
        tearDown();
    }



}
