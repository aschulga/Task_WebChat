package alexshulga.model;

public class Agent {

    private long id;
    private String name;
   /* private int numberAllSlots;
    private int numberFreeSlots;
    private int numberBusySlots;*/

    public Agent(long id, String name/*, int numberAllSlots, int numberFreeSlots, int numberBusySlots*/){
        this.id = id;
        this.name = name;
       /* this.numberAllSlots = numberAllSlots;
        this.numberFreeSlots = numberFreeSlots;
        this.numberBusySlots = numberBusySlots;*/
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   /* public int getNumberAllSlots() {
        return numberAllSlots;
    }

    public void setNumberAllSlots(int numberAllSlots) {
        this.numberAllSlots = numberAllSlots;
    }

    public int getNumberFreeSlots() {
        return numberFreeSlots;
    }

    public void setNumberFreeSlots(int numberFreeSlots) {
        this.numberFreeSlots = numberFreeSlots;
    }

    public int getNumberBusySlots() {
        return numberBusySlots;
    }

    public void setNumberBusySlots(int numberBusySlots) {
        this.numberBusySlots = numberBusySlots;
    }*/
}
