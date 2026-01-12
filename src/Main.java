/**
 * Clase principal que arranca la simulacion de la estacion de montaje.
 * Crea la estacion, inicia el trabajo de los operarios y lo detiene despues de 30 segundos.
 */
public class Main {
    /**
     * Metodo principal del programa.
     * 
     * @param args argumentos de linea de comandos (no se usan)
     */
    public static void main(String[] args) {
        EstacionMontaje estacion = new EstacionMontaje();
        estacion.iniciarTrabajo();
        
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Programa interrumpido.");
        }
        
        estacion.detenerTrabajo();
        System.out.println("\nPrograma finalizado.");
    }
}
