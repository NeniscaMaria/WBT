package ssvv.example.service;

import ssvv.example.domain.Student;
import ssvv.example.repository.StudentXMLRepo;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.ValidationException;

public class StudentService implements ServiceInterface<String, Student> {
    private StudentXMLRepo repository;
    private StudentValidator validator;

    public StudentService(StudentXMLRepo repository, StudentValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }


    @Override
    public Student findOne(String id) {
        checkId(id);
        return repository.findOne(id);

    }

    @Override
    public Iterable<Student> findAll() {
        return repository.findAll();
    }

    @Override
    public Student add(Student entity) throws ValidationException {
        validator.validate(entity);
        return repository.save(entity);
    }

    @Override
    public Student delete(String id) {
        checkId(id);
        return repository.delete(id);
    }


    @Override
    public Student update(Student entity) {
        validator.validate(entity);
        return repository.update(entity);
    }

    @Override
    public void checkId(String id) {
        if(id == null || id.equals("")) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
    }
}
