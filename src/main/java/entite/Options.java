package entite;

public class Options {
    private String _id;
    private String _name;
    private String _type;
    private Integer _mdteID;

    public Options(String id, String name, String type, Integer mdte) {
        this._id = id;
        this._name = name;
        this._type = type;
        this._mdteID = mdte;
    }

    public String getID() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public String getType() {
        return _type;
    }

    public Integer getMdteID() {
        return _mdteID;
    }


}
