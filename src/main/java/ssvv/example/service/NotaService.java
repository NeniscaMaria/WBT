package ssvv.example.service;

import ssvv.example.curent.Curent;
import ssvv.example.domain.Nota;
import ssvv.example.domain.Student;
import ssvv.example.domain.Tema;
import ssvv.example.repository.NotaXMLRepo;
import ssvv.example.repository.StudentXMLRepo;
import ssvv.example.repository.TemaXMLRepo;
import ssvv.example.validation.NotaValidator;
import ssvv.example.validation.ValidationException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class NotaService implements ServiceInterface<String, Nota> {
    private NotaXMLRepo repository;
    private NotaValidator validator;
    private StudentXMLRepo studentRepository;
    private TemaXMLRepo temaRepository;

    public NotaService(NotaXMLRepo repository, NotaValidator validator, StudentXMLRepo studentRepository, TemaXMLRepo temaRepository) {
        this.repository = repository;
        this.validator = validator;
        this.studentRepository = studentRepository;
        this.temaRepository = temaRepository;
    }

    @Override
    public Nota findOne(String id) {
        checkId(id);
        return repository.findOne(id);

    }

    @Override
    public Iterable<Nota> findAll() {
        return repository.findAll();
    }

    @Override
    public Nota add(Nota entity) throws ValidationException {
        Tema tema = temaRepository.findOne(entity.getIdTema());

        int primire = tema.getPrimire();
        int deadline = tema.getDeadline();
        int predare = calculeazaSaptamanaPredare(entity.getData());
        int delay = predare - deadline;
        double penalizare;

        if (predare < primire){
            throw new ValidationException("Studentul nu poate preda aceasta tema!");
        }
        else if (predare <= deadline){
            penalizare = 0;
        }
        else if (delay <= 2){
            penalizare = delay * 2.5;
        }
        else {
            penalizare = entity.getNota() - 1;
        }

        entity.setNota(entity.getNota() - penalizare);
        System.out.println("Tema predata dupa " + delay + " saptamani este " + entity.getNota());
        repository.save(entity);
        return entity;
    }

    public double addFeedback(Nota entity, String feedback) throws ValidationException {
        Student student = studentRepository.findOne(entity.getIdStudent());
        Tema tema = temaRepository.findOne(entity.getIdTema());
        int predare = calculeazaSaptamanaPredare(entity.getData());


        String filename = "src/main/java/ssvv/example/fisiere/" + student.getNume() + ".txt";
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename, true))){
            bufferedWriter.write("\nTema: " + tema.getID());
            bufferedWriter.write("\nNota: " + entity.getNota());
            bufferedWriter.write("\nPredata in saptamana: " + predare);
            bufferedWriter.write("\nDeadline: " + tema.getDeadline());
            bufferedWriter.write("\nFeedback: " +feedback);
            bufferedWriter.newLine();
        } catch (IOException exception){
            throw new ValidationException(exception.getMessage());
        }
        return entity.getNota();
    }

    @Override
    public Nota delete(String id) {
        checkId(id);
        return repository.delete(id);
    }


    @Override
    public Nota update(Nota entity) {
        validator.validate(entity);
        return repository.update(entity);
    }

    @Override
    public void checkId(String id) {
        if(id == null || id.equals("")) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
    }

    /**
     * Calculeaza saptamana de predare
     * @param predare - data predarii unei teme
     * @return saptamana in care a fost predata tema
     */
    private int calculeazaSaptamanaPredare(LocalDate predare) {
        LocalDate startDate = Curent.getStartDate();
        long days = DAYS.between(startDate, predare);
        double saptamanaPredare = Math.ceil((double)days/7);
        return (int)saptamanaPredare;
    }
}
