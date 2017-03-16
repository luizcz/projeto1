package projetoum.equipe.iteach.models;

import android.widget.Toast;

/**
 * Created by Victor on 13-Mar-17.
 */

public class FeedItem {
    public static final int TYPE_CLASS = 0;
    public static final int TYPE_CLASS_SUBTYPE_SUBSCRIBE = 0;
    public static final int TYPE_CLASS_SUBTYPE_ANNOUNCE = 1;
    public static final int TYPE_CLASS_SUBTYPE_NOTIFY = 2;
    public static final int TYPE_CLASS_SUBTYPE_LOCATION = 3;
    public static final int TYPE_FRIEND = 1;

    private ClassObject aula;
    public String aulaID;
    public int type;
    public int subtype;
    public String id;

    public FeedItem() {

    }

    public  FeedItem(ClassObject aula, int subtype) {
        this.aula = aula;
        aulaID = aula.getId();
        type = TYPE_CLASS;
        this.subtype = subtype;
    }

    public ClassObject getAula() {
        return aula;
    }

    public void setAula(ClassObject aula) {
        this.aula = aula;
    }
}
