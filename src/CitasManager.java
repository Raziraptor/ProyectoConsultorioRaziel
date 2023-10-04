import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Doctor {
    private static int contador = 1;
    private int id;
    private String nombre;
    private String especialidad;

    public Doctor(String nombre, String especialidad) {
        this.id = contador++;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }
}

class Paciente {
    private static int contador = 1;
    private int id;
    private String nombre;

    public Paciente(String nombre) {
        this.id = contador++;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}

class Cita {
    private static int contador = 1;
    private int id;
    private String fechaHora;
    private String motivo;
    private Doctor doctor;
    private Paciente paciente;

    public Cita(String fechaHora, String motivo, Doctor doctor, Paciente paciente) {
        this.id = contador++;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.doctor = doctor;
        this.paciente = paciente;
    }

    public int getId() {
        return id;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Paciente getPaciente() {
        return paciente;
    }
}

class Administrador {
    private String username;
    private String password;

    public Administrador(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

class CitasManager {
    private List<Doctor> doctores = new ArrayList<>();
    private List<Paciente> pacientes = new ArrayList<>();
    private List<Cita> citas = new ArrayList<>();
    private List<Administrador> administradores = new ArrayList<>();

    private boolean isLoggedIn = false;

    public CitasManager() {
        administradores.add(new Administrador("admin", "password"));
    }

    public void cargarDatos() {
        cargarDoctores();
        cargarPacientes();
        cargarCitas();
    }

    public void guardarDatos() {
        guardarDoctores();
        guardarPacientes();
        guardarCitas();
    }

    public boolean validarAdministrador(String username, String password) {
        for (Administrador admin : administradores) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public void mostrarMenuPrincipal() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nMenú Principal:");
            System.out.println("1. Iniciar sesión como administrador");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            if (opcion.equals("1")) {
                System.out.print("Ingrese el nombre de usuario: ");
                String username = scanner.nextLine();
                System.out.print("Ingrese la contraseña: ");
                String password = scanner.nextLine();

                if (validarAdministrador(username, password)) {
                    isLoggedIn = true;
                    mostrarMenuAdministrador(scanner);
                } else {
                    System.out.println("Usuario o contraseña incorrectos.");
                }
            } else if (opcion.equals("2")) {
                guardarDatos();
                System.out.println("¡Hasta luego!");
                break;
            } else {
                System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
            }
        }
    }

    public void mostrarMenuAdministrador(Scanner scanner) {
        while (isLoggedIn) {
            System.out.println("\nMenú Administrador:");
            System.out.println("1. Dar de alta doctor");
            System.out.println("2. Dar de alta paciente");
            System.out.println("3. Crear una cita");
            System.out.println("4. Mostrar citas");
            System.out.println("5. Ver doctores");
            System.out.println("6. Ver pacientes");
            System.out.println("7. Eliminar doctor");
            System.out.println("8. Eliminar paciente");
            System.out.println("9. Cancelar cita");
            System.out.println("10. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            if (opcion.equals("1")) {
                darDeAltaDoctor(scanner);
            } else if (opcion.equals("2")) {
                darDeAltaPaciente(scanner);
            } else if (opcion.equals("3")) {
                crearCita(scanner);
            } else if (opcion.equals("4")) {
                mostrarCitas();
            } else if (opcion.equals("5")) {
                verDoctores();
            } else if (opcion.equals("6")) {
                verPacientes();
            } else if (opcion.equals("7")) {
                eliminarDoctor(scanner);
            } else if (opcion.equals("8")) {
                eliminarPaciente(scanner);
            } else if (opcion.equals("9")) {
                cancelarCita(scanner);
            } else if (opcion.equals("10")) {
                isLoggedIn = false;
                System.out.println("Sesión cerrada.");
            } else {
                System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
            }
        }
    }

    public void cargarDoctores() {
        try (BufferedReader br = new BufferedReader(new FileReader("doctores.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] datos = line.split(",");
                if (datos.length == 3) {
                    String nombre = datos[0].trim();
                    String especialidad = datos[1].trim();
                    doctores.add(new Doctor(nombre, especialidad));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarPacientes() {
        try (BufferedReader br = new BufferedReader(new FileReader("pacientes.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] datos = line.split(",");
                if (datos.length == 1) {
                    String nombre = datos[0].trim();
                    pacientes.add(new Paciente(nombre));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarCitas() {
        try (BufferedReader br = new BufferedReader(new FileReader("citas.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] datos = line.split(",");
                if (datos.length == 5) {
                    String fechaHora = datos[0].trim();
                    String motivo = datos[1].trim();
                    int doctorId = Integer.parseInt(datos[2].trim());
                    int pacienteId = Integer.parseInt(datos[3].trim());

                    Doctor doctor = buscarDoctorPorId(doctorId);
                    Paciente paciente = buscarPacientePorId(pacienteId);

                    if (doctor != null && paciente != null) {
                        citas.add(new Cita(fechaHora, motivo, doctor, paciente));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void guardarDoctores() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("doctores.csv"))) {
            for (Doctor doctor : doctores) {
                bw.write(doctor.getNombre() + "," + doctor.getEspecialidad() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guardarPacientes() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("pacientes.csv"))) {
            for (Paciente paciente : pacientes) {
                bw.write(paciente.getNombre() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guardarCitas() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("citas.csv"))) {
            for (Cita cita : citas) {
                bw.write(
                        cita.getFechaHora() + "," +
                                cita.getMotivo() + "," +
                                cita.getDoctor().getId() + "," +
                                cita.getPaciente().getId() + "\n"
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void darDeAltaDoctor(Scanner scanner) {
        System.out.print("Ingrese el nombre del doctor: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese la especialidad del doctor: ");
        String especialidad = scanner.nextLine();

        doctores.add(new Doctor(nombre, especialidad));
        System.out.println("Doctor agregado con éxito.");
        guardarDoctores();
    }

    public void darDeAltaPaciente(Scanner scanner) {
        System.out.print("Ingrese el nombre del paciente: ");
        String nombre = scanner.nextLine();

        pacientes.add(new Paciente(nombre));
        System.out.println("Paciente agregado con éxito.");
        guardarPacientes();
    }

    public void crearCita(Scanner scanner) {
        System.out.print("Ingrese la fecha y hora de la cita (Ejemplo: 2023-10-03 14:30): ");
        String fechaHora = scanner.nextLine();
        System.out.print("Ingrese el motivo de la cita: ");
        String motivo = scanner.nextLine();

        System.out.println("Doctores disponibles:");
        for (Doctor doctor : doctores) {
            System.out.println(doctor.getId() + ". " + doctor.getNombre() + " (" + doctor.getEspecialidad() + ")");
        }
        System.out.print("Seleccione el ID del doctor: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine();  // Limpiar el buffer de entrada
        Doctor doctor = buscarDoctorPorId(doctorId);

        if (doctor == null) {
            System.out.println("Doctor no encontrado.");
            return;
        }

        System.out.println("Pacientes disponibles:");
        for (Paciente paciente : pacientes) {
            System.out.println(paciente.getId() + ". " + paciente.getNombre());
        }
        System.out.print("Seleccione el ID del paciente: ");
        int pacienteId = scanner.nextInt();
        scanner.nextLine();  // Limpiar el buffer de entrada
        Paciente paciente = buscarPacientePorId(pacienteId);

        if (paciente == null) {
            System.out.println("Paciente no encontrado.");
            return;
        }

        citas.add(new Cita(fechaHora, motivo, doctor, paciente));
        System.out.println("Cita creada con éxito.");
        guardarCitas();
    }

    public void mostrarCitas() {
        System.out.println("\nLista de Citas:");
        for (Cita cita : citas) {
            System.out.println("ID: " + cita.getId());
            System.out.println("Fecha y Hora: " + cita.getFechaHora());
            System.out.println("Motivo: " + cita.getMotivo());
            System.out.println("Doctor: " + cita.getDoctor().getNombre() + " (" + cita.getDoctor().getEspecialidad() + ")");
            System.out.println("Paciente: " + cita.getPaciente().getNombre());
            System.out.println("------------------------");
        }
    }

    public void verDoctores() {
        System.out.println("\nLista de Doctores:");
        for (Doctor doctor : doctores) {
            System.out.println("ID: " + doctor.getId());
            System.out.println("Nombre: " + doctor.getNombre());
            System.out.println("Especialidad: " + doctor.getEspecialidad());
            System.out.println("------------------------");
        }
    }

    public void verPacientes() {
        System.out.println("\nLista de Pacientes:");
        for (Paciente paciente : pacientes) {
            System.out.println("ID: " + paciente.getId());
            System.out.println("Nombre: " + paciente.getNombre());
            System.out.println("------------------------");
        }
    }

    public void eliminarDoctor(Scanner scanner) {
        verDoctores();
        System.out.print("Ingrese el ID del doctor a eliminar: ");
        int doctorId = scanner.nextInt();
        Doctor doctor = buscarDoctorPorId(doctorId);

        if (doctor == null) {
            System.out.println("Doctor no encontrado.");
            return;
        }

        doctores.remove(doctor);
        System.out.println("Doctor eliminado con éxito.");
        guardarDoctores();
    }

    public void eliminarPaciente(Scanner scanner) {
        verPacientes();
        System.out.print("Ingrese el ID del paciente a eliminar: ");
        int pacienteId = scanner.nextInt();
        Paciente paciente = buscarPacientePorId(pacienteId);

        if (paciente == null) {
            System.out.println("Paciente no encontrado.");
            return;
        }

        pacientes.remove(paciente);
        System.out.println("Paciente eliminado con éxito.");
        guardarPacientes();
    }

    public void cancelarCita(Scanner scanner) {
        mostrarCitas();
        System.out.print("Ingrese el ID de la cita a cancelar: ");
        int citaId = scanner.nextInt();
        Cita cita = buscarCitaPorId(citaId);

        if (cita == null) {
            System.out.println("Cita no encontrada.");
            return;
        }

        citas.remove(cita);
        System.out.println("Cita cancelada con éxito.");
        guardarCitas();
    }

    private Doctor buscarDoctorPorId(int id) {
        for (Doctor doctor : doctores) {
            if (doctor.getId() == id) {
                return doctor;
            }
        }
        return null;
    }

    private Paciente buscarPacientePorId(int id) {
        for (Paciente paciente : pacientes) {
            if (paciente.getId() == id) {
                return paciente;
            }
        }
        return null;
    }

    private Cita buscarCitaPorId(int id) {
        for (Cita cita : citas) {
            if (cita.getId() == id) {
                return cita;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        CitasManager citasManager = new CitasManager();
        citasManager.cargarDatos();
        citasManager.mostrarMenuPrincipal();
    }
}
