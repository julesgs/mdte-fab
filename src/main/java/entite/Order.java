package entite;

import java.util.List;

public class Order {

    private String _id;
    private int _clientId;
    private int _mdteId;
    private List<String> _options;
    private float _totalPrice;
    private int _state;
    private String _trackingNumber;

    public Order(String id, int clientId, int mdteId, List<String> options, float totalPrice, int state, String trackingNumber) {
        this._id = id;
        this._clientId = clientId;
        this._mdteId = mdteId;
        this._options = options;
        this._totalPrice = totalPrice;
        this._state = state;
        this._trackingNumber = trackingNumber;
    }

    public String getID() {
        return _id;
    }

    public Integer getClientID() {
        return _clientId;
    }

    public Integer getMdteID() {
        return _mdteId;
    }

    public List<String> getOptions() {
        return _options;
    }

    public Float getTotalPrice() {
        return _totalPrice;
    }

    public Integer getState() {
        return _state;
    }

    public String getTrackingNumber() {
        return _trackingNumber;
    }

    public String toString() {
        return _id + ";" + _clientId + ";" + _mdteId + ";" + _options + ";" + _totalPrice + ";" + 3 + ";" + _trackingNumber + "\n";
    }
}
