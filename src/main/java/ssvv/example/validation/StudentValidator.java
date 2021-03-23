package ssvv.example.validation;

import ssvv.example.domain.Student;

public class StudentValidator implements Validator<Student> {

    /**
     * Valideaza un student
     * @param entity - studentul pe care il valideaza
     * @throws ValidationException - daca studentul nu e valid
     */
    @Override
    public void validate(Student entity) throws ValidationException {
        if(entity.getID() == null || entity.getID().equals("")){
            throw new ValidationException("Id incorect!");
        }
        if(entity.getNume() == null || entity.getNume().equals("")){
            throw new ValidationException("Nume incorect!");
        }
        if(entity.getGrupa() <= 0) {
            throw new ValidationException("Grupa incorecta!");
        }
        if(entity.getEmail() == null || entity.getEmail().equals("")){
            throw new ValidationException("Email incorect!");
        }
        if(entity.getTeacher() == null || entity.getTeacher().equals("")){
            throw new ValidationException("Nume incorect!");
        }
    }
}
