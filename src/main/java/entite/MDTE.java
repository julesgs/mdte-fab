package entite;

public class MDTE {

    private String _id;
    private String _name;
    private float _price;

    public MDTE(String id, String name, float price) {
        this._id = id;
        this._name = name;
        this._price = price;
    }

    public String getID() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public float getPrice() {
        return _price;
    }


}
