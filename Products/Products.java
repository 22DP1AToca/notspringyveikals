class Products{
    String name;
    double price;
    int amountInStorage;
    String description;

    public String toString(){
        return name + ", " + price+ ", " + amountInStorage+ ", " + description;
    }

    public void Display(){
        System.out.println("Nosaukums: " + name);
        System.out.println("Cena: " + price);
        System.out.println("Daudzums noliktavā: " + amountInStorage);
        System.out.println("Apraksts: " +  description);
        System.out.println();
    }
}