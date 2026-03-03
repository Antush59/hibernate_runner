package ru.antush.listner;

import org.hibernate.envers.RevisionListener;
import ru.antush.entity.Revision;

public class AntushRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        ((Revision) revisionEntity).setUsername("antush");
    }
}
