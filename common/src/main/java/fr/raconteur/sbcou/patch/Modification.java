package fr.raconteur.sbcou.patch;

import fr.raconteur.sbcou.db.versions.DbDataModification;
import fr.raconteur.sbcou.flatobject.FlatKey;
import fr.raconteur.sbcou.db.versions.DbDataModification.ModificationActionChoice;
import fr.raconteur.sbcou.types.SbcouData;

public class Modification {
    private final DbDataModification dbModification;

    public Modification(FlatKey flatKey, ModificationActionChoice action, int dataValueId) {
        // Use DbDataModification.get() to get or create the modification record
        this.dbModification = DbDataModification.get(flatKey.toString(), dataValueId, action);
    }

    public int getId() {
        return dbModification.getId();
    }

    public FlatKey getFlatKey() {
        return FlatKey.getFlatKeyFromFormattedKey(dbModification.getFlatKey());
    }

    public int getDataValueId() {
        return dbModification.getDataValueId();
    }

    public SbcouData<?> getDataValue() {
        return SbcouData.sbcouDataFromId(dbModification.getDataValueId());
    }

    public ModificationActionChoice getAction() {
        return dbModification.getAction();
    }
}