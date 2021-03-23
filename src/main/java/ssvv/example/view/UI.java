package ssvv.example.view;


import ssvv.example.domain.Nota;
import ssvv.example.domain.Student;
import ssvv.example.domain.Tema;
import ssvv.example.service.NotaService;
import ssvv.example.service.StudentService;
import ssvv.example.service.TemaService;
import ssvv.example.validation.ValidationException;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Interfata utilizator de tip consola
 */
public class UI {
    private StudentService studentService;
    private TemaService temaService;
    private NotaService notaService;

    /**
     * Class constructor
     * @param studentService - service studenti
     * @param temaService - service teme
     * @param notaService - service note
     */
    public UI(StudentService studentService, TemaService temaService, NotaService notaService) {
        this.studentService = studentService;
        this.temaService = temaService;
        this.notaService = notaService;
    }

    /**
     * Metoda care ruleaza aplicatia
     */
    public void run() {
        System.out.println("Bine ati venit!");
        while (true) {
            try {
                System.out.println("Meniu comenzi: ");
                System.out.println("0.Exit");
                System.out.println("1.Comenzi student");
                System.out.println("2.Comenzi teme");
                System.out.println("3.Comenzi note");
                Scanner scanner = new Scanner(System.in);
                System.out.print("Introduceti comanda: ");
                int comanda = scanner.nextInt();
                switch (comanda){
                    case 0: System.out.println("La revedere!"); return;
                    case 1: meniuStudent(); break;
                    case 2: meniuTeme(); break;
                    case 3: meniuNote(); break;
                    default: System.out.println("Comanda invalida!");
                }
            } catch (ValidationException exception) {
                System.out.println(exception.getMessage());
            } catch (InputMismatchException exception) {
                System.out.println("Date introduse gresit!");
            } catch (ArrayIndexOutOfBoundsException exception) {
                System.out.println("Eroare la introducerea datelor!");
            }
        }
    }

    /**
     * Afiseaza meniul de comenzi asupra studentilor
     */
    private void meniuStudent() {
        while (true) {
            try {
                System.out.println("\n0.Iesire meniu student");
                System.out.println("1.Introducere student");
                System.out.println("2.Stergere student");
                System.out.println("3.Cautare student");
                System.out.println("4.Modificare student");
                System.out.println("5.Afisare lista studenti");
                Scanner scanner = new Scanner(System.in);
                System.out.print("Introduceti comanda: ");
                int comanda = scanner.nextInt();

                switch (comanda) {
                    case 0:
                        return;
                    case 1:
                        adaugaStudent();
                        break;
                    case 2:
                        stergeStudent();
                        break;
                    case 3:
                        cautareStudent();
                        break;
                    case 4:
                        updateStudent();
                        break;
                    case 5:
                        afisareStudenti();
                        break;
                    default:
                        System.out.println("Comanda invalida!");
                        break;
                }
            }
            catch (ValidationException exception) {
                System.out.println(exception.getMessage());
            } catch (InputMismatchException exception) {
                System.out.println("Date introduse gresit!");
            } catch (ArrayIndexOutOfBoundsException exception) {
                System.out.println("Eroare la introducerea datelor!");
            }

        }

    }

    /**
     * Adauga un student
     * @throws ValidationException daca datele studentul exista deja
     */
    private void adaugaStudent() throws ValidationException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceti id student: ");
        String idStudent = scanner.next();
        if (studentService.findOne(idStudent) != null) {
            throw new ValidationException("Studentul exista!");
        }
        System.out.print("Introduceti numele: ");
        scanner.nextLine();
        String numeStudent = scanner.nextLine();



        System.out.print("Introduceti grupa: ");
        int grupa = scanner.nextInt();
        System.out.print("Introduceti email: ");
        String email = scanner.next();

        System.out.print("Introduceti numele profesorului: ");
        scanner.nextLine();
        String numeProfesor = scanner.nextLine();

        Student student = new Student(idStudent, numeStudent, grupa, email, numeProfesor);
        Student student1 = studentService.add(student);

        if (student1 == null) {
            System.out.println("Student adaugat cu succes!");
        } else {
            System.out.println("Studentul deja exista " + student1);
        }
    }

    /**
     * Sterge un student
     */
    private void stergeStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceti id-ul student: pe care doriti sa il stergeti: ");
        String id = scanner.next();
        Student student = studentService.delete(id);
        if (student == null) {
            System.out.println("Studentul nu exista!");
        } else {
            System.out.println("Student sters cu succes!");
        }
    }

    /**
     * Cauta un student
     */
    private void cautareStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceti id-ul studentului: ");
        String id = scanner.next();
        Student student = studentService.findOne(id);
        if (student == null) {
            System.out.println("Studentul nu exista!");
        } else {
            System.out.println(student);
        }
    }

    /**
     * Modifica datele unui student
     */
    private void updateStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceti id-ul studentului pe care doriti sa il modificati: ");
        String id = scanner.next();
        System.out.println("Introduceti datele noi");
        System.out.print("Introduceti numele: ");
        scanner.nextLine();
        String nume = scanner.nextLine();
        System.out.print("Introduceti grupa: ");
        int grupa = scanner.nextInt();
        System.out.print("Introduceti email: ");
        String email = scanner.next();

        System.out.print("Introduceti numele profesorului: ");
        String profesor = scanner.next();
        Student student = new Student(id, nume, grupa, email, profesor);
        Student student1 = studentService.update(student);

        if (student1 == null) {
            System.out.println("Student modificat cu succes!" + student1);
        } else {
            System.out.print("Studentul nu exista!");
        }
    }

    /**
     * Afiseaza lista studentilor
     */
    private void afisareStudenti() {
        Iterable<Student> all = studentService.findAll();
        all.forEach(System.out::println);
    }

    /**
     * Afiseaza comenzile pentru teme
     */
    private void meniuTeme() {

        while (true) {
            try {
                System.out.println("\n0.Iesire meniu teme");
                System.out.println("1.Introducere tema");
                System.out.println("2.Prelungire deadline");
                System.out.println("3.Stergere tema");
                System.out.println("4.Cautare tema");
                System.out.println("5.Modificare tema");
                System.out.println("6.Afisare lista teme");
                Scanner scanner = new Scanner(System.in);
                System.out.print("Introduceti comanda: ");
                int comanda = scanner.nextInt();

                switch (comanda) {
                    case 0:
                        return;
                    case 1:
                        adaugaTema();
                        break;
                    case 2:
                        prelungireDeadline();
                        break;
                    case 3:
                        stergeTema();
                        break;
                    case 4:
                        cautareTema();
                        break;
                    case 5:
                        updateTema();
                        break;
                    case 6:
                        afisareTeme();
                        break;
                    default:
                        System.out.println("Comanda invalida!");
                        break;
                }
            }
            catch (ValidationException exception) {
                System.out.println(exception.getMessage());
            } catch (InputMismatchException exception) {
                System.out.println("Date introduse gresit!");
            } catch (ArrayIndexOutOfBoundsException exception) {
                System.out.println("Eroare la introducerea datelor!");
            }
        }
    }

    /**
     * Adauga o tema
     * @throws ValidationException daca tema exista deja
     */
    private void adaugaTema() throws ValidationException{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceti nr tema: ");
        String nrTema = scanner.next();
        if (temaService.findOne(nrTema) != null) {
            throw new ValidationException("Tema exista deja!");
        }
        System.out.print("Introduceti descrierea: ");
        scanner.nextLine();
        String descriere = scanner.nextLine();
        System.out.print("Introduceti saptamana primirii: ");
        int primire = scanner.nextInt();
        System.out.print("Introduceti deadline-ul(nr saptamanii): ");
        int deadline = scanner.nextInt();
        Tema tema = new Tema(nrTema, descriere, deadline, primire);
        tema = temaService.add(tema);
        if (tema == null) {
            System.out.println("Tema adaugata cu succes!");
        } else {
            System.out.println("Tema deja exista" + tema);
        }
    }


    /**
     * Prelungeste deadline-ul unei teme
     */
    private void prelungireDeadline(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceti id tema: ");
        String nrTema = scanner.next();
        System.out.print("Introduceti deadline nou: ");
        int deadline = scanner.nextInt();
        temaService.prelungireDeadline(nrTema, deadline);
        System.out.println("Dealine prelungit!");
    }

    /**
     * Sterge o tema
     */
    private void stergeTema() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceti id-ul temei: pe care doriti sa o stergeti: ");
        String id = scanner.next();
        Tema tema = temaService.delete(id);
        if (tema == null) {
            System.out.println("Tema nu exista!");
        } else {
            System.out.println("Tema stearsa cu succes!");
        }
    }

    /**
     * Cauta o tema
     */
    private void cautareTema() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceti id-ul temei: ");
        String id = scanner.next();
        Tema tema = temaService.findOne(id);
        if (tema == null) {
            System.out.println("Tema nu exista!");
        } else {
            System.out.println(tema);
        }
    }

    /**
     * Modifica o tema
     */
    private void updateTema() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceti id-ul temei pe care doriti sa o modificati: ");
        String id = scanner.next();
        System.out.println("Introduceti datele noi");
        System.out.print("Introduceti descrierea: ");
        scanner.nextLine();
        String descriere = scanner.nextLine();

        System.out.print("Introduceti saptamana primire: ");
        int primire = scanner.nextInt();

        System.out.print("Introduceti deadline: ");
        int deadline = scanner.nextInt();

        Tema tema = new Tema(id, descriere, deadline, primire);
        Tema tema1 = temaService.update(tema);


        // returned null if the object is modified
        // returned the object if it doesn't exist
        if (tema1 == null) {
            System.out.println("Tema modificata cu succes!" + tema1);
        } else {
            System.out.println("Tema nu exista!");
        }
    }

    /**
     * Afiseaza toate temele
     */
    private void afisareTeme(){
        Iterable<Tema> all = temaService.findAll();
        all.forEach(System.out::println);
    }

    /**
     * Afiseaza comenzile disponibile pentru note
     */
    private void meniuNote() {
        while (true) {
            try {
                System.out.println("\n0.Iesire meniu note");
                System.out.println("1.Introducere nota");
                System.out.println("2.Stergere nota");
                System.out.println("3.Cautare nota");
                System.out.println("4.Afisare lista note");
                Scanner scanner = new Scanner(System.in);
                System.out.print("Introduceti comanda: ");
                int comanda = scanner.nextInt();

                switch (comanda) {
                    case 0:
                        return;
                    case 1:
                        adaugaNota();
                        break;
                    case 2:
                        stergeNota();
                        break;
                    case 3:
                        cautareNota();
                        break;
                    case 4:
                        afisareNote();
                        break;
                    default:
                        System.out.println("Comanda invalida!");
                        break;
                }
            } catch (ValidationException exception) {
                System.out.println(exception.getMessage());
            } catch (InputMismatchException exception) {
                System.out.println("Date introduse gresit!");
            } catch (ArrayIndexOutOfBoundsException exception) {
                System.out.println("Eroare la introducerea datelor!");
            }


        }
    }

    /**
     * Adauga o nota
     * @throws ValidationException daca nota exista deja
     */
    private void adaugaNota() throws ValidationException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceti id student: ");
        String idStudent = scanner.next();
        System.out.print("Introduceti numarul temei: ");
        String nrTema = scanner.next();
        String idNota = idStudent + "#" + nrTema;
        if (notaService.findOne(idNota) != null) {
            throw new ValidationException("Nota exista deja!");
        }
        System.out.print("Introduceti nota: ");
        double nota = scanner.nextDouble();
        System.out.print("Introduceti data predarii temei(format: an-luna-data): ");
        String data = scanner.next();
        String[] date = data.split("-");
        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        System.out.print("Introduceti feedback: ");
        scanner.nextLine();
        String feedback = scanner.nextLine();        //System.out.println(feedback);
        Nota notaCatalog = new Nota(idNota, idStudent, nrTema, nota, dataPredare);
        Nota notaFinala = notaService.add(notaCatalog);
        System.out.println("Nota maxima pe care o poate primi studentul este: " +  notaService.addFeedback(notaFinala, feedback));
    }

    /**
     * Sterge o nota
     */
    private void stergeNota() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceti id-ul studentului: ");
        String idStudent = scanner.next();
        System.out.print("Introduceti nr-ul temei: ");
        String nrTema = scanner.next();
        String idNota = idStudent + "#" + nrTema;
        Nota nota = notaService.delete(idNota);
        if (nota == null) {
            System.out.println("Nota nu exista!");
        } else {
            System.out.println("Nota stearsa cu succes!");
        }
    }

    /**
     * Cauta o nota
     */
    private void cautareNota() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceti id-ul studentului: ");
        String idStudent = scanner.next();
        System.out.print("Introduceti nr-ul temei: ");
        String nrTema = scanner.next();
        String idNota = idStudent + "#" + nrTema;
        Nota nota = notaService.findOne(idNota);
        if (nota == null) {
            System.out.println("Nota nu exista!");
        } else {
            System.out.println(nota);
        }
    }

    /**
     * Afiseaza toate notele
     */
    private void afisareNote() {
        Iterable<Nota> all = notaService.findAll();
        all.forEach(System.out::println);
    }
}
