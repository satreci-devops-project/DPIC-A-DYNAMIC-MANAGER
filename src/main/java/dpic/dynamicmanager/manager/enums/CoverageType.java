package dpic.dynamicmanager.manager.enums;

public enum CoverageType {
    BRANCH("branchCoverage"),
    CLASS("classCoverage"),
    COMPLEXITY_SCORE("complexityScore"),
    INSTRUCTION("instructionCoverage"),
    LINE("lineCoverage"),
    METHOD("methodCoverage");

    String jsonType;

    CoverageType(String jsonType) {
        this.jsonType = jsonType;
    }
}
