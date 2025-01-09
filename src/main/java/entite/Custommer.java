package entite;

public class Custommer {

    private String _id;
    private String _firstName;
    private String _lastName;
    private String _email;
    private String _adress;

    public Custommer(String _id, String _firstName, String _lastName, String _email, String _adress) {
        this._id = _id;
        this._firstName = _firstName;
        this._lastName = _lastName;
        this._email = _email;
        this._adress = _adress;
    }

    public String getID() {
        return _id;
    }

    public String getFirstName() {
        return _firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public String getEmail() {
        return _email;
    }

    public String getAdress() {
        return _adress;
    }
}
