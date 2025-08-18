package fr.raconteur.sbcou.patch;

import fr.raconteur.sbcou.db.versions.DbDataModification;
import fr.raconteur.sbcou.db.versions.DbDataPatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Patch {
    private final DbDataPatch dbPatch;

    /**
     * Private constructor - use Builder to create instances
     *
     * @param name          The name of the patch
     * @param modifications The list of modifications
     */
    private Patch(String name, List<Modification> modifications) {
        // Convert Modification objects to DbDataModification objects
        List<DbDataModification> dbModifications = new ArrayList<>();
        for (Modification modification : modifications) {
            Optional<DbDataModification> dbMod = DbDataModification.getFromDb(modification.getId());
            dbMod.ifPresent(dbModifications::add);
        }

        // Create the DbDataPatch record
        Optional<DbDataPatch> createdPatch = DbDataPatch.create(name, dbModifications);
        if (createdPatch.isEmpty()) {
            throw new RuntimeException("Failed to create DbDataPatch for patch: " + name);
        }
        this.dbPatch = createdPatch.get();
    }

    /**
     * Private constructor from existing DbDataPatch
     *
     * @param dbPatch The existing DbDataPatch instance
     */
    private Patch(DbDataPatch dbPatch) {
        this.dbPatch = dbPatch;
    }

    /**
     * Gets the patch ID
     *
     * @return The patch ID
     */
    public int getId() {
        return dbPatch.getId();
    }

    /**
     * Gets the patch name
     *
     * @return The patch name
     */
    public String getName() {
        return dbPatch.getName();
    }

    /**
     * Gets the modification list as comma-separated string
     *
     * @return The modification list
     */
    public String getModificationList() {
        return dbPatch.getModificationList();
    }

    /**
     * Gets the modification IDs as a list
     *
     * @return List of modification IDs
     */
    public List<Integer> getModificationIds() {
        return dbPatch.getModificationIds();
    }


    /**
     * Gets the actual DbDataModification instances for this patch
     *
     * @return List of DbDataModification instances
     */
    public List<DbDataModification> getDbModifications() {
        return dbPatch.getModifications();
    }

    /**
     * Gets a Patch instance from its ID
     *
     * @param id The patch ID
     * @return An Optional containing the Patch if found, otherwise empty
     */
    public static Optional<Patch> getFromId(int id) {
        Optional<DbDataPatch> dbPatch = DbDataPatch.get(id);
        return dbPatch.map(Patch::new);
    }

    /**
     * Deletes this patch from the database
     *
     * @return true if deletion was successful, false otherwise
     */
    public boolean delete() {
        return dbPatch.deleteInDb();
    }

    /**
     * Builder class for creating Patch instances
     */
    public static class Builder {
        private final String name;
        private final List<Modification> modifications;

        /**
         * Creates a new Builder with the given patch name
         *
         * @param name The name of the patch
         */
        public Builder(String name) {
            this.name = name;
            this.modifications = new ArrayList<>();
        }

        /**
         * Adds a modification to the patch
         *
         * @param modification The modification to add
         * @return This builder instance for method chaining
         */
        public Builder addModification(Modification modification) {
            this.modifications.add(modification);
            return this;
        }

        /**
         * Adds multiple modifications to the patch
         *
         * @param modifications The modifications to add
         * @return This builder instance for method chaining
         */
        public Builder addModifications(List<Modification> modifications) {
            this.modifications.addAll(modifications);
            return this;
        }

        /**
         * Builds and returns the Patch instance
         *
         * @return A new Patch instance
         * @throws RuntimeException if patch creation fails
         */
        public Patch build() {
            return new Patch(name, modifications);
        }
    }
}