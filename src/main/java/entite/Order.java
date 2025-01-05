package entite;

import java.util.List;

public class Order {

    private String _id;
    private Custommer _client;
    private MDTE _mdte;
    private List<Options> _options;
    private float _totalPrice;
    private int _state;
    private String _trackingNumber;

    public Order(String id, Custommer client, MDTE mdte, float totalPrice, int state, String trackingNumber) {
        this._id = id;
        this._client = client;
        this._mdte = mdte;
        this._totalPrice = totalPrice;
        this._state = state;
        this._trackingNumber = trackingNumber;
    }

}
