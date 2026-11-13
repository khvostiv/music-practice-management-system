package ca.sheridancollege.khvostiv.beans;

public enum LearningStatus {

    WANT_TO_LEARN("Want to Learn"),
    LEARNING("Learning"),
    LEARNED("Learned");
	
    private final String displayName;

    LearningStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
	
}
