package projetoum.equipe.iteach.models;

import android.widget.Toast;

import com.google.firebase.database.Exclude;

/**
 * Created by Victor on 13-Mar-17.
 */

public class FeedItem {
    public static final int TYPE_CLASS = 0;
    public static final int TYPE_CLASS_SUBTYPE_SUBSCRIBE = 0;
    public static final int TYPE_CLASS_SUBTYPE_TAG = 1;
    public static final int TYPE_CLASS_SUBTYPE_CHANGE = 2;
    public static final int TYPE_CLASS_SUBTYPE_LOCATION = 3;
    public static final int TYPE_FRIEND = 1;
    public static final int STATUS_SHOWING = 0;
    public static final int STATUS_SHOWED = 1;

    public String aulaID;
    public int type;
    public int subtype;
    public String id;
    public int status;

    @Exclude
    private ClassObject aula;


    public FeedItem() {

    }

    public FeedItem(ClassObject aula, int subtype, int status) {
        this.aula = aula;
        aulaID = aula.getId();
        type = TYPE_CLASS;
        this.subtype = subtype;
        this.status = status;
    }

    @Exclude
    public ClassObject getAula() {
        return aula;
    }

    @Exclude
    public void setAula(ClassObject aula) {
        this.aula = aula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedItem feedItem = (FeedItem) o;
        if (feedItem.type == type && feedItem.status == status && feedItem.subtype == subtype && feedItem.aulaID == aulaID)
            return true;

        if (id != null && feedItem.id != null)
            return id.equals(feedItem.id);

        return false;
    }

    @Override
    public String toString() {
        return "id "+id+"subtype "+subtype+"type "+type+"status "+status;
    }
}
