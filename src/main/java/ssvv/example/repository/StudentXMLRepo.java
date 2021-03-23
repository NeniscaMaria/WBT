package ssvv.example.repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ssvv.example.domain.Student;

public class StudentXMLRepo extends AbstractXMLRepository<String, Student> {
    /**
     * Class constructor
     * @param filename - numele fisierului
     */
    public StudentXMLRepo(String filename) {
        super(filename);
    }

    /**
     * Extrage informatia despre student dintr-un element XML
     * @param element - XML-ul din care ia datele studentului
     * @return studentul
     */

    @Override
    public Student extractEntity(Element element) {
        String studentId = element.getAttribute("idStudent");
        String nume =element.getElementsByTagName("nume")
                .item(0)
                .getTextContent();
        String grupa =element.getElementsByTagName("grupa")
                .item(0)
                .getTextContent();
        String email =element.getElementsByTagName("email")
                .item(0)
                .getTextContent();
        String profesor = element.getElementsByTagName("profesor")
                .item(0)
                .getTextContent();

        return new Student(studentId, nume, Integer.parseInt(grupa), email, profesor);
    }

    @Override
    public Element createElementfromEntity(Document document, Student entity) {
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


}
