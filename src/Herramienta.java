/**
 * Representa una herramienta en la mesa de trabajo.
 * Puede ser un soldador o un destornillador dependiendo del operario.
 */
public class Herramienta {
    private final int id;
    private final java.util.concurrent.Semaphore semaforo;
    
    /**
     * Crea una nueva herramienta con un identificador.
     * 
     * @param id el numero de la herramienta
     */
    public Herramienta(int id) {
        this.id = id;
        this.semaforo = new java.util.concurrent.Semaphore(1);
    }
    
    /**
     * Devuelve el identificador de la herramienta.
     * 
     * @return el id de la herramienta
     */
    public int getId() {
        return id;
    }
    
    /**
     * Intenta coger la herramienta. Se queda esperando si esta ocupada.
     * 
     * @throws InterruptedException si el hilo se interrumpe
     */
    public void adquirir() throws InterruptedException {
        semaforo.acquire();
    }
    
    /**
     * Suelta la herramienta para que otro operario la pueda usar.
     */
    public void liberar() {
        semaforo.release();
    }
    
    /**
     * Intenta coger la herramienta sin esperar.
     * 
     * @return true si la consigue, false si esta ocupada
     */
    public boolean intentarAdquirir() {
        return semaforo.tryAcquire();
    }
}

