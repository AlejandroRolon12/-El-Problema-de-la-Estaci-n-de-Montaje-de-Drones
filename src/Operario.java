import java.util.Random;

/**
 * Representa un operario que trabaja en la estacion de montaje.
 * Cada operario es un hilo que repite un ciclo: preparar, coger herramientas,
 * ensamblar y soltar herramientas.
 */
public class Operario extends Thread {
    private final int id;
    private final Herramienta herramientaIzquierda;
    private final Herramienta herramientaDerecha;
    private final Random random;
    private int dronesEnsamblados;
    
    /**
     * Crea un nuevo operario con sus herramientas asignadas.
     * 
     * @param id el numero del operario
     * @param herramientaIzquierda la herramienta que tiene a su izquierda
     * @param herramientaDerecha la herramienta que tiene a su derecha
     */
    public Operario(int id, Herramienta herramientaIzquierda, Herramienta herramientaDerecha) {
        this.id = id;
        this.herramientaIzquierda = herramientaIzquierda;
        this.herramientaDerecha = herramientaDerecha;
        this.random = new Random();
        this.dronesEnsamblados = 0;
        this.setName("Operario-" + id);
    }
    
    /**
     * Metodo principal del hilo. Ejecuta el ciclo de trabajo continuamente.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                preparar();
                solicitarHerramientas();
                ensamblar();
                liberarHerramientas();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[Operario " + id + "] - Interrumpido. Finalizando trabajo.");
                break;
            }
        }
    }
    
    /**
     * El operario organiza sus piezas antes de empezar a trabajar.
     * Espera un tiempo aleatorio entre 500 y 2000 milisegundos.
     * 
     * @throws InterruptedException si el hilo se interrumpe
     */
    private void preparar() throws InterruptedException {
        int tiempo = 500 + random.nextInt(1501);
        System.out.println("[Operario " + id + "] - Preparando piezas...");
        Thread.sleep(tiempo);
    }
    
    /**
     * El operario intenta coger las dos herramientas que necesita.
     * Siempre coge primero la de menor ID para evitar bloqueos.
     * 
     * @throws InterruptedException si el hilo se interrumpe
     */
    private void solicitarHerramientas() throws InterruptedException {
        System.out.println("[Operario " + id + "] - Intentando coger herramientas...");
        
        Herramienta primera;
        Herramienta segunda;
        
        if (herramientaIzquierda.getId() < herramientaDerecha.getId()) {
            primera = herramientaIzquierda;
            segunda = herramientaDerecha;
        } else {
            primera = herramientaDerecha;
            segunda = herramientaIzquierda;
        }
        
        primera.adquirir();
        System.out.println("[Operario " + id + "] - Herramienta " + primera.getId() + " adquirida.");
        
        Thread.sleep(50);
        
        segunda.adquirir();
        System.out.println("[Operario " + id + "] - Herramienta " + segunda.getId() + " adquirida.");
        System.out.println("[Operario " + id + "] - Herramientas adquiridas.");
    }
    
    /**
     * El operario ensambla un dron usando las dos herramientas.
     * Tarda un tiempo aleatorio entre 300 y 1000 milisegundos.
     * 
     * @throws InterruptedException si el hilo se interrumpe
     */
    private void ensamblar() throws InterruptedException {
        dronesEnsamblados++;
        int tiempo = 300 + random.nextInt(701);
        System.out.println("[Operario " + id + "] - Ensamblando dron nº " + dronesEnsamblados + "...");
        Thread.sleep(tiempo);
        System.out.println("[Operario " + id + "] - Dron nº " + dronesEnsamblados + " finalizado.");
    }
    
    /**
     * El operario suelta las herramientas en orden inverso al que las cogio.
     */
    private void liberarHerramientas() {
        System.out.println("[Operario " + id + "] - Finalizado. Soltando herramientas.");
        
        Herramienta primera;
        Herramienta segunda;
        
        if (herramientaIzquierda.getId() < herramientaDerecha.getId()) {
            primera = herramientaIzquierda;
            segunda = herramientaDerecha;
        } else {
            primera = herramientaDerecha;
            segunda = herramientaIzquierda;
        }
        
        segunda.liberar();
        System.out.println("[Operario " + id + "] - Herramienta " + segunda.getId() + " liberada.");
        
        primera.liberar();
        System.out.println("[Operario " + id + "] - Herramienta " + primera.getId() + " liberada.");
    }
    
    /**
     * Devuelve el identificador del operario.
     * 
     * @return el id del operario
     */
    public int getOperarioId() {
        return id;
    }
    
    /**
     * Devuelve cuantos drones ha ensamblado este operario.
     * 
     * @return el numero de drones ensamblados
     */
    public int getDronesEnsamblados() {
        return dronesEnsamblados;
    }
}

