package entite;

public class Stock {
    private String _id;
    private String _idOption;
    private String _idRack;
    private Integer _quantity;

    public Stock(String _id, String _idOption, String _idRack, Integer _quantity) {
        this._id = _id;
        this._idOption = _idOption;
        this._idRack = _idRack;
        this._quantity = _quantity;
    }

    public String getID() {
        return _id;
    }

    public String getIDOption() {
        return _idOption;
    }

    public String getIDRack() {
        return _idRack;
    }

    public Integer getQuantity() {
        return _quantity;
    }

    public String toString() {
        return "\n" + _id + ";" + _idOption + ";" + _idRack + ";" + _quantity + "\n";
    }

}
