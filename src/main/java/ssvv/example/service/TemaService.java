package ssvv.example.service;

import ssvv.example.curent.Curent;
import ssvv.example.domain.Tema;
import ssvv.example.repository.TemaXMLRepo;
import ssvv.example.validation.TemaValidator;
import ssvv.example.validation.ValidationException;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class TemaService implements ServiceInterface<String, Tema> {
    private TemaXMLRepo repository;
    private TemaValidator validator;

    public TemaService(TemaXMLRepo repository, TemaValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public Tema findOne(String id) {
        checkId(id);
        return repository.findOne(id);

    }

    @Override
    public Iterable<Tema> findAll() {
        return repository.findAll();
    }

    @Override
    public Tema add(Tema entity) throws ValidationException {
        validator.validate(entity);
        return repository.save(entity);
    }

    @Override
    public Tema delete(String id) {
        checkId(id);
        return repository.delete(id);
    }


    @Override
    public Tema update(Tema entity) {
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
     * Prelungeste deadline-ul unei teme
     * @param nrTema - nr-ul temei
     * @param deadline - noul deadline
     */
    public void prelungireDeadline(String nrTema, int deadline){
        int diff= Curent.getCurrentWeek();
        Tema tema = repository.findOne(nrTema);
        if(tema == null){
            throw new ValidationException("Tema inexistenta!");
        }
        if(tema.getDeadline() >= diff) {
            tema.setDeadline(deadline);
            repository.writeToFile();
        }
        else{
            throw new ValidationException("Nu se mai poate prelungi deadline-ul!");
        }
    }


}
