package entite;

import java.util.List;

public class Order {

    private String _id;
    private int _clientId;
    private int _mdteId;
    private List<Integer> _options;
    private float _totalPrice;
    private int _state;
    private String _trackingNumber;

    public Order(String id, int clientId, int mdteId, float totalPrice, int state, String trackingNumber) {
        this._id = id;
        this._clientId = clientId;
        this._mdteId = mdteId;
        this._totalPrice = totalPrice;
        this._state = state;
        this._trackingNumber = trackingNumber;
    }

    public String get_id() {
        return _id;
    }
}
