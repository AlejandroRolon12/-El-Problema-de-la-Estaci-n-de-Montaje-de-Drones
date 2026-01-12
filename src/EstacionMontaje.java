import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona la estacion de montaje con 5 puestos de trabajo.
 * Crea las herramientas y los operarios en una configuracion circular.
 */
public class EstacionMontaje {
    private static final int NUM_OPERARIOS = 5;
    private final List<Herramienta> herramientas;
    private final List<Operario> operarios;
    
    /**
     * Construye la estacion creando las herramientas y los operarios.
     * Cada operario tiene una herramienta a su izquierda y otra a su derecha,
     * y las herramientas se comparten entre operarios vecinos.
     */
    public EstacionMontaje() {
        this.herramientas = new ArrayList<>();
        this.operarios = new ArrayList<>();
        
        for (int i = 0; i < NUM_OPERARIOS; i++) {
            herramientas.add(new Herramienta(i));
        }
        
        for (int i = 0; i < NUM_OPERARIOS; i++) {
            Herramienta izquierda = herramientas.get(i);
            Herramienta derecha = herramientas.get((i + 1) % NUM_OPERARIOS);
            operarios.add(new Operario(i, izquierda, derecha));
        }
    }
    
    /**
     * Inicia el trabajo de todos los operarios.
     * Cada uno empieza a ejecutarse en su propio hilo.
     */
    public void iniciarTrabajo() {
        System.out.println("=== ESTACIÓN DE MONTAJE DE DRONES ===");
        System.out.println("Iniciando trabajo con " + NUM_OPERARIOS + " operarios...");
        System.out.println("Configuración circular: Cada operario comparte herramientas con sus vecinos.\n");
        
        for (Operario operario : operarios) {
            operario.start();
        }
    }
    
    /**
     * Detiene el trabajo de todos los operarios de forma segura.
     * Interrumpe los hilos y espera a que terminen.
     */
    public void detenerTrabajo() {
        System.out.println("\n=== DETENIENDO ESTACIÓN DE MONTAJE ===");
        
        for (Operario operario : operarios) {
            operario.interrupt();
        }
        
        for (Operario operario : operarios) {
            try {
                operario.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        mostrarEstadisticas();
    }
    
    /**
     * Muestra por pantalla cuantos drones ha ensamblado cada operario.
     */
    private void mostrarEstadisticas() {
        System.out.println("\n=== ESTADISTICAS FINALES ===");
        int totalDrones = 0;
        for (Operario operario : operarios) {
            int drones = operario.getDronesEnsamblados();
            totalDrones += drones;
            System.out.println("Operario " + operario.getOperarioId() + ": " + drones + " drones ensamblados");
        }
        System.out.println("Total de drones ensamblados: " + totalDrones);
    }
    
    /**
     * Devuelve la lista de operarios.
     * 
     * @return la lista de operarios
     */
    public List<Operario> getOperarios() {
        return operarios;
    }
}

