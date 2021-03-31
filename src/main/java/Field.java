public class Field
{
    private SIGN sign;
    private Plant plant;

    public Field() {
        this.sign = SIGN.EMPTY;
    }

    public boolean isEmpty(){
        return this.sign == SIGN.EMPTY;
    }

    public boolean isFull(){
        return this.sign == SIGN.PLANT;
    }

    public Field updateField(Plant plant) {
        this.sign = this.sign.updateSign();
        this.plant = plant;
        return this;
    }

    public Field updateField() {
        return updateField(sign== SIGN.EMPTY ? null : plant);
    }

    public int getHarvestPrice() {
        return plant.getHarvestPrice();
    }

    @Override
    public String toString() {
        return sign.toString();
    }


    public Field(SIGN sign, Plant plant)
    {
        this.sign = sign;
        this.plant = plant;
    }

    public SIGN getSign()
    {
        return sign;
    }

    public Plant getPlant()
    {
        return plant;
    }

    public Long getPlantTime(){
        return plant.getTime();
    }
}
